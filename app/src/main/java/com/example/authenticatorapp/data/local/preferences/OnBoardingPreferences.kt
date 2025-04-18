package com.example.authenticatorapp.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class OnBoardingPreferences(context: Context) {

    companion object {
        private const val PREF_NAME = "onboarding_prefs"
        private const val KEY_IS_FINISHED = "is_finished"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun getStatus(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_FINISHED, false)
    }

    fun setFinishedStatus() {
        sharedPreferences.edit {
            putBoolean(KEY_IS_FINISHED, true)
        }
    }
}
