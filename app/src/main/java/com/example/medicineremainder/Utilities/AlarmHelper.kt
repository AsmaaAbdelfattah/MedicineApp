package com.example.medicineremainder.Utilities

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.medicineremainder.Model.Medicine
import com.example.medicineremainder.recivers.AlarmReceiver
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object AlarmHelper {

    @SuppressLint("ScheduleExactAlarm")
    fun setAlarm(context: Context, medicine: Medicine) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            val medicineJson = Gson().toJson(medicine)
            putExtra("medicine", medicineJson)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, medicine.medicineId.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, convertTimeToMillis(medicine.time), pendingIntent)

    }

    fun cancelAlarm(context: Context, medicine: Medicine) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context, medicine.medicineId.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
    }

    fun convertTimeToMillis(time: String): Long {
        val calendar = Calendar.getInstance()

        try {
            val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val date = sdf.parse(time)

            if (date != null) {
                val timeCalendar = Calendar.getInstance().apply {
                    timeInMillis = date.time // Use `timeInMillis` instead of overwriting `time`
                }

                // Set the parsed time into today's date
                calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY))
                calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE))
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
            }

            return calendar.timeInMillis
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return calendar.timeInMillis
    }
    fun getALarms(context: Context){
        val user = SharedPrefHelper(context).getUser()
        val currentTime = TimeHelper.getCurrentTimeFormatted()
        user?.medicine?.forEach { medicine ->
                if (medicine.time == currentTime) {
                    Log.d("AlarmHelper", "Setting alarm for medicine: ${medicine.name}")
                       setAlarm(context, medicine)
                    }
                }
        }
    }

