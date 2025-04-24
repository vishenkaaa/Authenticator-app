package com.example.authenticatorapp.data.model

enum class SubscriptionType(val value: String) {
    YEARLY("Yearly"),
    WEEKLY("Weekly");

    companion object {
        fun from(value: String): SubscriptionType? = SubscriptionType.entries.find { it.value == value }
    }
}