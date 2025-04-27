package com.example.medicineremainder.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.medicineremainder.Model.MedicineType
import com.example.medicineremainder.R
import com.example.medicineremainder.Utilities.SharedPrefHelper
import com.example.medicineremainder.Utilities.ValidationUtils
import com.example.medicineremainder.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.example.medicineremainder.Model.User
import com.example.medicineremainder.Utilities.Dialog
import com.example.medicineremainder.Utilities.FirebaseManager

class RegisterActivity : AppCompatActivity() {
    lateinit var firebase : FirebaseAuth
    lateinit var registerBinding: ActivityRegisterBinding
    //lateinit var sharedPrefHelper: SharedPrefHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)
        enableEdgeToEdge()
        firebase = FirebaseAuth.getInstance()
      //  sharedPrefHelper = SharedPrefHelper(this)

        registerBinding.registerBtn.setOnClickListener {

                val email = registerBinding.email.text.toString()
                val name = registerBinding.userName.text.toString()
                val age = registerBinding.age.text.toString()
                val password = registerBinding.password.text.toString()
                if (validateRegister(email,name,age,password)){
                    FirebaseManager.createUserWithEmailAndPassword(this,name,email,age.toInt(),password,registerBinding.progressBar,
                        SharedPrefHelper(this)
                    )
               }
             }
    }
    //TODO: validate register
    fun validateRegister(email:String,name:String,age:String, password: String):Boolean{
        if (email.isEmpty() || password.isEmpty() || age.isEmpty() || name.isEmpty()){
            Dialog.showResultDialog(this, "", getString(R.string.please_fill_all_the_fields))
            return false
        }
        if (!ValidationUtils.isValidEmail(email)){
            Dialog.showResultDialog(this, "", getString(R.string.please_Enter_valid_email))
            return false
        }
        return true
    }
}