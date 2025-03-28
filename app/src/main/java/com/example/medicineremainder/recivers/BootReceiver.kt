package com.example.medicineremainder.recivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.medicineremainder.Utilities.AlarmHelper


//TODO BootReceiver is triggered when the device is rebooted
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            AlarmHelper.getALarms(context)
        }
    }
}