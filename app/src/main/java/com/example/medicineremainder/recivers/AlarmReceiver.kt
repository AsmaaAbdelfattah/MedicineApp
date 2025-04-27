package com.example.medicineremainder.recivers


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.medicineremainder.Activities.AlarmActivity
import com.example.medicineremainder.Activities.MainActivity
import com.example.medicineremainder.R
import com.example.medicineremainder.Utilities.AlarmForegroundService

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val medicineName = intent.getStringExtra("medicine_name") ?: "Your medicine"
        val uniqueId = intent.getIntExtra("medicine_unique_id", -1) // Default -1 if not found

        // Debug
        Log.d("AlarmReceiver", "Received reminder for medicine: $medicineName with ID: $uniqueId")

        val serviceIntent = Intent(context, AlarmForegroundService::class.java).apply {
            putExtra("medicine_name", medicineName)
            putExtra("medicine_unique_id", uniqueId)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }


    private fun showNotification(context: Context, medicineName: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "medicine_reminder_channel"

        // Create notification channel for Android 8+
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

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.icon) // Replace with your actual icon
            .setContentTitle("Medicine Reminder: $medicineName")
            .setContentText("Time to take your medicine: $medicineName")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(medicineName.hashCode(), notification)
    }
}