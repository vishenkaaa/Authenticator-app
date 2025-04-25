package com.example.authenticatorapp.di

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object BiometricModule {

    @Provides
    fun provideFragmentActivity(activity: Activity): FragmentActivity {
        return activity as FragmentActivity
    }
}
