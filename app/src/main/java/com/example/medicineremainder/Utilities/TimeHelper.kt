package com.example.medicineremainder.Utilities


import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

object TimeHelper {
    //TODO: get formatted time
    fun getCurrentTimeFormatted(): String {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        val formattedTime = formatter.format(calendar.time)
        return formattedTime.toLowerCase(Locale.ENGLISH)
    }
    fun getAllDatesBetween(start: String, end: String): List<String> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val startDate = LocalDate.parse(start, formatter)
        val endDate = LocalDate.parse(end, formatter)

        val dates = mutableListOf<String>()
        var current = startDate
        while (!current.isAfter(endDate)) {
            dates.add(current.format(formatter))
            current = current.plusDays(1)
        }

        return dates
    }
}