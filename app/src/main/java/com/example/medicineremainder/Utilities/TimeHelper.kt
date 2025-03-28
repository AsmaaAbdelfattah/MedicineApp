package com.example.medicineremainder.Utilities

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object TimeHelper {
    //TODO: get formatted time
    fun getCurrentTimeFormatted(): String {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        return formatter.format(calendar.time)
    }
}