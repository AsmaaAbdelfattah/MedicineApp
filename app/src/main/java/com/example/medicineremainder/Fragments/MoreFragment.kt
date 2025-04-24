package com.example.medicineremainder.Fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
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
import java.util.Locale

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
            MoreData(getString(R.string.pharamcies),R.drawable.ic_pharmacy),
            MoreData(getString(R.string.change_language),R.drawable.ic_pharmacy)
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
                getString(R.string.change_language) -> {
                    showLanguageSelectionDialog()
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

    fun showLanguageSelectionDialog( ) {
        val languages = arrayOf("العربية", "English")
        val languageCodes = arrayOf("ar", "en")

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("اختر اللغة / Choose Language")
            .setItems(languages) { _, which ->
                val selectedLanguage = languageCodes[which]
                setAppLocaleAndDirection(requireContext(), selectedLanguage)

                // Save language preference
                val prefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                prefs.edit().putString("language", selectedLanguage).apply()

                // Callback to refresh UI or navigate to Home
               // onLanguageChanged()
            }
            .setNegativeButton("Cancel", null)

        builder.create().show()
    }
    fun setAppLocaleAndDirection(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        // Change layout direction
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }

}