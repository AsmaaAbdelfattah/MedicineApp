package com.example.medicineremainder.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.medicineremainder.Adapters.TodayMedicineAdapter
import com.example.medicineremainder.Model.Medicine
import com.example.medicineremainder.Model.MedicineType
import com.example.medicineremainder.Model.User
import com.example.medicineremainder.R
import com.example.medicineremainder.Utilities.FirebaseManager
import com.example.medicineremainder.Utilities.SharedPrefHelper
import com.example.medicineremainder.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters


    lateinit var binding: FragmentHomeBinding
    lateinit var adapter: TodayMedicineAdapter
    lateinit var newsList :MutableList<Medicine>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        binding.time.text = getCurrentTimeFormatted()

         FirebaseManager.currentUserFromDB(requireContext()){ user ->
             if (user != null) {
                 bindUser(user)

             }
         }

        return binding.root
    }

    //TODO: bind user
     fun bindUser(user:User){
            binding.welcome.text = getString(R.string.hi) + " " + user.name + "\n" + getString(R.string.stay_connrcted_with_your_health)
            newsList = filterMedicinesForToday(user.medicine).toMutableList()
            adapter = TodayMedicineAdapter(newsList)
             binding.todayRecycler.adapter = adapter
             adapter.notifyDataSetChanged()
     }

    //TODO: get formatted time
    fun getCurrentTimeFormatted(): String {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        return formatter.format(calendar.time)
    }
    //TODO filter medicine list
    fun filterMedicinesForToday(medicines: MutableList<Medicine>): List<Medicine> {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        return medicines.filter { medicine ->
            val startDate = medicine.startDate
            val endDate = medicine.endDate
            val frequency = medicine.frequency

            // Medicine must have a valid start date
            if (startDate.isNullOrEmpty()) return@filter false

            // Check if today is within the medicine's duration
            val isWithinRange = (startDate <= today) && (endDate.isNullOrEmpty() || today <= endDate)

            // If frequency is set, check if today matches the schedule
            val isScheduledToday = frequency?.contains(today) ?: true

            isWithinRange && isScheduledToday
        }
    }
}