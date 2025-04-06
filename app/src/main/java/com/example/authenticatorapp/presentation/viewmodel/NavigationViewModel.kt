package com.example.authenticatorapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.authenticatorapp.presentation.ui.screens.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigationViewModel : ViewModel() {
    private val _selectedTab = MutableStateFlow(Screen.HOME)
    val selectedTab = _selectedTab.asStateFlow()

    fun selectTab(tab: Screen) {
        _selectedTab.value = tab
    }
}