package com.example.medicineremainder.Activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.activity.ComponentActivity
import com.example.medicineremainder.R
import com.example.medicineremainder.Utilities.SharedPrefHelper
import com.example.medicineremainder.databinding.ActivitySplashBinding


class SplashActivity : ComponentActivity() {
    lateinit var sharedPrefHelper: SharedPrefHelper
    lateinit var splashBinding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // enableEdgeToEdge()
        splashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(splashBinding.root)

        sharedPrefHelper = SharedPrefHelper(this)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )
//        Handler(Looper.getMainLooper()).postDelayed({
//
//        },3000)

        val videoUri = Uri.parse("android.resource://$packageName/${R.raw.splash}")
        splashBinding.videoView.setVideoURI(videoUri)

        splashBinding.videoView.setOnCompletionListener {
            proceedAfterSplash()
        }

        splashBinding.videoView.setOnErrorListener { _, _, _ ->
            proceedAfterSplash()
            true
        }

        splashBinding.videoView.start()

    }
    fun proceedAfterSplash() {
        if (sharedPrefHelper.isUserLoggedIn()) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        } else {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)

        }
        finish()
    }
}