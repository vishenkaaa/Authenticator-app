package com.example.authenticatorapp.presentation.model

data class InfoItem(
    val icon: Int,
    val text: String,
    val onClick: () -> Unit
)