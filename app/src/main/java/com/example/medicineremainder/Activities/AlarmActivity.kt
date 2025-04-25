package com.example.medicineremainder.Activities

import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.medicineremainder.R
import com.example.medicineremainder.Utilities.FirebaseManager
import com.example.medicineremainder.databinding.ActivityAlarmBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AlarmActivity : AppCompatActivity() {
     lateinit var medicineName: String
     lateinit var binding: ActivityAlarmBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_FULLSCREEN or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        medicineName = intent.getStringExtra("medicine_name") ?: "Medicine"

       binding.medicineName.text = "Take your medicine: $medicineName"

        binding.btnDismiss.setOnClickListener {
            addTodayToTakenDates(medicineName)
            finish()
        }

    }
    fun addTodayToTakenDates(medicineName: String) {
        val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date())

        // Use your FirebaseManager or Firestore logic here:
        FirebaseManager.addTakenDate(medicineName, todayDate)
    }
}