package com.example.medicineremainder.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.medicineremainder.R
import com.example.medicineremainder.databinding.CalenderRecyclerBinding

class CalendarAdapter(
    private val days: Pair<List<String>, List<String>>,
    private val selectedDays: MutableList<String> = mutableListOf(),
    private val onDaySelected: (String) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    class CalendarViewHolder(val binding: CalenderRecyclerBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = CalenderRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.binding.calendarDayNAme.text = days.first[position]
        holder.binding.calendarDayText.text = days.second[position]

        // Check if the day is selected
        val day = days.second[position]
        if (selectedDays.contains(day)) {
            holder.itemView.setBackgroundResource(R.drawable.selected_day_calender) // Change to your selected color
        } else {
            holder.itemView.setBackgroundResource(R.drawable.card_view) // Default background
        }

        // Handle item click
        holder.itemView.setOnClickListener {
            if (selectedDays.contains(day)) {
                selectedDays.remove(day) // Deselect if already selected
            } else {
                selectedDays.add(day) // Select if not already selected
            }
            onDaySelected(day) // Notify the selection change
            notifyDataSetChanged() // Refresh the list to show the selected state
        }
    }

    override fun getItemCount(): Int {
        return days.first.size
    }
}