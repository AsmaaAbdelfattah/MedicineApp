package com.example.medicineremainder.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicineremainder.Adapters.TodayMedicineAdapter
import com.example.medicineremainder.Model.Medicine
import com.example.medicineremainder.Utilities.AlarmHelper
import com.example.medicineremainder.Model.User
import com.example.medicineremainder.R
import com.example.medicineremainder.Utilities.FirebaseManager
import com.example.medicineremainder.Utilities.SharedPrefHelper
import com.example.medicineremainder.Utilities.TimeHelper
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
    lateinit var currentTime:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        currentTime = TimeHelper.getCurrentTimeFormatted()
        binding.time.text = currentTime
        binding.progressBar.visibility = View.VISIBLE
         FirebaseManager.currentUserFromDB(requireContext()){ user ->
             binding.progressBar.visibility = View.GONE
             if (user != null) {
                 SharedPrefHelper(requireContext()).saveUser(user)
                 bindUser(user)
             }
         }

        return binding.root
    }

    //TODO: bind user
     fun bindUser(user:User){
            binding.welcome.text = getString(R.string.hi) + " " + user.name + "\n" + getString(R.string.stay_connrcted_with_your_health)
             newsList = filterMedicinesForToday(user.medicine).toMutableList()
             handleValidateALarm() //TODO handle alarm
             adapter = TodayMedicineAdapter(newsList)
             binding.todayRecycler.layoutManager  = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
             binding.todayRecycler.adapter = adapter
             adapter.notifyDataSetChanged()
     }


    //TODO filter medicine list
    fun filterMedicinesForToday(medicines: MutableList<Medicine>): List<Medicine> {
        val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        return medicines.distinctBy { it.medicineId } // Ensure unique medicines by name
            .filter { medicine ->
                val startDate = medicine.startDate ?: return@filter false
                val endDate = medicine.endDate ?: return@filter false

                // Check if today falls within the medicine's scheduled range
                todayDate >= startDate && todayDate <= endDate
            }
    }

    fun handleValidateALarm(){
       newsList.forEach { medicine ->
         if ( medicine.time == currentTime )
           AlarmHelper.setAlarm(requireContext(), medicine)
       }

    }
}