package com.example.medicineremainder.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.medicineremainder.Activities.PharmaciesActivity
import com.example.medicineremainder.Model.MoreData
import com.example.medicineremainder.R
import com.example.medicineremainder.databinding.MoreItemBinding



class MoreAdapter(val list: List<MoreData>,val onItemClicked: (String) -> Unit): RecyclerView.Adapter<MoreAdapter.MoreViewHolder>() {

    inner class MoreViewHolder(val binding: MoreItemBinding): RecyclerView.ViewHolder(binding.root){


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreViewHolder {
        val binding = MoreItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return MoreViewHolder(binding)
    }

    override fun getItemCount(): Int {
            return  list.size
    }

    override fun onBindViewHolder(holder: MoreViewHolder, position: Int) {
        holder.binding.moreIcon.setImageResource(list[position].image)
        holder.binding.moreTitle.text = list[position].name
        holder.itemView.setOnClickListener {
            onItemClicked(list[position].name)
        }
    }
}