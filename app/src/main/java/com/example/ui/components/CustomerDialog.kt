package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.CustomerEntity
import com.example.data.model.IspPackageEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CustomerDialog(
    initialCustomer: CustomerEntity? = null,
    availablePackages: List<IspPackageEntity>,
    currencySymbol: String,
    onDismiss: () -> Unit,
    onSave: (CustomerEntity) -> Unit
) {
    var name by remember { mutableStateOf(initialCustomer?.name ?: "") }
    var phone by remember { mutableStateOf(initialCustomer?.phone ?: "") }
    var address by remember { mutableStateOf(initialCustomer?.address ?: "") }
    var pppoeUsername by remember { mutableStateOf(initialCustomer?.pppoeUsername ?: "") }
    var ipAddress by remember { mutableStateOf(initialCustomer?.ipAddress ?: "") }
    var notes by remember { mutableStateOf(initialCustomer?.notes ?: "") }
    var status by remember { mutableStateOf(initialCustomer?.status ?: "ACTIVE") }

    var selectedPkg by remember {
        mutableStateOf(
            availablePackages.find { it.id == initialCustomer?.packageId }
                ?: availablePackages.firstOrNull()
        )
    }

    var monthlyFeeStr by remember {
        mutableStateOf(
            initialCustomer?.monthlyFee?.formatAmount()
                ?: selectedPkg?.monthlyPrice?.formatAmount()
                ?: "0"
        )
    }

    var pkgDropdownExpanded by remember { mutableStateOf(false) }

    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val joiningDate = initialCustomer?.joiningDate ?: sdf.format(Date())
    val code = initialCustomer?.customerCode
        ?: "CUST-${System.currentTimeMillis().toString().takeLast(4)}"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (initialCustomer == null) androidx.compose.ui.res.stringResource(com.example.R.string.add_new_customer) else androidx.compose.ui.res.stringResource(com.example.R.string.edit_customer),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(androidx.compose.ui.res.stringResource(com.example.R.string.customer_name_req)) },
                    singleLine = true,
                    
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text(androidx.compose.ui.res.stringResource(com.example.R.string.phone_number_req)) },
                    singleLine = true,
                    
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = pppoeUsername,
                    onValueChange = { pppoeUsername = it },
                    label = { Text(androidx.compose.ui.res.stringResource(com.example.R.string.pppoe_req)) },
                    singleLine = true,
                    
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = ipAddress,
                    onValueChange = { ipAddress = it },
                    label = { Text(androidx.compose.ui.res.stringResource(com.example.R.string.static_ip_opt)) },
                    singleLine = true,
                    placeholder = { Text("192.168.10.100") },
                    
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text(androidx.compose.ui.res.stringResource(com.example.R.string.address_location)) },
                    
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text(androidx.compose.ui.res.stringResource(com.example.R.string.notes_optional)) },
                    
                    modifier = Modifier.fillMaxWidth()
                )

                // Package selection
                Column {
                    Text(
                        text = androidx.compose.ui.res.stringResource(com.example.R.string.internet_package_req),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = selectedPkg?.let { "${it.name} (${it.speedMbps} Mbps)" } ?: androidx.compose.ui.res.stringResource(com.example.R.string.select_package),
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            Text(
                                "▼",
                                modifier = Modifier
                                    .clickable { pkgDropdownExpanded = true }
                                    .padding(8.dp)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { pkgDropdownExpanded = true }
                    )
                    DropdownMenu(
                        expanded = pkgDropdownExpanded,
                        onDismissRequest = { pkgDropdownExpanded = false }
                    ) {
                        availablePackages.forEach { pkg ->
                            DropdownMenuItem(
                                text = { Text("${pkg.name} — $currencySymbol${pkg.monthlyPrice.formatAmount()}/mo") },
                                onClick = {
                                    selectedPkg = pkg
                                    monthlyFeeStr = pkg.monthlyPrice.formatAmount()
                                    pkgDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = monthlyFeeStr,
                    onValueChange = { monthlyFeeStr = it },
                    label = { Text("Monthly Fee ($currencySymbol)") },
                    singleLine = true,
                    
                    modifier = Modifier.fillMaxWidth()
                )

                // Connection Status selection
                Column {
                    Text(
                        text = androidx.compose.ui.res.stringResource(com.example.R.string.connection_status),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        
                    modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf("ACTIVE", "SUSPENDED", "INACTIVE").forEach { st ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { status = st }
                            ) {
                                RadioButton(
                                    selected = (status == st),
                                    onClick = { status = st }
                                )
                                Text(
                                    text = st.lowercase().capitalize(Locale.getDefault()),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val fee = monthlyFeeStr.replace(",", "").trim().toDoubleOrNull() ?: selectedPkg?.monthlyPrice ?: 0.0
                    val pkgName = selectedPkg?.name ?: "Custom Package"
                    val pkgId = selectedPkg?.id ?: 0L

                    val customerToSave = CustomerEntity(
                        id = initialCustomer?.id ?: 0L,
                        customerCode = code,
                        name = name.trim(),
                        phone = phone.trim(),
                        address = address.trim(),
                        pppoeUsername = pppoeUsername.trim(),
                        ipAddress = ipAddress.trim(),
                        packageId = pkgId,
                        packageName = pkgName,
                        monthlyFee = fee,
                        status = status,
                        joiningDate = joiningDate,
                        notes = notes.trim()
                    )
                    onSave(customerToSave)
                },
                enabled = name.isNotBlank() && phone.isNotBlank() && pppoeUsername.isNotBlank()
            ) {
                Text(androidx.compose.ui.res.stringResource(com.example.R.string.save_customer))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(androidx.compose.ui.res.stringResource(com.example.R.string.cancel))
            }
        }
    )
}
