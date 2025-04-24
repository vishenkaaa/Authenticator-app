package com.example.authenticatorapp.di

import android.content.Context
import com.example.authenticatorapp.data.local.preferences.OnBoardingPreferences
import com.example.authenticatorapp.data.local.preferences.SubscriptionPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    @Singleton
    fun provideSubscriptionPreferences(@ApplicationContext context: Context): SubscriptionPreferences {
        return SubscriptionPreferences(context)
    }

    @Provides
    @Singleton
    fun provideOnboardingPreferences(@ApplicationContext context: Context): OnBoardingPreferences {
        return OnBoardingPreferences(context)
    }
}
