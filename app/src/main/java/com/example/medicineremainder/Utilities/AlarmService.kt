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
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.medicineremainder.Activities.AlarmActivity
import com.example.medicineremainder.R

class AlarmForegroundService : Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val medicineName = intent.getStringExtra("medicine_name") ?: "medicine"
        val uniqueId = intent.getIntExtra("medicine_unique_id", -1)

        Log.d("AlarmForegroundService", "Showing notification for $medicineName (ID: $uniqueId)")

        val channelId = "medicine_reminder_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Medicine Reminder",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for reminding medicines"
                enableLights(true)
                lightColor = Color.BLUE
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification: Notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.icon)
            .setContentTitle("Time to take your medicine")
            .setContentText(medicineName)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .build()

        startForeground(uniqueId, notification) // <-- startForeground using the uniqueId

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
