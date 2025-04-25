package com.example.authenticatorapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.authenticatorapp.data.local.preferences.OnBoardingPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(private val onboardingPref: OnBoardingPreferences
) : ViewModel() {

    private val _isOnboardingShowed = MutableStateFlow(false)
    val isOnboardingShowed = _isOnboardingShowed.asStateFlow()

    init {
        _isOnboardingShowed.value = onboardingPref.isOnboardingShowed()
    }

    fun setOnboardingShowed () {
        onboardingPref.setOnboardingShowed()
        _isOnboardingShowed.value = true
    }
}