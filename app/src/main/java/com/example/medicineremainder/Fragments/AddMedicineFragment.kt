package com.example.medicineremainder.Fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medicineremainder.Adapters.CalendarAdapter
import com.example.medicineremainder.R
import com.example.medicineremainder.databinding.FragmentAddMedicineBinding
import java.time.LocalDate
import java.util.Calendar
import kotlin.math.log

class AddMedicineFragment : Fragment() {
   lateinit var binding: FragmentAddMedicineBinding
    lateinit var calendarAdapter: CalendarAdapter
    val selectedDates = mutableSetOf<LocalDate>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddMedicineBinding.inflate(layoutInflater,container,false)
        val days = getDaysOfMonth()
         calendarAdapter = CalendarAdapter(days)

         binding.calenderrecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
         binding.calenderrecyclerView.adapter = calendarAdapter
        binding.capsule.setBackgroundResource(R.drawable.selected_day_calender)
        handleMedicineType()
        return binding.root
    }

    fun handleMedicineType(){
        binding.capsule.setOnClickListener {
            binding.capsule.setBackgroundResource(R.drawable.selected_day_calender)
            binding.drink.setBackgroundResource(R.drawable.borders)
            binding.cream.setBackgroundResource(R.drawable.borders)
            binding.injection.setBackgroundResource(R.drawable.borders)
        }
        binding.cream.setOnClickListener {
            binding.capsule.setBackgroundResource(R.drawable.borders)
            binding.drink.setBackgroundResource(R.drawable.borders)
            binding.injection.setBackgroundResource(R.drawable.borders)
            binding.cream.setBackgroundResource(R.drawable.selected_day_calender)
        }
        binding.injection.setOnClickListener {
            binding.capsule.setBackgroundResource(R.drawable.borders)
            binding.drink.setBackgroundResource(R.drawable.borders)
            binding.cream.setBackgroundResource(R.drawable.borders)
            binding.injection.setBackgroundResource(R.drawable.selected_day_calender)
        }
        binding.drink.setOnClickListener {
            binding.capsule.setBackgroundResource(R.drawable.borders)
            binding.drink.setBackgroundResource(R.drawable.selected_day_calender)
            binding.cream.setBackgroundResource(R.drawable.borders)
            binding.injection.setBackgroundResource(R.drawable.borders)
        }
    }

    fun getDaysOfMonth(): Pair<List<String>, List<String>> {
        val calendar = Calendar.getInstance()
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val days = mutableListOf<String>()
        val weekDays = mutableListOf<String>()

        // Weekday names: adjust as needed
        val weekDayNames = arrayOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")

        // Get each day of the month along with its corresponding week day
        for (day in 1..daysInMonth) {
            // Set the calendar to the specific date
            calendar.set(Calendar.DAY_OF_MONTH, day)

            // Get the current weekday index (0=Sunday, 1=Monday, ..., 6=Saturday)
            val weekDayIndex = calendar.get(Calendar.DAY_OF_WEEK) - 1 // Adjust for 0-based index
            days.add(day.toString())
            weekDays.add(weekDayNames[weekDayIndex % 7]) // Ensure it wraps around for index
        }
        return Pair(weekDays, days)
    }


    }