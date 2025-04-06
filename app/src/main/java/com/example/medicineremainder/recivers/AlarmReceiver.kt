package com.example.medicineremainder.recivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.medicineremainder.Model.Medicine
import com.example.medicineremainder.Utilities.NotificationHelper

//TODO AlarmReceiver is only triggered when an alarm fires
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val medicine = intent.getSerializableExtra("medicine") as? Medicine
        if (medicine != null) {
            Log.d("AlarmReceiver", "Showing notification for: ${medicine.name}")
            NotificationHelper.showNotification(context, medicine)
            Toast.makeText(context, "Alarm Triggered!", Toast.LENGTH_SHORT).show()
        }
    }
}