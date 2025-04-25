package com.example.authenticatorapp.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.authenticatorapp.data.model.AccountType
import com.example.authenticatorapp.data.model.OtpAlgorithm
import com.example.authenticatorapp.data.model.ServiceName
import com.google.firebase.firestore.DocumentSnapshot

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    //TODO стровюємо enum або sealed class
    //Done
    @ColumnInfo(name = "serviceName")
    val serviceName: ServiceName,
    val email: String,
    val secret: String,
    //TODO стровюємо enum або sealed class
    //Done
    val type: AccountType,
    //TODO стровюємо enum або sealed class
    //Done
    val algorithm: OtpAlgorithm,
    val digits: Int = 6,
    val counter: Long = 0
){
    //TODO винести літерали в companion object
    //Done
    companion object {
        private const val KEY_SERVICE_NAME = "serviceName"
        private const val KEY_EMAIL = "email"
        private const val KEY_SECRET = "secret"
        private const val KEY_TYPE = "type"
        private const val KEY_ALGORITHM = "algorithm"
        private const val KEY_DIGITS = "digits"
        private const val KEY_COUNTER = "counter"
        private const val KEY_TIMESTAMP = "timestamp"

        private fun fromFirebaseMap(map: Map<String, Any>, id: Int): AccountEntity? {
            return try {
                AccountEntity(
                    id = id,
                    serviceName = ServiceName.from(map[KEY_SERVICE_NAME] as String),
                    email = map[KEY_EMAIL] as String,
                    secret = map[KEY_SECRET] as String,
                    type = AccountType.from(map[KEY_TYPE] as String) ?: return null,
                    algorithm = OtpAlgorithm.from(map[KEY_ALGORITHM] as String) ?: return null,
                    digits = (map[KEY_DIGITS] as Number).toInt(),
                    counter = (map[KEY_COUNTER] as Number).toLong()
                )
            } catch (e: Exception) {
                null
            }
        }

        // для конвертації AccountEntity в Map для збереження в Firebase
        //TODO винести літерали в companion object
        //Done
        fun AccountEntity.toFirebaseMap(): Map<String, Any> {
            return mapOf(
                KEY_SERVICE_NAME to serviceName.displayName,
                KEY_EMAIL to email,
                KEY_SECRET to secret,
                KEY_TYPE to type.value,
                KEY_ALGORITHM to algorithm.value,
                KEY_DIGITS to digits,
                KEY_COUNTER to counter,
                KEY_TIMESTAMP to System.currentTimeMillis()
            )
        }

        // для створення AccountEntity з Map даних Firebase
        //FIXME перенести в companion object
        //Done
        fun Map<String, Any>.toAccountEntity(id: Int): AccountEntity? {
            return fromFirebaseMap(this, id)
        }

        // для створення AccountEntity з документа Firebase
        fun DocumentSnapshot.toAccountEntity(): AccountEntity? {
            val data = this.data ?: return null
            val id = this.id.toIntOrNull() ?: return null
            return data.toAccountEntity(id)
        }
    }
}