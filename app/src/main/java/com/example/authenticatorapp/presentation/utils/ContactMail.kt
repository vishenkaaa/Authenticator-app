package com.example.authenticatorapp.presentation.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.authenticatorapp.R
import androidx.core.net.toUri

fun sendFeedbackEmail(context: Context) {
    val emailUri = "mailto:nahalkaanna06@gmail.com".toUri()
    val intent = Intent(Intent.ACTION_SENDTO, emailUri).apply {
        putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.feedback))
    }

    try {
        context.startActivity(Intent.createChooser(intent,
            context.getString(R.string.choose_email_client)))
    } catch (e: Exception) {
        Toast.makeText(context, context.getString(R.string.no_email_client_found), Toast.LENGTH_LONG).show()
    }
}


