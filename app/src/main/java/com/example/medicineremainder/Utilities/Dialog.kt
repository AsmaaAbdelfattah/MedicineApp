package com.example.medicineremainder.Utilities

import android.app.AlertDialog
import android.content.Context
import com.example.medicineremainder.R

object Dialog{
    fun showResultDialog(context: Context, title: String, message: String) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(context.getString(R.string.ok)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}