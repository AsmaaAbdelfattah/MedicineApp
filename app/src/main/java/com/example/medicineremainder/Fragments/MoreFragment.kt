package com.example.medicineremainder.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicineremainder.Activities.PharmaciesActivity
import com.example.medicineremainder.Activities.StatisticsActivity
import com.example.medicineremainder.Adapters.MoreAdapter
import com.example.medicineremainder.Model.MoreData
import com.example.medicineremainder.R
import com.example.medicineremainder.databinding.FragmentMoreBinding

class MoreFragment : Fragment() {
    //TODO: vars
     lateinit var binding: FragmentMoreBinding
    lateinit var moreList: List<MoreData>
    lateinit var adapter: MoreAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMoreBinding.inflate(inflater,container,false)
        moreList = listOf(
            MoreData(getString(R.string.my_profile),R.drawable.ic_pharmacy),
            MoreData(getString(R.string.mystatistics),R.drawable.ic_pharmacy),
            MoreData(getString(R.string.pharamcies),R.drawable.ic_pharmacy)
        )
        adapter = MoreAdapter(moreList,onItemClicked = {
            when(it){
                getString(R.string.my_profile) -> {

                }
                getString(R.string.mystatistics) -> {
                    val intent = Intent(requireContext(), StatisticsActivity::class.java)
                    startActivity(intent)
                }
                getString(R.string.pharamcies) -> {
                    val intent = Intent(requireContext(), PharmaciesActivity::class.java)
                    startActivity(intent)
                }
                else -> {}
            }
        })
        binding.moreRecycler.adapter = adapter
        //GridLayoutManager(requireContext(),2)
        binding.moreRecycler.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)

        adapter.notifyDataSetChanged()
        return binding.root
    }


}