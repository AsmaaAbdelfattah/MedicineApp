package com.example.medicineremainder.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.medicineremainder.R
import com.example.medicineremainder.databinding.FragmentStatisticsBinding
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet


class StatisticsFragment : Fragment() {

    lateinit var binding: FragmentStatisticsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentStatisticsBinding.inflate(inflater,container,false)
        setupChart()
        return binding.root
    }
    fun setupChart() {
        val takenData = listOf(10f, 12f, 8f, 9f) // Example data for "Taken"
        val notTakenData = listOf(5f, 3f, 4f, 2f) // Example data for "Not Taken"

        val takenEntries = takenData.mapIndexed { index, value -> BarEntry(index.toFloat(), value) }
        val notTakenEntries = notTakenData.mapIndexed { index, value -> BarEntry(index.toFloat(), value) }

        val takenDataSet = BarDataSet(takenEntries, "Taken").apply {
            color = resources.getColor(R.color.mainAppColor)
        }

        val notTakenDataSet = BarDataSet(notTakenEntries, "Not Taken").apply {
            color = resources.getColor(R.color.lightPurple)
        }

        val dataSet = ArrayList<BarDataSet>().apply {
            add(takenDataSet)
            add(notTakenDataSet)
        }

        val barData = BarData(dataSet as List<IBarDataSet>?)
        binding.barChart.data = barData
        binding.barChart.invalidate() // refresh
    }

}