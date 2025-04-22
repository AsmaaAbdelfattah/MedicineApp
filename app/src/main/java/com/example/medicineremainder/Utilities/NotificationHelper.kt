package com.example.medicineremainder.Utilities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.example.medicineremainder.R
import com.example.medicineremainder.Model.Medicine

object NotificationHelper {
     fun showNotification(context: Context, medicineName: String) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationId = 1

        val notificationChannelId = "med_reminder_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                notificationChannelId,
                "Medication Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, notificationChannelId)
            .setContentTitle("Time to take your medicine")
            .setContentText("It's time to take: $medicineName")
            .setSmallIcon(R.drawable.icon)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)  // Set sound
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(notificationId, notification)
    }

   }