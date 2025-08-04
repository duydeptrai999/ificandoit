package com.xiaomi.base.security

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Comprehensive security manager for the application
 * Handles encryption, biometric authentication, certificate pinning, and secure storage
 */
@Singleton
class SecurityManager @Inject constructor(
    private val context: Context
) {
    
    companion object {
        private const val KEYSTORE_ALIAS = "XiaomiBaseAppKey"
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val ENCRYPTED_PREFS_NAME = "secure_preferences"
        private const val CERTIFICATE_PINS_PREFS = "certificate_pins"
    }
    
    private val masterKey: MasterKey by lazy {
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }
    
    private val encryptedSharedPreferences: SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            context,
            ENCRYPTED_PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
    
    private val biometricManager: BiometricManager by lazy {
        BiometricManager.from(context)
    }
    
    /**
     * Initialize security components
     */
    fun initialize() {
        try {
            generateSecretKey()
            setupCertificatePinning()
            Timber.d("Security manager initialized successfully")
        } catch (e: Exception) {
            Timber.e(e, "Failed to initialize security manager")
        }
    }
    
    /**
     * Generate or retrieve secret key from Android Keystore
     */
    private fun generateSecretKey() {
        try {
            val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
            keyStore.load(null)
            
            if (!keyStore.containsAlias(KEYSTORE_ALIAS)) {
                val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
                val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                    KEYSTORE_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setUserAuthenticationRequired(false)
                    .build()
                
                keyGenerator.init(keyGenParameterSpec)
                keyGenerator.generateKey()
                
                Timber.d("Secret key generated successfully")
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to generate secret key")
            throw SecurityException("Failed to generate secret key", e)
        }
    }
    
    /**
     * Encrypt data using Android Keystore
     */
    fun encryptData(data: String): EncryptedData {
        try {
            val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
            keyStore.load(null)
            
            val secretKey = keyStore.getKey(KEYSTORE_ALIAS, null) as SecretKey
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            
            val encryptedBytes = cipher.doFinal(data.toByteArray())
            val iv = cipher.iv
            
            return EncryptedData(
                encryptedData = encryptedBytes,
                iv = iv
            )
        } catch (e: Exception) {
            Timber.e(e, "Failed to encrypt data")
            throw SecurityException("Failed to encrypt data", e)
        }
    }
    
    /**
     * Decrypt data using Android Keystore
     */
    fun decryptData(encryptedData: EncryptedData): String {
        try {
            val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
            keyStore.load(null)
            
            val secretKey = keyStore.getKey(KEYSTORE_ALIAS, null) as SecretKey
            val cipher = Cipher.getInstance(TRANSFORMATION)
            val spec = GCMParameterSpec(128, encryptedData.iv)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)
            
            val decryptedBytes = cipher.doFinal(encryptedData.encryptedData)
            return String(decryptedBytes)
        } catch (e: Exception) {
            Timber.e(e, "Failed to decrypt data")
            throw SecurityException("Failed to decrypt data", e)
        }
    }
    
    /**
     * Store sensitive data securely
     */
    fun storeSecureData(key: String, value: String) {
        try {
            encryptedSharedPreferences.edit()
                .putString(key, value)
                .apply()
            Timber.d("Secure data stored successfully for key: $key")
        } catch (e: Exception) {
            Timber.e(e, "Failed to store secure data for key: $key")
            throw SecurityException("Failed to store secure data", e)
        }
    }
    
    /**
     * Retrieve sensitive data securely
     */
    fun getSecureData(key: String, defaultValue: String? = null): String? {
        return try {
            encryptedSharedPreferences.getString(key, defaultValue)
        } catch (e: Exception) {
            Timber.e(e, "Failed to retrieve secure data for key: $key")
            defaultValue
        }
    }
    
    /**
     * Remove sensitive data
     */
    fun removeSecureData(key: String) {
        try {
            encryptedSharedPreferences.edit()
                .remove(key)
                .apply()
            Timber.d("Secure data removed successfully for key: $key")
        } catch (e: Exception) {
            Timber.e(e, "Failed to remove secure data for key: $key")
        }
    }
    
    /**
     * Clear all sensitive data
     */
    fun clearAllSecureData() {
        try {
            encryptedSharedPreferences.edit()
                .clear()
                .apply()
            Timber.d("All secure data cleared successfully")
        } catch (e: Exception) {
            Timber.e(e, "Failed to clear all secure data")
        }
    }
    
    /**
     * Check if biometric authentication is available
     */
    fun isBiometricAvailable(): BiometricAvailability {
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricAvailability.AVAILABLE
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> BiometricAvailability.NO_HARDWARE
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> BiometricAvailability.HARDWARE_UNAVAILABLE
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricAvailability.NONE_ENROLLED
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> BiometricAvailability.SECURITY_UPDATE_REQUIRED
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> BiometricAvailability.UNSUPPORTED
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> BiometricAvailability.UNKNOWN
            else -> BiometricAvailability.UNKNOWN
        }
    }
    
    /**
     * Authenticate using biometrics
     */
    suspend fun authenticateWithBiometrics(
        activity: FragmentActivity,
        title: String = "Biometric Authentication",
        subtitle: String = "Use your biometric credential to authenticate",
        negativeButtonText: String = "Cancel"
    ): BiometricResult = suspendCancellableCoroutine { continuation ->
        
        val biometricPrompt = BiometricPrompt(
            activity,
            ContextCompat.getMainExecutor(context),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    continuation.resume(BiometricResult.Error(errorCode, errString.toString()))
                }
                
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    continuation.resume(BiometricResult.Success)
                }
                
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    continuation.resume(BiometricResult.Failed)
                }
            }
        )
        
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setNegativeButtonText(negativeButtonText)
            .build()
        
        biometricPrompt.authenticate(promptInfo)
        
        continuation.invokeOnCancellation {
            biometricPrompt.cancelAuthentication()
        }
    }
    
    /**
     * Setup certificate pinning for network security
     */
    private fun setupCertificatePinning() {
        try {
            // This is a basic implementation
            // In production, you should implement proper certificate pinning
            // using libraries like OkHttp's CertificatePinner or custom implementation
            
            val certificateFactory = CertificateFactory.getInstance("X.509")
            // Add your pinned certificates here
            val pinnedCertificates = mutableListOf<Certificate>()
            
            if (pinnedCertificates.isNotEmpty()) {
                val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
                keyStore.load(null, null)
                
                pinnedCertificates.forEachIndexed { index, certificate ->
                    keyStore.setCertificateEntry("pin_$index", certificate)
                }
                
                val trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm()
                )
                trustManagerFactory.init(keyStore)
                
                val sslContext = SSLContext.getInstance("TLS")
                sslContext.init(null, trustManagerFactory.trustManagers, null)
                
                HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.socketFactory)
                
                Timber.d("Certificate pinning setup completed")
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to setup certificate pinning")
        }
    }
    
    /**
     * Validate certificate pins
     */
    fun validateCertificatePin(certificate: X509Certificate, expectedPins: List<String>): Boolean {
        return try {
            val certificatePin = generateCertificatePin(certificate)
            expectedPins.contains(certificatePin)
        } catch (e: Exception) {
            Timber.e(e, "Failed to validate certificate pin")
            false
        }
    }
    
    /**
     * Generate certificate pin (SHA-256 hash of the certificate)
     */
    private fun generateCertificatePin(certificate: X509Certificate): String {
        val digest = java.security.MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(certificate.encoded)
        return android.util.Base64.encodeToString(hash, android.util.Base64.NO_WRAP)
    }
    
    /**
     * Generate secure random token
     */
    fun generateSecureToken(length: Int = 32): String {
        val secureRandom = java.security.SecureRandom()
        val bytes = ByteArray(length)
        secureRandom.nextBytes(bytes)
        return android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
    }
    
    /**
     * Hash password securely
     */
    fun hashPassword(password: String, salt: String): String {
        val digest = java.security.MessageDigest.getInstance("SHA-256")
        val hash = digest.digest((password + salt).toByteArray())
        return android.util.Base64.encodeToString(hash, android.util.Base64.NO_WRAP)
    }
    
    /**
     * Verify password hash
     */
    fun verifyPassword(password: String, salt: String, expectedHash: String): Boolean {
        val actualHash = hashPassword(password, salt)
        return actualHash == expectedHash
    }
}

