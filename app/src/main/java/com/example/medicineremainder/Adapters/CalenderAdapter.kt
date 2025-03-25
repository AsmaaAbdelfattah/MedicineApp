package com.example.medicineremainder.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.medicineremainder.R
import com.example.medicineremainder.databinding.CalenderRecyclerBinding


class CalendarAdapter(private val days: Pair<List<String>, List<String>>) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {
    var selectedPosition = -1
    class CalendarViewHolder(val binding: CalenderRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = CalenderRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return  CalendarViewHolder(binding)

    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.binding.calendarDayNAme.text = days.first[position]
       holder.binding.calendarDayText.text = days.second[position]
        if (position == selectedPosition) {
            holder.itemView.setBackgroundResource(R.drawable.selected_day_calender) // Use selected layout
        } else {
            holder.itemView.setBackgroundResource(R.drawable.card_view) // Use unselected layout
        }
     holder.itemView.setOnClickListener {
    // Handle item click hereonItemClick(position)

    selectedPosition = position
    notifyDataSetChanged() // Refresh the list to show the selected state
    }
    }

    override fun getItemCount(): Int {
        return days.first.size
    }
}