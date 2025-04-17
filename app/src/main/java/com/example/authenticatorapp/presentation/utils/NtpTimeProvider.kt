package com.example.authenticatorapp.presentation.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.net.ntp.NTPUDPClient
import java.net.InetAddress

object NtpTimeProvider {
    suspend fun getNtpTime(): Long? = withContext(Dispatchers.IO) {
        try {
            val client = NTPUDPClient()
            client.defaultTimeout = 3000
            val inetAddress = InetAddress.getByName("time.google.com")
            val info = client.getTime(inetAddress)
            info.message.transmitTimeStamp.time
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}