package com.example.medicineremainder.Activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.medicineremainder.Model.User
import com.example.medicineremainder.R
import com.example.medicineremainder.Utilities.Dialog
import com.example.medicineremainder.Utilities.SharedPrefHelper
import com.example.medicineremainder.Utilities.FirebaseManager
import com.example.medicineremainder.Utilities.ValidationUtils
import com.example.medicineremainder.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth


class LogInActivity : AppCompatActivity() {
    lateinit var logBinding: ActivityLogInBinding
    lateinit var firebase :FirebaseAuth
    lateinit var sharedPrefHelper: SharedPrefHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        logBinding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(logBinding.root)
        firebase = FirebaseAuth.getInstance()
        sharedPrefHelper = SharedPrefHelper(this)

        logBinding.logInBtn.setOnClickListener {
            val email = logBinding.email.text.toString()
            val password =  logBinding.password.text.toString()
             if (validateLogIn(email,password)){
                 FirebaseManager.signInWithEmailAndPassword(this,email,password,logBinding.progressBar,sharedPrefHelper)
             }
        }

    }

    //TODO: validate log in
    fun validateLogIn(email:String, password: String):Boolean{
        if (email.isEmpty() || password.isEmpty()){
            Dialog.showResultDialog(this, "", getString(R.string.please_fill_all_the_fields))
            return false
        }
        if (!ValidationUtils.isValidEmail(email)){
            Dialog.showResultDialog(this, "", getString(R.string.please_Enter_valid_email))
            return false
        }
        return true
    }

    fun registerTapped(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}