package com.example.medicineremainder.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicineremainder.Adapters.TodayMedicineAdapter
import com.example.medicineremainder.Model.Medicine

import com.example.medicineremainder.Model.User
import com.example.medicineremainder.R
import com.example.medicineremainder.Utilities.SharedPrefHelper
import com.example.medicineremainder.Utilities.TimeHelper
import com.example.medicineremainder.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

        refreshData()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        refreshData()
    }
    override fun onResume() {
        super.onResume()
        refreshData()
    }
    //TODO: refresh data
    fun refreshData(){
        val user = SharedPrefHelper(requireContext()).getUser()
        if (user != null) {
            bindUser(user)
        }
    }
    //TODO: bind user
     fun bindUser(user:User){

            binding.welcome.text = getString(R.string.hi) + " " + user.name + "\n" + getString(R.string.stay_connrcted_with_your_health)
             newsList = filterMedicinesForToday(user.medicine).toMutableList()
      //  Toast.makeText(requireContext(),newsList.size.toString(),Toast.LENGTH_LONG).show()
             //handleValidateALarm() //TODO handle alarm
             adapter = TodayMedicineAdapter(newsList)
             binding.todayRecycler.layoutManager  = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
             binding.todayRecycler.adapter = adapter
             adapter.notifyDataSetChanged()
             binding.progressBar.visibility = View.GONE
     }

    //TODO filter medicine list
    fun filterMedicinesForToday(medicines: MutableList<Medicine>): List<Medicine> {
        val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        return medicines
            .filter { medicine ->
                val startDate = medicine.startDate ?: return@filter false
                val endDate = medicine.endDate ?: return@filter false

                // Check if today falls within the medicine's scheduled range
                todayDate >= startDate && todayDate <= endDate
            }
    }

}