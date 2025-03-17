package com.example.medicineremainder.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.medicineremainder.Adapters.TodayMedicineAdapter
import com.example.medicineremainder.Model.Medicine
import com.example.medicineremainder.Model.MedicineType
import com.example.medicineremainder.Model.User
import com.example.medicineremainder.R
import com.example.medicineremainder.Utilities.FirebaseManager
import com.example.medicineremainder.Utilities.SharedPrefHelper
import com.example.medicineremainder.databinding.FragmentHomeBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters


    lateinit var binding: FragmentHomeBinding
    lateinit var adapter: TodayMedicineAdapter
    lateinit var newsList :MutableList<Medicine>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        binding.time.text = getCurrentTimeFormatted()

         FirebaseManager.currentUserFromDB(requireContext()){ user ->
             if (user != null) {
                 bindUser(user)
             }
         }
//        FirebaseManager.addMedicineToUser(requireContext(),Medicine("third",true,"5 days","5 tablet",true,"10:00 AM", MedicineType.BILLS)){ success ->
//            if (success) {
//                FirebaseManager.currentUserFromDB(requireContext()){ user ->
//                    if (user != null) {
//                        bindUser(user)
//                    }
//                }
//            }else{
//                Toast.makeText(requireContext(), "Cannots amm", Toast.LENGTH_SHORT).show()
//
//            }
//        }
        return binding.root
    }

    //TODO: bind user
     fun bindUser(user:User){
            binding.welcome.text = getString(R.string.hi) + " " + user.name + "\n" + getString(R.string.stay_connrcted_with_your_health)
            newsList = user.medicine
            adapter = TodayMedicineAdapter(newsList)
             binding.todayRecycler.adapter = adapter
             adapter.notifyDataSetChanged()
     }

    //TODO: get formatted time
    fun getCurrentTimeFormatted(): String {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        return formatter.format(calendar.time)
    }
}