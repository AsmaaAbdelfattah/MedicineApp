package com.example.medicineremainder.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicineremainder.Adapters.MedciniesAdapter
import com.example.medicineremainder.Utilities.SharedPrefHelper
import com.example.medicineremainder.databinding.FragmentMedicationsBinding


class MedicationsFragment : Fragment() {

    lateinit var binding: FragmentMedicationsBinding
    lateinit var sharedPrefHelper: SharedPrefHelper
    lateinit var adapter: MedciniesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMedicationsBinding.inflate(inflater,container,false)
        sharedPrefHelper = SharedPrefHelper(requireContext())
        val user = sharedPrefHelper.getUser()
        val list = user?.medicine
        binding.total.text = list?.size.toString()
        adapter =  MedciniesAdapter(list ?: mutableListOf())
        binding.medicinesRecycler.layoutManager = LinearLayoutManager(requireContext(),
            RecyclerView.VERTICAL,false)
        binding.medicinesRecycler.adapter = adapter
        adapter.notifyDataSetChanged()
        return binding.root
    }




}