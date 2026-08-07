package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.BillEntity

@Composable
fun PaymentDialog(
    unpaidBills: List<BillEntity>,
    preSelectedBill: BillEntity? = null,
    currencySymbol: String,
    onDismiss: () -> Unit,
    onRecordPayment: (billId: Long, customerId: Long, amount: Double, method: String, notes: String) -> Unit
) {
    var selectedBill by remember { mutableStateOf(preSelectedBill ?: unpaidBills.firstOrNull()) }
    var billDropdownExpanded by remember { mutableStateOf(false) }

    var amountStr by remember { mutableStateOf(selectedBill?.dueAmount?.formatAmount() ?: "0") }
    val defaultPaymentMethod = androidx.compose.ui.res.stringResource(com.example.R.string.cash)
    var paymentMethod by remember { mutableStateOf(defaultPaymentMethod) }
    var notes by remember { mutableStateOf("") }

    val methods = listOf(androidx.compose.ui.res.stringResource(com.example.R.string.cash), androidx.compose.ui.res.stringResource(com.example.R.string.bkash), androidx.compose.ui.res.stringResource(com.example.R.string.card), androidx.compose.ui.res.stringResource(com.example.R.string.bank_transfer), androidx.compose.ui.res.stringResource(com.example.R.string.online))

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = androidx.compose.ui.res.stringResource(com.example.R.string.collect_payment),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Bill selection
                Column {
                    Text(
                        text = androidx.compose.ui.res.stringResource(com.example.R.string.select_bill_req),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = selectedBill?.let { "${it.customerName} (${it.billingMonth}) — Due: $currencySymbol${it.dueAmount.formatAmount()}" }
                            ?: androidx.compose.ui.res.stringResource(com.example.R.string.no_unpaid_bill_selected),
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { Text("▼") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { if (unpaidBills.isNotEmpty()) billDropdownExpanded = true }
                    )
                    DropdownMenu(
                        expanded = billDropdownExpanded,
                        onDismissRequest = { billDropdownExpanded = false }
                    ) {
                        unpaidBills.forEach { bill ->
                            DropdownMenuItem(
                                text = { Text("${bill.customerName} — ${bill.billingMonth} — Due: $currencySymbol${bill.dueAmount.formatAmount()}") },
                                onClick = {
                                    selectedBill = bill
                                    amountStr = bill.dueAmount.formatAmount()
                                    billDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Amount input
                OutlinedTextField(
                    value = amountStr,
                    onValueChange = { amountStr = it },
                    label = { Text(androidx.compose.ui.res.stringResource(com.example.R.string.payment_amount_req)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Quick amount chips
                selectedBill?.let { bill ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = amountStr == bill.dueAmount.formatAmount(),
                            onClick = { amountStr = bill.dueAmount.formatAmount() },
                            label = { Text("Full ($currencySymbol${bill.dueAmount.formatAmount()})") }
                        )
                        val half = (bill.dueAmount / 2.0)
                        if (half > 0) {
                            FilterChip(
                                selected = amountStr == half.formatAmount(),
                                onClick = { amountStr = half.formatAmount() },
                                label = { Text(androidx.compose.ui.res.stringResource(com.example.R.string.msg_half, currencySymbol, half.formatAmount())) }
                            )
                        }
                    }
                }

                // Payment Method
                Column {
                    Text(
                        text = androidx.compose.ui.res.stringResource(com.example.R.string.payment_method),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        methods.take(3).forEach { m ->
                            FilterChip(
                                selected = (paymentMethod == m),
                                onClick = { paymentMethod = m },
                                label = { Text(m) }
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        methods.drop(3).forEach { m ->
                            FilterChip(
                                selected = (paymentMethod == m),
                                onClick = { paymentMethod = m },
                                label = { Text(m) }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text(androidx.compose.ui.res.stringResource(com.example.R.string.receipt_notes)) },
                    placeholder = { Text(androidx.compose.ui.res.stringResource(com.example.R.string.eg_bkash_trx)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val bill = selectedBill ?: return@Button
                    val amt = amountStr.replace(",", "").trim().toDoubleOrNull() ?: 0.0
                    if (amt > 0) {
                        onRecordPayment(bill.id, bill.customerId, amt, paymentMethod, notes)
                    }
                },
                enabled = selectedBill != null && (amountStr.replace(",", "").trim().toDoubleOrNull() ?: 0.0) > 0
            ) {
                Text(androidx.compose.ui.res.stringResource(com.example.R.string.confirm_payment))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(androidx.compose.ui.res.stringResource(com.example.R.string.cancel))
            }
        }
    )
}
