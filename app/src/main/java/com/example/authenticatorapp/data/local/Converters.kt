package com.example.authenticatorapp.data.local

import androidx.room.TypeConverter
import com.example.authenticatorapp.data.model.AccountType
import com.example.authenticatorapp.data.model.OtpAlgorithm
import com.example.authenticatorapp.data.model.ServiceName

class Converters {

    @TypeConverter
    fun fromServiceName(value: ServiceName): String {
        return value.displayName
    }

    @TypeConverter
    fun toServiceName(value: String): ServiceName {
        return ServiceName.entries.find { it.displayName.equals(value, ignoreCase = true) }
            ?: ServiceName.UNKNOWN
    }

    @TypeConverter
    fun fromAccountType(value: AccountType): String = value.name

    @TypeConverter
    fun toAccountType(value: String): AccountType =
        AccountType.entries.find { it.name == value } ?: AccountType.TOTP

    @TypeConverter
    fun fromOtpAlgorithm(value: OtpAlgorithm): String = value.name

    @TypeConverter
    fun toOtpAlgorithm(value: String): OtpAlgorithm =
        OtpAlgorithm.entries.find { it.name == value } ?: OtpAlgorithm.SHA1
}
