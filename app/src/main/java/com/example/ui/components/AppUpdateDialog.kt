package com.example.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.util.AppUpdateManager
import com.example.util.GitHubReleaseInfo
import com.example.util.UpdateException
import kotlinx.coroutines.launch
import java.io.File

private enum class UpdateState {
    CHECKING,
    NEW_AVAILABLE,
    LATEST,
    ERROR,
    DOWNLOADING,
    READY_TO_INSTALL
}

@Composable
fun AppUpdateDialog(
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var state by remember { mutableStateOf(UpdateState.CHECKING) }
    var releaseInfo by remember { mutableStateOf<GitHubReleaseInfo?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var downloadProgress by remember { mutableFloatStateOf(0f) }
    var downloadedFile by remember { mutableStateOf<File?>(null) }
    var showFullReleaseNotesDialog by remember { mutableStateOf(false) }

    val installedVersion = remember { AppUpdateManager.getInstalledVersion(context) }
    val rawReleaseNotes = releaseInfo?.releaseNotes ?: ""
    val summaryBullets = remember(rawReleaseNotes) {
        parseSummaryItems(rawReleaseNotes)
    }

    fun checkUpdates() {
        state = UpdateState.CHECKING
        errorMessage = null
        scope.launch {
            val result = AppUpdateManager.checkForUpdates(context)
            result.onSuccess { info ->
                releaseInfo = info
                state = if (info.isNewer) UpdateState.NEW_AVAILABLE else UpdateState.LATEST
            }.onFailure { err ->
                errorMessage = when (err) {
                    is UpdateException.NoInternet -> context.getString(R.string.update_error_no_internet)
                    is UpdateException.ReleaseNotFound -> context.getString(R.string.update_error_no_release)
                    is UpdateException.ApkAssetNotFound -> context.getString(R.string.update_error_no_apk)
                    is UpdateException.HttpError -> context.getString(R.string.update_error_server, "HTTP ${err.httpCode}")
                    is UpdateException.ServerError -> context.getString(R.string.update_error_server, "HTTP ${err.httpCode}")
                    is UpdateException.RateLimited -> context.getString(R.string.update_error_server, "Rate Limit Exceeded")
                    is UpdateException.Timeout, is UpdateException.ConnectionFailed -> context.getString(R.string.update_error_server, "Connection Timeout")
                    is UpdateException.SslError -> context.getString(R.string.update_error_server, "SSL Error")
                    is UpdateException.InvalidJson -> context.getString(R.string.update_error_server, "Invalid Response")
                    else -> err.localizedMessage ?: context.getString(R.string.unable_to_check_updates)
                }
                state = UpdateState.ERROR
            }
        }
    }

    LaunchedEffect(Unit) {
        checkUpdates()
    }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SystemUpdate,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = stringResource(R.string.app_update),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (state) {
                    UpdateState.CHECKING -> {
                        Spacer(modifier = Modifier.height(12.dp))
                        CircularProgressIndicator(modifier = Modifier.size(40.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.checking_updates),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    UpdateState.NEW_AVAILABLE -> {
                        Icon(
                            imageVector = Icons.Default.CloudDownload,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = stringResource(R.string.new_update_available),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = stringResource(R.string.current_version),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = installedVersion,
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = stringResource(R.string.latest_version),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = releaseInfo?.version ?: "",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        if (summaryBullets.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = stringResource(R.string.whats_new),
                                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                summaryBullets.forEach { bullet ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 2.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Text(
                                            text = "• ",
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = bullet,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                if (rawReleaseNotes.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    TextButton(
                                        onClick = { showFullReleaseNotesDialog = true },
                                        modifier = Modifier.align(Alignment.End)
                                    ) {
                                        Text(
                                            text = stringResource(R.string.view_details),
                                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        } else if (rawReleaseNotes.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(
                                onClick = { showFullReleaseNotesDialog = true },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text(
                                    text = stringResource(R.string.view_details),
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    UpdateState.LATEST -> {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = stringResource(R.string.latest_version_message),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${stringResource(R.string.current_version)}: $installedVersion",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    UpdateState.ERROR -> {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = errorMessage ?: stringResource(R.string.unable_to_check_updates),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    UpdateState.DOWNLOADING -> {
                        Text(
                            text = stringResource(R.string.downloading_update),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        LinearProgressIndicator(
                            progress = { downloadProgress },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${(downloadProgress * 100).toInt()}%",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    UpdateState.READY_TO_INSTALL -> {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = stringResource(R.string.download_complete),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }
        },
        confirmButton = {
            when (state) {
                UpdateState.NEW_AVAILABLE -> {
                    Button(
                        onClick = {
                            val apkUrl = releaseInfo?.apkDownloadUrl
                            if (apkUrl.isNullOrEmpty()) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.unable_to_check_updates),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                state = UpdateState.DOWNLOADING
                                scope.launch {
                                    // Automatic safety backup of all customer/billing data before APK update
                                    AppUpdateManager.createPreUpdateSafetyBackup(context)

                                    val downloadResult = AppUpdateManager.downloadApk(
                                        context = context,
                                        downloadUrl = apkUrl,
                                        onProgress = { pct ->
                                            downloadProgress = pct / 100f
                                        }
                                    )
                                    downloadResult.onSuccess { file ->
                                        downloadedFile = file
                                        state = UpdateState.READY_TO_INSTALL
                                        AppUpdateManager.installApk(context, file)
                                    }.onFailure { err ->
                                        errorMessage = context.getString(R.string.unable_to_check_updates)
                                        state = UpdateState.ERROR
                                    }
                                }
                            }
                        },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(stringResource(R.string.update))
                    }
                }

                UpdateState.READY_TO_INSTALL -> {
                    Button(
                        onClick = {
                            downloadedFile?.let { file ->
                                AppUpdateManager.installApk(context, file)
                            }
                        },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(stringResource(R.string.install))
                    }
                }

                UpdateState.ERROR -> {
                    Button(
                        onClick = { checkUpdates() },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(stringResource(R.string.check_for_update))
                    }
                }

                else -> {}
            }
        },
        dismissButton = {
            if (state != UpdateState.DOWNLOADING) {
                TextButton(onClick = onDismissRequest) {
                    Text(stringResource(R.string.cancel))
                }
            }
        }
    )

    if (showFullReleaseNotesDialog) {
        AlertDialog(
            onDismissRequest = { showFullReleaseNotesDialog = false },
            shape = RoundedCornerShape(16.dp),
            title = {
                Text(
                    text = stringResource(R.string.release_notes_title),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            text = {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 320.dp)
                        .verticalScroll(scrollState)
                ) {
                    Text(
                        text = rawReleaseNotes,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showFullReleaseNotesDialog = false }) {
                    Text(stringResource(R.string.close))
                }
            }
        )
    }
}

private fun parseSummaryItems(releaseNotes: String): List<String> {
    if (releaseNotes.isBlank()) return emptyList()
    return releaseNotes.lines()
        .map { it.trim() }
        .filter { line ->
            line.isNotBlank() &&
            !line.startsWith("#") &&
            !line.contains("Full Changelog", ignoreCase = true) &&
            !line.startsWith("http://", ignoreCase = true) &&
            !line.startsWith("https://", ignoreCase = true)
        }
        .map { line ->
            line.removePrefix("*")
                .removePrefix("-")
                .removePrefix("•")
                .removePrefix("1.")
                .removePrefix("2.")
                .removePrefix("3.")
                .trim()
        }
        .filter { it.isNotBlank() }
        .take(3)
}
