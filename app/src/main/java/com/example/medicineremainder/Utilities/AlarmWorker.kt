package com.example.medicineremainder.Utilities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters


class MedicineReminderWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // Retrieve data passed from the input
        val medicineName = inputData.getString("medicine_name") ?: return Result.failure()

        // Trigger the notification
        triggerNotification(medicineName)

        // Trigger the alarm (you can implement AlarmManager here)
        triggerAlarm()

        return Result.success()
    }

    private fun triggerNotification(medicineName: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel for devices running Android Oreo or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "medicine_reminder_channel",
                "Medicine Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel for medicine reminders"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val notification = NotificationCompat.Builder(applicationContext, "medicine_reminder_channel")
            .setContentTitle("Time to Take Your Medicine")
            .setContentText("It's time to take your medicine: $medicineName")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)  // Automatically removes the notification after clicking
            .setSound(android.provider.Settings.System.DEFAULT_NOTIFICATION_URI)  // Notification sound
            .setPriority(NotificationCompat.PRIORITY_HIGH)  // Make the notification urgent
            .build()

        // Show the notification
        notificationManager.notify(0, notification)
    }

    private fun triggerAlarm() {
        // You can implement AlarmManager here if you need to trigger an alarm sound
    }
}