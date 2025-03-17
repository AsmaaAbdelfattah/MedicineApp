package com.example.medicineremainder.Utilities

import android.content.Context
import android.content.Intent
import android.util.Log

import android.view.View
import android.widget.Toast
import com.example.medicineremainder.Activities.MainActivity
import com.example.medicineremainder.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseManager {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // 🔹 Save News to Firestore
    fun saveToDatabase(user: User, context: Context) {
        db.collection("Users").add(user)
            .addOnSuccessListener {
                Toast.makeText(context, "Saved Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to save", Toast.LENGTH_SHORT).show()
            }
    }

    // 🔹 Read News from Firestore
    fun readFromDatabase(onSuccess: (List<User>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("Users").get()
            .addOnSuccessListener { task ->
                val newsList = mutableListOf<User>()
                task.forEach { document ->
                    val news = document.toObject(User::class.java)
                    newsList.add(news)
                }
                onSuccess(newsList)  // ✅ Pass the list to the calling function
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun currentUserFromDB(context: Context, callback: (User?) -> Unit) {
        readFromDatabase(
            onSuccess = { newsList ->
                if (newsList.isNotEmpty()) {
                    val sharedPrefHelper = SharedPrefHelper(context)
                    val user = newsList.firstOrNull { it.userId == sharedPrefHelper.getUser()?.userId }
                    callback(user) // Send result using callback
                } else {
                    callback(null) // No user found
                }
            },
            onFailure = {
                Log.e("FirebaseManager", "Error reading from database", it)
                callback(null) // Error case
            }
        )
    }
    // 🔹 Register User with Email & Password
    fun createUserWithEmailAndPassword(
        context: Context,
        name: String,
        email: String,
        age: Int,
        password:String,
        progressBar: View,
        sharedPrefHelper: SharedPrefHelper
    ) {
        progressBar.visibility = View.VISIBLE
        auth.createUserWithEmailAndPassword(email, password)  // Replace "password" with actual input
            .addOnCompleteListener { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid.toString()
                    val user = User(userId, name, email, age, 0.0, 0.0)
                    sharedPrefHelper.saveUser(user)
                    saveToDatabase(user, context)
                    context.startActivity(Intent(context, MainActivity::class.java))
                } else {
                    Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }

    // 🔹 Sign In User
    fun signInWithEmailAndPassword(
        context: Context,
        email: String,
        password: String,
        progressBar: View,
        sharedPrefHelper: SharedPrefHelper
    ) {
        progressBar.visibility = View.VISIBLE
        auth.signInWithEmailAndPassword(email, password)  // Replace "password" with actual input
            .addOnCompleteListener { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid.toString()
                    val user = User(userId, "", "", 0, 0.0, 0.0)
                    sharedPrefHelper.saveUser(user)
                    context.startActivity(Intent(context, MainActivity::class.java))
                } else {
                    Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }
}
