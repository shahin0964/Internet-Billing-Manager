package com.example.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

object AppUpdateConfig {
    // Configured official GitHub repository details
    const val GITHUB_OWNER = "isp-app"
    const val GITHUB_REPO = "isp-billing-app"
}

data class GitHubReleaseInfo(
    val tagName: String,
    val version: String,
    val releaseNotes: String,
    val apkDownloadUrl: String?,
    val isNewer: Boolean
)

object AppUpdateManager {

    /**
     * Get installed app version name safely.
     */
    fun getInstalledVersion(context: Context): String {
        return try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            pInfo.versionName ?: "1.0.0"
        } catch (e: Exception) {
            "1.0.0"
        }
    }

    /**
     * Compare semantic version strings (e.g. "1.1.0" vs "1.0.0").
     */
    fun isVersionNewer(installedVersion: String, latestVersion: String): Boolean {
        val cleanInstalled = installedVersion.trim().removePrefix("v").removePrefix("V")
        val cleanLatest = latestVersion.trim().removePrefix("v").removePrefix("V")

        if (cleanInstalled == cleanLatest) return false

        val installedParts = cleanInstalled.split(".", "-", "+").mapNotNull { it.toIntOrNull() }
        val latestParts = cleanLatest.split(".", "-", "+").mapNotNull { it.toIntOrNull() }

        val maxLen = maxOf(installedParts.size, latestParts.size)
        for (i in 0 until maxLen) {
            val inst = installedParts.getOrElse(i) { 0 }
            val lat = latestParts.getOrElse(i) { 0 }
            if (lat > inst) return true
            if (lat < inst) return false
        }
        return false
    }

    /**
     * Check GitHub API for the latest release.
     */
    suspend fun checkForUpdates(
        context: Context,
        owner: String = AppUpdateConfig.GITHUB_OWNER,
        repo: String = AppUpdateConfig.GITHUB_REPO
    ): Result<GitHubReleaseInfo> = withContext(Dispatchers.IO) {
        try {
            val urlString = "https://api.github.com/repos/$owner/$repo/releases/latest"
            val url = URL(urlString)
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 10000
                readTimeout = 10000
                setRequestProperty("Accept", "application/vnd.github.v3+json")
                setRequestProperty("User-Agent", "Android-ISP-Billing-App")
            }

            val responseCode = connection.responseCode
            if (responseCode != HttpURLConnection.HTTP_OK) {
                return@withContext Result.failure(Exception("HTTP Error: $responseCode"))
            }

            val jsonString = connection.inputStream.bufferedReader().use { it.readText() }
            val json = JSONObject(jsonString)

            val tagName = json.optString("tag_name", "")
            val cleanVersion = tagName.removePrefix("v").removePrefix("V")
            val releaseNotes = json.optString("body", "")

            var apkUrl: String? = null
            if (json.has("assets")) {
                val assets = json.getJSONArray("assets")
                for (i in 0 until assets.length()) {
                    val asset = assets.getJSONObject(i)
                    val assetName = asset.optString("name", "")
                    val downloadUrl = asset.optString("browser_download_url", "")
                    if (assetName.endsWith(".apk", ignoreCase = true)) {
                        apkUrl = downloadUrl
                        break
                    }
                }
                // Fallback to first asset if no explicit .apk extension match
                if (apkUrl == null && assets.length() > 0) {
                    apkUrl = assets.getJSONObject(0).optString("browser_download_url", null)
                }
            }

            val installedVersion = getInstalledVersion(context)
            val isNewer = isVersionNewer(installedVersion, cleanVersion)

            Result.success(
                GitHubReleaseInfo(
                    tagName = tagName,
                    version = cleanVersion,
                    releaseNotes = releaseNotes,
                    apkDownloadUrl = apkUrl,
                    isNewer = isNewer
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Download the APK file from the release asset URL.
     */
    suspend fun downloadApk(
        context: Context,
        downloadUrl: String,
        onProgress: (Int) -> Unit
    ): Result<File> = withContext(Dispatchers.IO) {
        try {
            val url = URL(downloadUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 15000
            connection.readTimeout = 30000
            connection.connect()

            val fileLength = connection.contentLength
            val downloadDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) ?: context.cacheDir
            val outputFile = File(downloadDir, "app-update.apk")

            if (outputFile.exists()) {
                outputFile.delete()
            }

            connection.inputStream.use { input ->
                FileOutputStream(outputFile).use { output ->
                    val data = ByteArray(4096)
                    var total: Long = 0
                    var count: Int
                    while (input.read(data).also { count = it } != -1) {
                        total += count
                        if (fileLength > 0) {
                            val progress = ((total * 100) / fileLength).toInt()
                            withContext(Dispatchers.Main) {
                                onProgress(progress)
                            }
                        }
                        output.write(data, 0, count)
                    }
                    output.flush()
                }
            }

            Result.success(outputFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Trigger Android package installer for downloaded APK.
     */
    fun installApk(context: Context, apkFile: File) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            val apkUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    apkFile
                )
            } else {
                Uri.fromFile(apkFile)
            }

            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
