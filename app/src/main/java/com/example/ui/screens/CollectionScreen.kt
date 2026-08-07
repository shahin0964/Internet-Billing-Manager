package com.example.ui.screens

import com.example.ui.components.formatAmount
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BillEntity
import com.example.data.model.PaymentEntity
import com.example.ui.components.CustomSearchBar
import com.example.ui.components.EmptyStateView
import com.example.ui.components.KpiCard
import com.example.ui.components.SectionHeader
import com.example.ui.theme.EmeraldSuccess
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun CollectionScreen(
    payments: List<PaymentEntity>,
    bills: List<BillEntity>,
    currencySymbol: String,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onCollectPaymentClick: () -> Unit
) {
    val sdfMonth = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    var monthOffset by remember { mutableIntStateOf(0) }
    
    val selectedMonthString = remember(monthOffset) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, monthOffset)
        sdfMonth.format(calendar.time)
    }

    val monthlyBills = remember(bills, selectedMonthString) {
        bills.filter { it.billingMonth.equals(selectedMonthString, ignoreCase = true) }
    }
    
    val monthlyBillIds = remember(monthlyBills) {
        monthlyBills.map { it.id }.toSet()
    }

    val totalMonthlyBill = monthlyBills.sumOf { it.amount }
    val totalMonthlyCollected = payments.filter { it.billId in monthlyBillIds }.sumOf { it.amount }
    val totalMonthlyDue = monthlyBills.sumOf { it.dueAmount }

    val totalOutstanding = bills.sumOf { it.dueAmount }

    val filteredPayments = remember(payments, searchQuery) {
        if (searchQuery.isBlank()) payments
        else payments.filter {
            it.customerName.contains(searchQuery, ignoreCase = true) ||
                    it.paymentReceiptNo.contains(searchQuery, ignoreCase = true) ||
                    it.paymentMethod.contains(searchQuery, ignoreCase = true)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        // Monthly Summary Selection
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { monthOffset -= 1 }) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = androidx.compose.ui.res.stringResource(com.example.R.string.previous_month))
                }
                Text(
                    text = selectedMonthString,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = { monthOffset += 1 }) {
                    Icon(Icons.Default.ChevronRight, contentDescription = androidx.compose.ui.res.stringResource(com.example.R.string.next_month))
                }
            }
        }

        // Monthly Summary Details
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                KpiCard(
                    title = androidx.compose.ui.res.stringResource(com.example.R.string.total_bill),
                    value = "$currencySymbol${totalMonthlyBill.formatAmount()}",
                    icon = Icons.Default.Receipt,
                    iconColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                KpiCard(
                    title = androidx.compose.ui.res.stringResource(com.example.R.string.total_collected),
                    value = "$currencySymbol${totalMonthlyCollected.formatAmount()}",
                    icon = Icons.Default.Payments,
                    iconColor = EmeraldSuccess,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            KpiCard(
                title = androidx.compose.ui.res.stringResource(com.example.R.string.total_due),
                value = "$currencySymbol${totalMonthlyDue.formatAmount()}",
                icon = Icons.Default.MoneyOff,
                iconColor = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Action banner
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
shadowElevation = 3.dp,
tonalElevation = 2.dp,
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .padding(14.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.record_customer_payment),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Outstanding Balance: $currencySymbol${totalOutstanding.formatAmount()}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Button(
                        onClick = onCollectPaymentClick,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EmeraldSuccess
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.CreditCard,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(androidx.compose.ui.res.stringResource(com.example.R.string.collect), fontSize = 12.sp)
                    }
                }
            }
        }

        // Search Bar
        item {
            CustomSearchBar(
                query = searchQuery,
                onQueryChange = onSearchQueryChange,
                placeholder = androidx.compose.ui.res.stringResource(com.example.R.string.search_payments_hint)
            )
        }

        item {
            SectionHeader(title = androidx.compose.ui.res.stringResource(com.example.R.string.payment_receipts_history))
        }

        if (filteredPayments.isEmpty()) {
            item {
                EmptyStateView(
                    title = if (searchQuery.isNotEmpty()) androidx.compose.ui.res.stringResource(com.example.R.string.no_matching_receipts) else androidx.compose.ui.res.stringResource(com.example.R.string.no_payment_receipts),
                    description = androidx.compose.ui.res.stringResource(com.example.R.string.payment_receipts_desc),
                    icon = Icons.Default.Receipt,
                    actionButton = {
                        Button(onClick = onCollectPaymentClick) {
                            Text(androidx.compose.ui.res.stringResource(com.example.R.string.record_first_payment))
                        }
                    }
                )
            }
        } else {
            items(filteredPayments, key = { it.id }) { payment ->
                PaymentReceiptCard(
                    payment = payment,
                    currencySymbol = currencySymbol
                )
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
fun PaymentReceiptCard(
    payment: PaymentEntity,
    currencySymbol: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
shadowElevation = 3.dp,
tonalElevation = 2.dp,
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = payment.customerName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Receipt: ${payment.paymentReceiptNo} • Date: ${payment.paymentDate}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (payment.notes.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Ref: ${payment.notes}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "+$currencySymbol${payment.amount.formatAmount()}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = EmeraldSuccess,
                        fontSize = 18.sp
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
shadowElevation = 3.dp,
tonalElevation = 2.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = payment.paymentMethod,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
