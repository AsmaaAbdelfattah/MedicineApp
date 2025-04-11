package com.example.medicineremainder.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.medicineremainder.Model.Medicine
import com.example.medicineremainder.Model.MedicineType
import com.example.medicineremainder.R
import com.example.medicineremainder.databinding.MyMedicineItemBinding

class MedciniesAdapter(val list: MutableList<Medicine>):RecyclerView.Adapter<MedciniesAdapter.MyMedicineViewHolder>() {

    inner class MyMedicineViewHolder(val binding: MyMedicineItemBinding): RecyclerView.ViewHolder(binding.root){


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyMedicineViewHolder {
    val binding = MyMedicineItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyMedicineViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: MyMedicineViewHolder, position: Int) {
       holder.binding.medicineTitle.text = list[position].name
        when (list[position].type){
            MedicineType.BILLS -> {
                holder.binding.medicineImg.setImageResource(R.drawable.capsule)

            }
            MedicineType.CREAM -> {
                holder.binding.medicineImg.setImageResource(R.drawable.cream)
            }
            MedicineType.INJECTION -> {
                holder.binding.medicineImg.setImageResource(R.drawable.injection)
            }
            MedicineType.DRINK -> {
                holder.binding.medicineImg.setImageResource(R.drawable.drink)

            }
        }
    }

}