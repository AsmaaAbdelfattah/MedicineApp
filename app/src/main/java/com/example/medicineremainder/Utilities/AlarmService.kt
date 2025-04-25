package com.example.medicineremainder.Utilities

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.example.medicineremainder.Activities.AlarmActivity
import com.example.medicineremainder.R
import android.content.pm.ServiceInfo

class AlarmForegroundService : Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val medicineName = intent.getStringExtra("medicine_name") ?: "Your medicine"

        // Create a persistent notification
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "medicine_reminder_channel"

        // Create the notification channel if the app is running on Android Oreo or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Medicine Reminder",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for medicine reminders"
                enableLights(true)
                lightColor = Color.BLUE
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.icon)  // Your app's icon
            .setContentTitle("Time to take your medicine")
            .setContentText(medicineName)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .build()

        // Start the service in the foreground with a persistent notification
        startForeground(1, notification)

        // Start the AlarmActivity (full-screen)
        val activityIntent = Intent(this, AlarmActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            putExtra("medicine_name", medicineName)
        }
        startActivity(activityIntent)

        return START_NOT_STICKY
    }

        override fun onBind(intent: Intent?): IBinder? = null
}