/**
 * Data class for encrypted data
 */
data class EncryptedData(
    val encryptedData: ByteArray,
    val iv: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        
        other as EncryptedData
        
        if (!encryptedData.contentEquals(other.encryptedData)) return false
        if (!iv.contentEquals(other.iv)) return false
        
        return true
    }
    
    override fun hashCode(): Int {
        var result = encryptedData.contentHashCode()
        result = 31 * result + iv.contentHashCode()
        return result
    }
}

/**
 * Enum for biometric availability status
 */
enum class BiometricAvailability {
    AVAILABLE,
    NO_HARDWARE,
    HARDWARE_UNAVAILABLE,
    NONE_ENROLLED,
    SECURITY_UPDATE_REQUIRED,
    UNSUPPORTED,
    UNKNOWN
}

/**
 * Sealed class for biometric authentication results
 */
sealed class BiometricResult {
    object Success : BiometricResult()
    object Failed : BiometricResult()
    data class Error(val errorCode: Int, val errorMessage: String) : BiometricResult()
}

/**
 * Security configuration data class
 */
data class SecurityConfig(
    val enableBiometrics: Boolean = true,
    val enableCertificatePinning: Boolean = true,
    val enableEncryption: Boolean = true,
    val sessionTimeoutMinutes: Int = 30,
    val maxFailedAttempts: Int = 3
)

/**
 * Security event listener interface
 */
interface SecurityEventListener {
    fun onSecurityEvent(event: SecurityEvent)
}

/**
 * Security events
 */
sealed class SecurityEvent {
    object BiometricAuthenticationSuccess : SecurityEvent()
    object BiometricAuthenticationFailed : SecurityEvent()
    data class BiometricAuthenticationError(val errorCode: Int, val errorMessage: String) : SecurityEvent()
    object SessionExpired : SecurityEvent()
    object MaxFailedAttemptsReached : SecurityEvent()
    data class CertificatePinningFailed(val hostname: String) : SecurityEvent()
    data class EncryptionError(val operation: String, val error: String) : SecurityEvent()
}