package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun BillGenerateDialog(
    activeCustomerCount: Int,
    onDismiss: () -> Unit,
    onGenerate: (billingMonth: String, dueDate: String) -> Unit
) {
    val sdfMonth = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    val sdfDate = SimpleDateFormat("yyyy-MM-10", Locale.getDefault())

    var billingMonth by remember { mutableStateOf(sdfMonth.format(Date())) }
    var dueDate by remember { mutableStateOf(sdfDate.format(Date())) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = androidx.compose.ui.res.stringResource(com.example.R.string.generate_monthly_bills),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = androidx.compose.ui.res.stringResource(com.example.R.string.msg_generate_desc, activeCustomerCount),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = billingMonth,
                    onValueChange = { billingMonth = it },
                    label = { Text(androidx.compose.ui.res.stringResource(com.example.R.string.billing_month)) },
                    placeholder = { Text(androidx.compose.ui.res.stringResource(com.example.R.string.eg_august_2026)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = { Text(androidx.compose.ui.res.stringResource(com.example.R.string.payment_due_date)) },
                    placeholder = { Text("2026-08-10") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onGenerate(billingMonth.trim(), dueDate.trim())
                },
                enabled = billingMonth.isNotBlank() && activeCustomerCount > 0
            ) {
                Text(androidx.compose.ui.res.stringResource(com.example.R.string.msg_generate_customers, activeCustomerCount))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(androidx.compose.ui.res.stringResource(com.example.R.string.cancel))
            }
        }
    )
}
