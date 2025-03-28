package com.example.medicineremainder.Utilities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.medicineremainder.R
import com.example.medicineremainder.Model.Medicine

object NotificationHelper {
    private const val CHANNEL_ID = "medicine_reminder_channel"

    fun showNotification(context: Context, medicine: Medicine) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Medicine Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Reminders to take medicine"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_pill)
            .setContentTitle("Time for your medicine!")
            .setContentText("${medicine.name} - ${medicine.dose}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(medicine.medicineId.hashCode(), notification)
    }
}