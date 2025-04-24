package com.example.authenticatorapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.authenticatorapp.presentation.model.TabScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigationViewModel : ViewModel() {
    private val _selectedTab = MutableStateFlow(TabScreen.HOME)
    val selectedTab = _selectedTab.asStateFlow()

    fun selectTab(tab: TabScreen) {
        _selectedTab.value = tab
    }
}