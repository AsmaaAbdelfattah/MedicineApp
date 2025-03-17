package com.example.medicineremainder.Utilities

import android.content.Context
import android.content.Intent
import android.util.Log

import android.view.View
import android.widget.Toast
import com.example.medicineremainder.Activities.MainActivity
import com.example.medicineremainder.Model.Medicine
import com.example.medicineremainder.Model.User
import com.example.medicineremainder.Model.MedicineType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseManager {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // 🔹 Save News to Firestore
    fun saveToDatabase(user: User) {
        val userRef = db.collection("Users").document(user.userId) // Use UID as document ID

        userRef.set(user)  // Set user data to Firestore
            .addOnSuccessListener {
                Log.d("Firestore", "User saved successfully with ID: ${user.userId}")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error saving user", e)
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
        val sharedPrefHelper = SharedPrefHelper(context)
        val userID = sharedPrefHelper.getUser()?.userId.toString()
        val userRef = db.collection("Users").document(userID)  // Document ID matches userId (UID)

        userRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.d("FirebaseManager", document.toString())
                    val user = document.toObject(User::class.java)
                    user?.let { sharedPrefHelper.saveUser(it) }
                    callback(user)
                } else {
                    Log.e("FirebaseManager", "User not found in Firestore.")
                    callback(null)
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseManager", "Error fetching user data", e)
                callback(null)
            }
    }

    // 🔹 Register User with Email & Password
    fun createUserWithEmailAndPassword(
        context: Context,
        name: String,
        email: String,
        age: Int,
        password: String,
        progressBar: View,
        sharedPrefHelper: SharedPrefHelper
    ) {
        progressBar.visibility = View.VISIBLE
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid  // Ensure userId is fetched here!
                    if (userId != null) {
                        val user = User(userId, name, email, age, 0.0, 0.0)
                        sharedPrefHelper.saveUser(user)
                        saveToDatabase(user) // Save to Firestore with correct UID
                        context.startActivity(Intent(context, MainActivity::class.java))
                    } else {
                        Log.e("Auth", "User ID is null after account creation")
                    }
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
                    val user = User(userId, "", "", 0,0.0, 0.0, MutableList(0,init = {Medicine("",false,"","",true,"",MedicineType.BILLS)}) )
                    sharedPrefHelper.saveUser(user)
                    context.startActivity(Intent(context, MainActivity::class.java))
                } else {
                    Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun addMedicineToUser(context: Context, newMedicine: Medicine, callback: (Boolean) -> Unit) {
        val sharedPrefHelper = SharedPrefHelper(context)
        val userId = sharedPrefHelper.getUser()?.userId.toString()
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId)

        databaseReference.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                // Convert snapshot to User object
                val user = snapshot.getValue(User::class.java)
                // Ensure medicine list is not null
                val updatedMedicineList = user?.medicine?.toMutableList() ?: mutableListOf() // Initialize if null
                // Add new medicine
                updatedMedicineList.add(newMedicine)
                // Update in Firebase
                databaseReference.child("medicine").setValue(updatedMedicineList)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Medicine added successfully", Toast.LENGTH_SHORT).show()
                        Log.d("FirebaseManager", "Medicine added successfully")
                        callback(true)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Failed to add medicine", Toast.LENGTH_SHORT).show()
                        Log.e("FirebaseManager", "Failed to add medicine", e)
                        callback(false)
                    }
            } else {
                Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show()
                Log.e("FirebaseManager", "User not found")
                callback(false)
            }
        }.addOnFailureListener { e ->
            Toast.makeText(context, "Error fetching user", Toast.LENGTH_SHORT).show()
            Log.e("FirebaseManager", "Error fetching user", e)
            callback(false)
        }
    }

}
