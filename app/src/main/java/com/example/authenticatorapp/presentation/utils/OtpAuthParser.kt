package com.example.authenticatorapp.presentation.utils

import android.util.Log
import com.example.authenticatorapp.data.local.model.AccountEntity
import androidx.core.net.toUri
import com.example.authenticatorapp.data.model.AccountType
import com.example.authenticatorapp.data.model.OtpAlgorithm
import com.example.authenticatorapp.data.model.ServiceName

object OtpAuthParser {
    fun parseOtpAuthUrl(url: String): AccountEntity? {
        try {
            if (!url.startsWith("otpauth://")) {
                return null
            }

            val uri = url.toUri()
            val typeString = uri.host?.uppercase() ?: return null
            val type = AccountType.from(typeString) ?: return null

            val path = uri.path?.removePrefix("/") ?: return null
            val labelParts = path.split(":")

            var serviceName = uri.getQueryParameter("issuer")
            if (serviceName.isNullOrBlank() && labelParts.size > 1) {
                serviceName = labelParts[0]
            } else if (serviceName.isNullOrBlank()) {
                serviceName = "Unknown"
            }
            val service = ServiceName.from(serviceName)

            val email = if (labelParts.size > 1) labelParts[1] else labelParts[0]

            val secret = uri.getQueryParameter("secret") ?: return null
            val algorithmParam = uri.getQueryParameter("algorithm")?.uppercase() ?: "SHA1"
            val algorithm = OtpAlgorithm.from(algorithmParam) ?: OtpAlgorithm.SHA1

            val digits = uri.getQueryParameter("digits")?.toIntOrNull() ?: 6
            val counter = if (type == AccountType.HOTP) {
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