package com.example.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object PinLockManager {
    private const val PREFS_NAME = "secure_pin_app_lock"
    private const val KEY_PIN_ENABLED = "pin_lock_enabled"
    private const val KEY_PIN_HASH = "pin_hash"
    private const val KEY_PIN_SALT = "pin_salt"

    private const val SALT_SIZE_BYTES = 16
    private const val HASH_KEY_LENGTH = 256
    private const val ITERATION_COUNT = 10000

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun isPinLockEnabled(context: Context): Boolean {
        val prefs = getPrefs(context)
        return prefs.getBoolean(KEY_PIN_ENABLED, false) && hasPinSet(context)
    }

    fun hasPinSet(context: Context): Boolean {
        val prefs = getPrefs(context)
        return !prefs.getString(KEY_PIN_HASH, null).isNullOrBlank() &&
                !prefs.getString(KEY_PIN_SALT, null).isNullOrBlank()
    }

    fun savePin(context: Context, pin: String): Boolean {
        if (!pin.matches(Regex("^[0-9]{4,6}$"))) {
            return false
        }
        val secureRandom = SecureRandom()
        val salt = ByteArray(SALT_SIZE_BYTES)
        secureRandom.nextBytes(salt)

        val hash = deriveHash(pin, salt) ?: return false

        val saltBase64 = Base64.encodeToString(salt, Base64.NO_WRAP)
        val hashBase64 = Base64.encodeToString(hash, Base64.NO_WRAP)

        getPrefs(context).edit()
            .putString(KEY_PIN_SALT, saltBase64)
            .putString(KEY_PIN_HASH, hashBase64)
            .putBoolean(KEY_PIN_ENABLED, true)
            .apply()

        return true
    }

    fun verifyPin(context: Context, pin: String): Boolean {
        if (!pin.matches(Regex("^[0-9]{4,6}$"))) {
            return false
        }
        val prefs = getPrefs(context)
        val saltBase64 = prefs.getString(KEY_PIN_SALT, null) ?: return false
        val storedHashBase64 = prefs.getString(KEY_PIN_HASH, null) ?: return false

        val salt = try {
            Base64.decode(saltBase64, Base64.NO_WRAP)
        } catch (e: Exception) {
            return false
        }

        val storedHash = try {
            Base64.decode(storedHashBase64, Base64.NO_WRAP)
        } catch (e: Exception) {
            return false
        }

        val computedHash = deriveHash(pin, salt) ?: return false

        return MessageDigest.isEqual(storedHash, computedHash)
    }

    fun setPinLockEnabled(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_PIN_ENABLED, enabled).apply()
    }

    fun removePin(context: Context) {
        getPrefs(context).edit()
            .remove(KEY_PIN_HASH)
            .remove(KEY_PIN_SALT)
            .putBoolean(KEY_PIN_ENABLED, false)
            .apply()
    }

    private fun deriveHash(pin: String, salt: ByteArray): ByteArray? {
        return try {
            val spec = PBEKeySpec(pin.toCharArray(), salt, ITERATION_COUNT, HASH_KEY_LENGTH)
            val skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
            skf.generateSecret(spec).encoded
        } catch (e: Exception) {
            null
        }
    }
}
