package com.example.authenticatorapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentSnapshot

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    //TODO стровюємо enum або sealed class
    val serviceName: String,
    val email: String,
    val secret: String,
    //TODO стровюємо enum або sealed class
    val type: String, // "TOTP" або "HOTP"
    //TODO стровюємо enum або sealed class
    val algorithm: String, // "SHA1", "SHA256", "SHA512"
    val digits: Int = 6,
    val counter: Long = 0
)

// для конвертації AccountEntity в Map для збереження в Firebase
//TODO винести літерали в companion object
fun AccountEntity.toFirebaseMap(): Map<String, Any> {
    return mapOf(
        "serviceName" to serviceName,
        "email" to email,
        "secret" to secret,
        "type" to type,
        "algorithm" to algorithm,
        "digits" to digits,
        "counter" to counter,
        "timestamp" to System.currentTimeMillis()
    )
}

// для створення AccountEntity з Map даних Firebase
//FIXME перенести в companion object
fun Map<String, Any>.toAccountEntity(id: Int): AccountEntity? {
    return try {
        AccountEntity(
            id = id, 
            //TODO тут використати вже змінні з companion object
            serviceName = this["serviceName"] as String,
            email = this["email"] as String,
            secret = this["secret"] as String,
            type = this["type"] as String,
            algorithm = this["algorithm"] as String,
            digits = (this["digits"] as Number).toInt(),
            counter = (this["counter"] as Number).toLong()
        )
    } catch (e: Exception) {
        null
    }
}

// для створення AccountEntity з документа Firebase
fun DocumentSnapshot.toAccountEntity(): AccountEntity? {
    val data = this.data ?: return null
    val id = this.id.toIntOrNull() ?: return null
    return data.toAccountEntity(id)
}