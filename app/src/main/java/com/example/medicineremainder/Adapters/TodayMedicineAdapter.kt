package com.example.medicineremainder.Adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.medicineremainder.Model.Medicine
import com.example.medicineremainder.Model.MedicineType
import com.example.medicineremainder.R
import com.example.medicineremainder.databinding.TodaysMedicineRecyclerBinding


class TodayMedicineAdapter(val list: MutableList<Medicine>):RecyclerView.Adapter<TodayMedicineAdapter.TodayMedicineViewHolder>() {

    inner class TodayMedicineViewHolder(val binding: TodaysMedicineRecyclerBinding): RecyclerView.ViewHolder(binding.root){


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodayMedicineViewHolder {
        val binding = TodaysMedicineRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  TodayMedicineViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TodayMedicineViewHolder, position: Int) {
        holder.binding.medicineTitle.text = list[position].name
        holder.binding.dose.text = list[position].dose
        holder.binding.time.text = list[position].time
        holder.binding.toggleSwitch.isActivated = list[position].remindMe

        when (list[position].type){
            MedicineType.BILLS -> {
                holder.binding.medicineImage.setImageResource(R.drawable.capsule)
                holder.binding.dosageIcon.setImageResource(R.drawable.ic_pill)
            }
            MedicineType.CREAM -> {
                holder.binding.medicineImage.setImageResource(R.drawable.cream)
                holder.binding.dosageIcon.setImageResource(R.drawable.ic_cream)
            }
            MedicineType.INJECTION -> {
                holder.binding.medicineImage.setImageResource(R.drawable.injection)
                holder.binding.dosageIcon.setImageResource(R.drawable.ic_pen)
            }
            MedicineType.DRINK -> {
                holder.binding.medicineImage.setImageResource(R.drawable.drink)
                holder.binding.dosageIcon.setImageResource(R.drawable.ic_drink)
            }
        }

    }
}