package com.example.medicineremainder.Activities

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.medicineremainder.R
import com.example.medicineremainder.Utilities.SharedPrefHelper
import java.util.Locale
open class BaseActivity : AppCompatActivity() {
        override fun attachBaseContext(newBase: Context) {
            val langCode = SharedPrefHelper(newBase).getLanguage() ?: "en"
            val updatedContext = setAppLocale(newBase, langCode)
            super.attachBaseContext(updatedContext)
        }

    fun setAppLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val resources = context.resources
        val config = resources.configuration

        config.setLocale(locale)
        config.setLayoutDirection(locale) // this sets LTR/RTL

        return context.createConfigurationContext(config)
    }
}