package com.example.authenticatorapp.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class OnBoardingPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)

    fun isFinished(): Boolean {
        return sharedPreferences.getBoolean("isFinished", false)
    }

    fun setFinished() {
        sharedPreferences.edit {
            putBoolean("isFinished", true)
        }
    }
}
