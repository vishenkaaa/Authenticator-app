package com.example.authenticatorapp.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class OnBoardingPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)

    //FIXME isFinished не дає контексту. Що саме ми перевіряємо, показ фото мого кота? улюблений серіал? Краще використати isOnboardingShowed, isOnboardingViewed, isOnboardingFinished etc. Так само для setIsOnboardingShowed..
    fun isFinished(): Boolean { 
        //TODO виносимо літерали в companion object
        return sharedPreferences.getBoolean("isFinished", false)
    }

    fun setFinished() {
        sharedPreferences.edit {
            //TODO літерал
            putBoolean("isFinished", true)
        }
    }
}
