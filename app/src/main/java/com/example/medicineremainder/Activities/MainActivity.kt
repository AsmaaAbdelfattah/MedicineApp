package com.example.medicineremainder.Activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.medicineremainder.Fragments.HomeFragment
import com.example.medicineremainder.Fragments.MedicationsFragment
import com.example.medicineremainder.Fragments.AddMedicineFragment
import com.example.medicineremainder.Fragments.StatisticsFragment
import com.example.medicineremainder.R
import com.example.medicineremainder.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {
lateinit var homeBinding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = MainActivityBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        enableEdgeToEdge()

        ///TODO: ADD FRAGMENTS
            replaceFragment(HomeFragment())
           homeBinding.bottomNav.setOnItemSelectedListener {
              when(it.itemId){
                  R.id.home -> replaceFragment(HomeFragment())
                  R.id.medicines -> replaceFragment(MedicationsFragment())
                  R.id.addMed -> replaceFragment(AddMedicineFragment())
                  R.id.stat -> replaceFragment(StatisticsFragment())
                  else ->{

                  }
              }
              true
           }

    }


    //TODO Create framentes for bottom navigation bar
fun replaceFragment(fragment:Fragment){
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(homeBinding.fragmentContainer.id,fragment)
        fragmentTransaction.commit()
}


}
