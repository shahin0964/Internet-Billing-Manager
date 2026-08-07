package com.example.util

import java.nio.ByteBuffer
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object BackupEncryptionManager {
    private const val MAGIC_HEADER = "ISPBKUP1"
    private const val SALT_SIZE = 16
    private const val IV_SIZE = 12
    private const val KEY_SIZE_BITS = 256
    private const val GCM_TAG_LENGTH_BITS = 128
    private const val ITERATION_COUNT = 65536

    fun encryptPayload(jsonPayload: String, password: String): ByteArray {
        val secureRandom = SecureRandom()
        val salt = ByteArray(SALT_SIZE)
        secureRandom.nextBytes(salt)

        val iv = ByteArray(IV_SIZE)
        secureRandom.nextBytes(iv)

        val secretKey = deriveKey(password, salt, ITERATION_COUNT)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val gcmSpec = GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec)

        val ciphertext = cipher.doFinal(jsonPayload.toByteArray(Charsets.UTF_8))

        val headerBytes = MAGIC_HEADER.toByteArray(Charsets.US_ASCII)
        val buffer = ByteBuffer.allocate(headerBytes.size + SALT_SIZE + IV_SIZE + 4 + ciphertext.size)
        buffer.put(headerBytes)
        buffer.put(salt)
        buffer.put(iv)
        buffer.putInt(ITERATION_COUNT)
        buffer.put(ciphertext)

        return buffer.array()
    }

    fun decryptPayload(encryptedBytes: ByteArray, password: String): String {
        if (encryptedBytes.size < 8 + SALT_SIZE + IV_SIZE + 4) {
            throw IllegalArgumentException("Invalid backup file: file size too small")
        }

        val buffer = ByteBuffer.wrap(encryptedBytes)
        val headerBytes = ByteArray(8)
        buffer.get(headerBytes)

        val headerStr = String(headerBytes, Charsets.US_ASCII)
        if (headerStr != MAGIC_HEADER) {
            throw IllegalArgumentException("Invalid backup file header format")
        }

        val salt = ByteArray(SALT_SIZE)
        buffer.get(salt)

        val iv = ByteArray(IV_SIZE)
        buffer.get(iv)

        val iterations = buffer.int
        if (iterations <= 0 || iterations > 500000) {
            throw IllegalArgumentException("Invalid backup iterations header")
        }

        val ciphertextLength = buffer.remaining()
        if (ciphertextLength <= 0) {
            throw IllegalArgumentException("Backup ciphertext is empty")
        }

        val ciphertext = ByteArray(ciphertextLength)
        buffer.get(ciphertext)

        val secretKey = deriveKey(password, salt, iterations)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val gcmSpec = GCMParameterSpec(GCM_TAG_LENGTH_BITS, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec)

        val decryptedBytes = cipher.doFinal(ciphertext)
        return String(decryptedBytes, Charsets.UTF_8)
    }

    private fun deriveKey(password: String, salt: ByteArray, iterations: Int): SecretKeySpec {
        val pbeKeySpec = PBEKeySpec(password.toCharArray(), salt, iterations, KEY_SIZE_BITS)
        val secretKeyFactory = try {
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        } catch (e: Exception) {
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        }
        val keyBytes = secretKeyFactory.generateSecret(pbeKeySpec).encoded
        return SecretKeySpec(keyBytes, "AES")
    }
}
