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

                    // Get user data as a map
                    val userMap = document.data

                    if (userMap != null) {
                        val medicineList = (userMap["medicine"] as? List<Map<String, Any>>) ?: listOf()
                        val medicineObjects = medicineList.map { medicineMap ->
                            Medicine(
                                name = medicineMap["name"] as? String ?: "",
                                durtion = medicineMap["duration"] as? String ?: "",
                                dose = medicineMap["dose"] as? String ?: "",
                                remindMe = medicineMap["remindMe"] as? Boolean ?: true,
                                dates = (medicineMap["dates"] as? List<String>)?.toMutableList() ?: mutableListOf(),
                                time = medicineMap["time"] as? String ?: "",
                                type = (medicineMap["type"] as? Int)?.let { intType ->
                                    MedicineType.values().firstOrNull { it.type == intType } ?: MedicineType.BILLS
                                } ?: MedicineType.BILLS

                            )
                        }.toMutableList()

                        val user = User(
                            userId = userMap["userId"] as? String ?: "",
                            name = userMap["name"] as? String ?: "",
                            email = userMap["email"] as? String ?: "",
                            age = (userMap["age"] as? Long)?.toInt() ?: 0,
                            latitude = (userMap["latitude"] as? Double) ?: 0.0,
                            longitude = (userMap["longitude"] as? Double) ?: 0.0,
                            medicine = medicineObjects
                        )

                        sharedPrefHelper.saveUser(user)
                        callback(user)
                    } else {
                        Log.e("FirebaseManager", "User data is null in Firestore.")
                        callback(null)
                    }
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
                    val user = User(userId, "", "", 0,0.0, 0.0)
                    sharedPrefHelper.saveUser(user)
                    context.startActivity(Intent(context, MainActivity::class.java))
                } else {
                    Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }

    //TODO add medicine to user list
    fun addMedicineToUser(context: Context, newMedicine: Medicine, callback: (Boolean) -> Unit) {
        val sharedPrefHelper = SharedPrefHelper(context)
        val userId = sharedPrefHelper.getUser()?.userId.toString()
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId)

        databaseReference.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val user = snapshot.getValue(User::class.java)
                val updatedMedicineList = user?.medicine?.toMutableList() ?: mutableListOf()

                // Add the new medicine
                updatedMedicineList.add(newMedicine)

                // Convert MedicineType to Int before saving
                val firebaseMedicineList = updatedMedicineList.map { medicine ->
                    mapOf(
                        "name" to medicine.name,
                        "duration" to medicine.durtion,
                        "dose" to medicine.dose,
                        "remindMe" to medicine.remindMe,
                        "dates" to medicine.dates,
                        "time" to medicine.time,
                        "type" to medicine.type
                        // Store enum as Int
                    )
                }

                // Save back to Firebase
                databaseReference.child("medicine").setValue(firebaseMedicineList)
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
            Log.e("FirebaseManager", "Error fetching user ${userId}", e)
            callback(false)
        }
    }
    fun editMedicineForUser(
        context: Context,
        updatedMedicine: Medicine,
        callback: (Boolean) -> Unit
    ) {
        val sharedPrefHelper = SharedPrefHelper(context)
        val userId = sharedPrefHelper.getUser()?.userId.toString()
        val databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId)

        databaseReference.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val user = snapshot.getValue(User::class.java)
                val updatedMedicineList = user?.medicine?.toMutableList() ?: mutableListOf()

                // Find the medicine to edit (Assuming 'name' is unique; you can use another identifier)
                val index = updatedMedicineList.indexOfFirst { it.name == updatedMedicine.name }
                if (index != -1) {
                    updatedMedicineList[index] = updatedMedicine // Update existing medicine
                } else {
                    callback(false) // Medicine not found
                    return@addOnSuccessListener
                }

                // Convert MedicineType to Int before saving
                val firebaseMedicineList = updatedMedicineList.map { medicine ->
                    mapOf(
                        "name" to medicine.name,
                        "duration" to medicine.durtion,
                        "dose" to medicine.dose,
                        "remindMe" to medicine.remindMe,
                        "dates" to medicine.dates,
                        "time" to medicine.time,
                        "type" to medicine.type // Store enum as Int
                    )
                }

                // Save back to Firebase
                databaseReference.child("medicine").setValue(firebaseMedicineList)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Medicine updated successfully", Toast.LENGTH_SHORT).show()
                        Log.d("FirebaseManager", "Medicine updated successfully")
                        callback(true)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Failed to update medicine", Toast.LENGTH_SHORT).show()
                        Log.e("FirebaseManager", "Failed to update medicine", e)
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
