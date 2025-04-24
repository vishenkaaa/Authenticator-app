
import com.example.authenticatorapp.data.model.OtpAlgorithm
import com.example.authenticatorapp.presentation.utils.NtpTimeProvider.getNtpTime
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.binary.Base32
import java.nio.ByteBuffer
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.math.pow

object OtpGenerator {
    private fun decodeSecret(secret: String): ByteArray {
        return try {
            Base32().decode(secret.replace(" ", "").uppercase())
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid secret key format")
        }
    }

    fun generateTOTP(secret: String, timeStepSeconds: Long = 30, digits: Int = 6, algorithm: OtpAlgorithm = OtpAlgorithm.SHA1
    ): String {
        val ntpTime = System.currentTimeMillis() //getNtpTime() ?: System.currentTimeMillis()
        val currentTimeSeconds = ntpTime / 1000
        val counter = currentTimeSeconds / timeStepSeconds
        return generateOTP(secret, counter, digits, algorithm)
    }

    fun generateHOTP(secret: String, counter: Long, digits: Int = 6, algorithm: OtpAlgorithm = OtpAlgorithm.SHA1): String {
        return generateOTP(secret, counter, digits, algorithm)
    }

    private fun generateOTP(secret: String, counter: Long, digits: Int, algorithm: OtpAlgorithm = OtpAlgorithm.SHA1): String {
        val key = decodeSecret(secret)
        val buffer = ByteBuffer.allocate(8).putLong(0, counter).array()

        val mac = try {
            Mac.getInstance(algorithm.toMacAlgorithm())
        } catch (e: Exception) {
            throw IllegalArgumentException("Unsupported algorithm: $algorithm")
        }

        mac.init(SecretKeySpec(key, algorithm.toMacAlgorithm()))
        val hash = mac.doFinal(buffer)

        val offset = hash.last().toInt() and 0x0F
        val binary = ((hash[offset].toInt() and 0x7F) shl 24) or
                ((hash[offset + 1].toInt() and 0xFF) shl 16) or
                ((hash[offset + 2].toInt() and 0xFF) shl 8) or
                (hash[offset + 3].toInt() and 0xFF)

        val otp = (binary % 10.0.pow(digits.toDouble())).toInt()
        return otp.toString().padStart(digits, '0')
    }
}
