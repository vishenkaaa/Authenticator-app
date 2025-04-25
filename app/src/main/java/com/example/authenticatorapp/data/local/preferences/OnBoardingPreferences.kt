package com.example.authenticatorapp.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class OnBoardingPreferences(context: Context) {

    companion object {
        private const val PREF_NAME = "onboarding_prefs"
        private const val KEY_IS_FINISHED = "onboarding_showed"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    //FIXME isFinished не дає контексту. Що саме ми перевіряємо, показ фото мого кота? улюблений серіал? Краще використати isOnboardingShowed, isOnboardingViewed, isOnboardingFinished etc. Так само для setIsOnboardingShowed..
    //Done
    fun isOnboardingShowed(): Boolean {
        //TODO виносимо літерали в companion object
        return sharedPreferences.getBoolean(KEY_IS_FINISHED, false)
    }

    fun setOnboardingShowed() {
        sharedPreferences.edit {
            //TODO літерал
            //Done
            putBoolean(KEY_IS_FINISHED, true)
        }
    }
}