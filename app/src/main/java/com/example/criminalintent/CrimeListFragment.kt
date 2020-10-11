@file:Suppress("DEPRECATION")

package com.example.criminalintent

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.DateFormat
import java.text.SimpleDateFormat

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    private lateinit var crimeRecyclerView: RecyclerView
    private var adapter: CrimeAdapter? = null

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total crimes: ${crimeListViewModel.crimes.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)

        updateUI()

        return view
    }

    private fun updateUI() {
        val crimes = crimeListViewModel.crimes
        adapter = CrimeAdapter(crimes)
        crimeRecyclerView.adapter = (adapter)
    }

    private open inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        private lateinit var crime: Crime

        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
        private val solvedImageView: ImageView = itemView.findViewById(R.id.crime_solved)

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("SimpleDateFormat")
        open fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title
            // dateTextView.text = this.crime.date.toString()
            //CH

            val pattern = "EEEE, MMMM dd, yyyy"
            val simpleDateFormat = SimpleDateFormat(pattern)
            val dateString = simpleDateFormat.format(crime.date)
            dateTextView.text = dateString

            solvedImageView.visibility = if (crime.isSolved) View.VISIBLE else View.GONE
        }

        override fun onClick(v: View) {
            Toast.makeText(context, "${crime.title} clicked!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private  inner class RequiredCrimeHolder(view: View) : CrimeHolder(view){
        private lateinit var crime: Crime
        val requiredCrimeTextView: TextView = itemView.findViewById(R.id.crime_title)
        val requireddateTextView: TextView = itemView.findViewById(R.id.crime_date)

        override fun bind(crime: Crime) {
            this.crime = crime

            requiredCrimeTextView.text = this.crime.title
            requireddateTextView.text= DateFormat.getDateInstance(DateFormat.FULL).format(this.crime.date).toString()
        }
    }
    private inner class CrimeAdapter(var crimes: List<Crime>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        val requiredCrime = 1
        val normalCrime = 2


        override fun getItemViewType(position: Int): Int {
             return if (crimes[position].requiredPolice == 1)
                requiredCrime
            else
                normalCrime

        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val mrecycleviewholder: RecyclerView.ViewHolder
            when (viewType) {
                requiredCrime -> {

                    val view = layoutInflater.inflate(
                        R.layout.list_item_police_required, parent, false)

                    mrecycleviewholder = RequiredCrimeHolder(view)
                }
                else -> {
                    val view: View = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
                    mrecycleviewholder = CrimeHolder(view)
                }

            }
            return mrecycleviewholder

        }



        override fun getItemCount(): Int {
            return crimes.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            val crime = crimes[position]
            if (holder is RequiredCrimeHolder)
                holder.bind(crime)
            else
                if(holder is CrimeHolder)
                    holder.bind(crime)





        }
    }
    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }
}
