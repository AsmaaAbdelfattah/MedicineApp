package com.example.medicineremainder.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.medicineremainder.Model.Feature

import com.example.medicineremainder.databinding.PharmacyItemBinding

class PharmacyAdapter(val list: List<Feature>, val onItemClicked: (Feature) -> Unit): RecyclerView.Adapter<PharmacyAdapter.PharmacyViewHolder>() {

    inner class PharmacyViewHolder(val binding: PharmacyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PharmacyViewHolder {
       val binding = PharmacyItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PharmacyViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: PharmacyViewHolder, position: Int) {
       holder.binding.phamName.text = list[position].properties.name
        holder.binding.loc.text = list[position].properties.street + " , " + list[position].properties.suburb
        holder.itemView.setOnClickListener {
            onItemClicked(list[position])
        }

    }
}