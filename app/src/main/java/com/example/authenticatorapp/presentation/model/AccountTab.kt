package com.example.authenticatorapp.presentation.model

import com.example.authenticatorapp.R

sealed class AccountTab(val titleRez: Int) {
    object TimeBased : AccountTab(R.string.time_based)
    object CounterBased : AccountTab(R.string.counter_based)

    companion object{
        val values = listOf(TimeBased, CounterBased)
        fun fromPage(page: Int) = values[page]
        fun toPage(tab: AccountTab) = values.indexOf(tab)
    }
}