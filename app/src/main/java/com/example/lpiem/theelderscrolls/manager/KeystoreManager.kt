package com.example.lpiem.theelderscrolls.manager

import android.content.Context
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.text.TextUtils
import android.util.Base64
import timber.log.Timber
import java.math.BigInteger
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.spec.AlgorithmParameterSpec
import java.util.*
import javax.crypto.Cipher
import javax.security.auth.x500.X500Principal

class KeystoreManager(val context: Context) {

    val tag = "KeystoreService"

    private val androidKeystore = "AndroidKeyStore"

    private val keyStore: KeyStore

    init {
        keyStore = KeyStore.getInstance(androidKeystore)
        keyStore.load(null)
    }

    fun deleteAlias(alias: String) {
        try {
            keyStore.deleteEntry(alias)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun generateNewKey(alias: String) {
        try {
            // Create new key if needed
            val start = Calendar.getInstance()
            val end = Calendar.getInstance()
            end.add(Calendar.YEAR, 1)

            val generator: KeyPairGenerator = KeyPairGenerator.getInstance("RSA", androidKeystore)

            val spec: AlgorithmParameterSpec
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                spec = KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_DECRYPT)
                        .setCertificateSubject(X500Principal("CN=Sample Name, O=Android Authority"))
                        .setCertificateSerialNumber(BigInteger.ONE)
                        .setCertificateNotBefore(start.time)
                        .setCertificateNotAfter(end.time)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                        .build()
            } else {
                spec = KeyPairGeneratorSpec.Builder(context)
                        .setAlias(alias)
                        .setSubject(X500Principal("CN=Sample Name, O=Android Authority"))
                        .setSerialNumber(BigInteger.ONE)
                        .setStartDate(start.time)
                        .setEndDate(end.time)
                        .build()
            }
            generator.initialize(spec)
            generator.generateKeyPair()
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    fun encryptString(alias: String, textToEncrypt: String?): String? {
        try {
            if (!keyStore.containsAlias(alias)) {
                generateNewKey(alias)
            }
            val privateKeyEntry = keyStore.getEntry(alias, null) as KeyStore.PrivateKeyEntry
            val publicKey = privateKeyEntry.certificate.publicKey

            // Encrypt the text
            return if (TextUtils.isEmpty(textToEncrypt)) {
                Timber.e("Empty string to save")
                null
            } else {
                val input = getCipher()
                input.init(Cipher.ENCRYPT_MODE, publicKey)

                val bytes = input.doFinal(textToEncrypt!!.toByteArray(Charsets.UTF_8))
                Base64.encodeToString(bytes, Base64.DEFAULT)
            }
        } catch (e: Exception) {
            Timber.e(e)
            return null
        }
    }

    fun decryptString(alias: String, textToDecrypt: String?): String? {
        return try {
            val privateKeyEntry = keyStore.getEntry(alias, null) as KeyStore.PrivateKeyEntry
            val privateKey = privateKeyEntry.privateKey

            val output = getCipher()
            output.init(Cipher.DECRYPT_MODE, privateKey)
            val decodeString = Base64.decode(textToDecrypt, Base64.DEFAULT)
            val decodedData = output.doFinal(decodeString)

            val result = String(decodedData, Charsets.UTF_8)
            result
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }

    private fun getCipher(): Cipher {
        try {
            return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { // below android m
                Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL") // error in android 6: InvalidKeyException: Need RSA private or public key
            } else { // android m and above
                Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidKeyStoreBCWorkaround") // error in android 5: NoSuchProviderException: Provider not available: AndroidKeyStoreBCWorkaround
            }
        } catch (exception: Exception) {
            throw RuntimeException("getCipher: Failed to get an instance of Cipher", exception)
        }
    }
}