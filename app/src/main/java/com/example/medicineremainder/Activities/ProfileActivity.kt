package com.example.medicineremainder.Activities

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.medicineremainder.R
import com.example.medicineremainder.Utilities.Dialog
import com.example.medicineremainder.Utilities.FirebaseManager
import com.example.medicineremainder.Utilities.SharedPrefHelper
import com.example.medicineremainder.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {
    //TODO: var
    lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = SharedPrefHelper(this).getUser()
        binding.name.setText(user?.name)
        binding.age.setText(user?.age.toString())

        binding.save.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            if (validateData(binding.name.text.toString(),binding.age.text.toString())){
            FirebaseManager.editUserNameAndAge(this, binding.name.text.toString(), binding.age.text.toString()) {
                if (it) {
                    binding.progressBar.visibility = View.GONE
                    finish()
                }
            }
        }
        }
    }

    fun validateData(name: String, age:String):Boolean {
        if (age.isEmpty() || name.isEmpty()){
            Dialog.showResultDialog(this, "", getString(R.string.please_fill_all_the_fields))
            return false
        }
        return true
    }
}