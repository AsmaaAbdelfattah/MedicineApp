package com.example.medicineremainder.recivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.medicineremainder.Utilities.MedicineReminderWorker
import com.example.medicineremainder.Utilities.NotificationHelper
import com.example.medicineremainder.Utilities.SharedPrefHelper


//TODO BootReceiver is triggered when the device is rebooted
//class BootReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context, intent: Intent) {
////        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
////         val user = SharedPrefHelper(context).getUser()
////            if (user != null) {
////            user.medicine.forEach {
////            medicine ->
////            NotificationHelper.showNotification(context,medicine.name)
////              }
////            }
////        }
//    }
//}
