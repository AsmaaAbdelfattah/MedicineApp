package com.example.medicineremainder.Utilities

import android.content.Context
import android.content.SharedPreferences
import com.example.medicineremainder.Model.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
class SharedPrefHelper(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "MedicinePrefs"
        private const val KEY_USER = "user_data"  // Store a single user
    }

    /** ✅ Save a Single User **/
    fun saveUser(user: User) {
        val gson = Gson()
        if (user.medicine == null) {
            user.medicine = mutableListOf()
        }
        val json = gson.toJson(user)  // Convert user to JSON
        sharedPreferences.edit().putString(KEY_USER, json).apply()
    }

    /** ✅ Retrieve the Stored User **/
    fun getUser(): User? {
        val json = sharedPreferences.getString(KEY_USER, null)
        return if (json != null) {
            Gson().fromJson(json, User::class.java)  // Convert JSON back to User object
        } else {
            null  // No user saved
        }
    }

    /** ✅ Remove the Stored User (Logout) **/
    fun clearUser() {
        sharedPreferences.edit().remove(KEY_USER).apply()
    }
    fun isUserLoggedIn(): Boolean {
        return getUser() != null
    }
}