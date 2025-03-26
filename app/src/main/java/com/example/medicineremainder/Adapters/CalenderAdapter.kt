package com.example.medicineremainder.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.medicineremainder.R
import com.example.medicineremainder.databinding.CalenderRecyclerBinding

class CalendarAdapter(
    private val days: Pair<List<String>, List<String>>,
    private val selectedDays: MutableList<String> = mutableListOf(),
    private val onDaySelected: (List<String>) -> Unit // Updated to return list
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    class CalendarViewHolder(val binding: CalenderRecyclerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = CalenderRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val day = days.second[position] // The actual date in "YYYY-MM-DD" format
        holder.binding.calendarDayNAme.text = days.first[position] // Weekday name
        holder.binding.calendarDayText.text = day.substring(8, 10) // Extract only the day number

        // Handle selection UI
        if (selectedDays.contains(day)) {
            holder.itemView.setBackgroundResource(R.drawable.selected_day_calender) // Custom selected card
        } else {
            holder.itemView.setBackgroundResource(R.drawable.card_view) // Default card
        }

        // Handle item click for multi-selection
        holder.itemView.setOnClickListener {
            if (selectedDays.contains(day)) {
                selectedDays.remove(day) // Deselect if already selected
            } else {
                selectedDays.add(day) // Select if not already selected
            }
            onDaySelected(selectedDays.sorted()) // Return sorted selected dates
            notifyDataSetChanged() // Refresh UI
        }
    }

    override fun getItemCount(): Int = days.first.size
}