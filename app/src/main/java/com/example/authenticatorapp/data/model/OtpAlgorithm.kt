package com.example.authenticatorapp.data.model

enum class OtpAlgorithm(val value: String) {
    SHA1("SHA1"),
    SHA256("SHA256"),
    SHA512("SHA512");

    fun toMacAlgorithm(): String {
        return when (this) {
            SHA1 -> "HmacSHA1"
            SHA256 -> "HmacSHA256"
            SHA512 -> "HmacSHA512"
        }
    }

    companion object {
        fun from(value: String): OtpAlgorithm? = entries.find { it.value == value }
    }
}
