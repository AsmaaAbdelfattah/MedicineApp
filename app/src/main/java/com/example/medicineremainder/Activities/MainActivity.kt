package com.example.medicineremainder.Activities

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.medicineremainder.Fragments.HomeFragment
import com.example.medicineremainder.Fragments.MedicationsFragment
import com.example.medicineremainder.Fragments.AddMedicineFragment
import com.example.medicineremainder.Fragments.StatisticsFragment
import com.example.medicineremainder.R
import com.example.medicineremainder.databinding.MainActivityBinding
import com.example.medicineremainder.recivers.AlarmReceiver
import java.util.Calendar
import android.provider.Settings
import android.net.Uri
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.medicineremainder.Model.Medicine
import com.example.medicineremainder.Utilities.FirebaseManager
import com.example.medicineremainder.Utilities.MedicineReminderWorker
import com.example.medicineremainder.Utilities.SharedPrefHelper
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    lateinit var homeBinding: MainActivityBinding
    lateinit var alarmManager: AlarmManager
    lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = MainActivityBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        enableEdgeToEdge()

        // Ensure POST_NOTIFICATIONS permission for API >= 33
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }

        getUserData()

        // Fragment navigation
        replaceFragment(HomeFragment())
        homeBinding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.medicines -> replaceFragment(MedicationsFragment())
                R.id.addMed -> replaceFragment(AddMedicineFragment())
                R.id.stat -> replaceFragment(StatisticsFragment())
                else -> {}
            }
            true
        }
    }

    private fun getUserData() {
        FirebaseManager.currentUserFromDB(this) { user ->
            if (user != null) {
                SharedPrefHelper(this).saveUser(user)
                user.medicine.forEach { medicine ->
                    scheduleAlarm(medicine)
                }
            }
        }
    }

    private fun scheduleAlarm(medicine: Medicine) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.parse(medicine.startDate) ?: return

        val timeFormat = DateTimeFormatter.ofPattern("hh:mm a")
        val parsedTime = LocalTime.parse(medicine.time, timeFormat)

        val calendar = Calendar.getInstance().apply {
            time = date
            set(Calendar.HOUR_OF_DAY, parsedTime.hour)
            set(Calendar.MINUTE, parsedTime.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_MONTH, 1)
            }
        }

        setAlarm(calendar.timeInMillis, medicine.name)
    }

    private fun setAlarm(triggerTime: Long, medicineName: String) {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, AlarmReceiver::class.java).apply {
            putExtra("medicine_name", medicineName)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            medicineName.hashCode(),  // Unique request code per medicine
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                val requestIntent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    data = Uri.parse("package:$packageName")
                }
                startActivity(requestIntent)
                Toast.makeText(this, "Enable exact alarm permission", Toast.LENGTH_LONG).show()
                return
            }
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
            }
            Toast.makeText(this, "Alarm set for $medicineName", Toast.LENGTH_SHORT).show()
        } catch (e: SecurityException) {
            Toast.makeText(this, "Alarm failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    // Fragment handling
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(homeBinding.fragmentContainer.id, fragment)
        fragmentTransaction.commit()
    }
}
