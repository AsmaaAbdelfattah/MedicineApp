package com.example.medicineremainder.recivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.medicineremainder.Model.Medicine
import com.example.medicineremainder.Utilities.NotificationHelper

//TODO AlarmReceiver is only triggered when an alarm fires
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val medicine = intent.getSerializableExtra("medicine") as? Medicine
        if (medicine != null) {
            NotificationHelper.showNotification(context, medicine)
        }
    }
}