package com.example.authenticatorapp.presentation.utils

import android.net.Uri
import android.util.Log
import com.example.authenticatorapp.data.local.model.AccountEntity

object OtpAuthParser {
    fun parseOtpAuthUrl(url: String): AccountEntity? {
        try {
            if (!url.startsWith("otpauth://")) {
                return null
            }

            val uri = Uri.parse(url)
            val type = when (uri.host?.lowercase()) {
                "totp" -> "TOTP"
                "hotp" -> "HOTP"
                else -> return null
            }

            val path = uri.path?.removePrefix("/") ?: return null
            val labelParts = path.split(":")

            var service = uri.getQueryParameter("issuer")
            if (service.isNullOrBlank() && labelParts.size > 1) {
                service = labelParts[0]
            } else if (service.isNullOrBlank()) {
                service = "Unknown"
            }

            val email = if (labelParts.size > 1) labelParts[1] else labelParts[0]

            val secret = uri.getQueryParameter("secret") ?: return null
            val algorithm = when (uri.getQueryParameter("algorithm")?.uppercase()) {
                "SHA1" -> "HmacSHA1"
                "SHA256" -> "HmacSHA256"
                "SHA512" -> "HmacSHA512"
                else -> "HmacSHA1"
            }

            val digits = uri.getQueryParameter("digits")?.toIntOrNull() ?: 6
            val counter = if (type == "HOTP") {
                uri.getQueryParameter("counter")?.toLongOrNull() ?: 0
            } else 0

            return AccountEntity(
                serviceName = service,
                email = email,
                secret = secret,
                type = type,
                algorithm = algorithm,
                digits = digits,
                counter = counter
            )
        } catch (e: Exception) {
            Log.e("OtpAuthParser", "Error parsing OTP URL", e)
            return null
        }
    }
}