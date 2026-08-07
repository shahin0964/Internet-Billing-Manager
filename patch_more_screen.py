import re

with open('app/src/main/java/com/example/ui/screens/MoreScreen.kt', 'r') as f:
    content = f.read()

target = """        // ISP Business Settings Card
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SectionHeader(
                        title = androidx.compose.ui.res.stringResource(com.example.R.string.isp_business_info),
                        subtitle = androidx.compose.ui.res.stringResource(com.example.R.string.configure_noc_desc)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = ispName,
                        onValueChange = { ispName = it },
                        label = { Text(androidx.compose.ui.res.stringResource(com.example.R.string.isp_name_brand)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = hotline,
                            onValueChange = { hotline = it },
                            label = { Text(androidx.compose.ui.res.stringResource(com.example.R.string.support_hotline)) },
                            singleLine = true,
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = currencySymbol,
                            onValueChange = { currencySymbol = it },
                            label = { Text(androidx.compose.ui.res.stringResource(com.example.R.string.currency_symbol)) },
                            singleLine = true,
                            modifier = Modifier.width(90.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text(androidx.compose.ui.res.stringResource(com.example.R.string.office_address)) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = androidx.compose.ui.res.stringResource(com.example.R.string.network_status),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(androidx.compose.ui.res.stringResource(com.example.R.string.operational), androidx.compose.ui.res.stringResource(com.example.R.string.maintenance), androidx.compose.ui.res.stringResource(com.example.R.string.degraded)).forEach { status ->
                            FilterChip(
                                selected = (networkStatus == status),
                                onClick = { networkStatus = status },
                                label = { Text(status) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            val updated = settings.copy(
                                ispName = ispName.trim(),
                                hotline = hotline.trim(),
                                address = address.trim(),
                                currencySymbol = currencySymbol.trim(),
                                networkStatus = networkStatus,
                                themeMode = themeMode
                            )
                            onUpdateSettings(updated)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(androidx.compose.ui.res.stringResource(com.example.R.string.save_business_info))
                    }
                }
            }
        }"""

replacement = """        // ISP Business Settings Card
        item {
            var showBusinessInfoDialog by remember { mutableStateOf(false) }
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showBusinessInfoDialog = true },
                shape = RoundedCornerShape(18.dp),
                color = MaterialTheme.colorScheme.surface,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SectionHeader(
                        title = androidx.compose.ui.res.stringResource(com.example.R.string.isp_business_info),
                        subtitle = androidx.compose.ui.res.stringResource(com.example.R.string.configure_noc_desc)
                    )
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if (showBusinessInfoDialog) {
                androidx.compose.material3.AlertDialog(
                    onDismissRequest = { showBusinessInfoDialog = false },
                    title = {
                        Text(
                            text = androidx.compose.ui.res.stringResource(com.example.R.string.isp_business_info),
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    text = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .androidx.compose.foundation.verticalScroll(androidx.compose.foundation.rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = ispName,
                                onValueChange = { ispName = it },
                                label = { Text(androidx.compose.ui.res.stringResource(com.example.R.string.isp_name_brand)) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = hotline,
                                    onValueChange = { hotline = it },
                                    label = { Text(androidx.compose.ui.res.stringResource(com.example.R.string.support_hotline)) },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f)
                                )

                                OutlinedTextField(
                                    value = currencySymbol,
                                    onValueChange = { currencySymbol = it },
                                    label = { Text(androidx.compose.ui.res.stringResource(com.example.R.string.currency_symbol)) },
                                    singleLine = true,
                                    modifier = Modifier.width(90.dp)
                                )
                            }

                            OutlinedTextField(
                                value = address,
                                onValueChange = { address = it },
                                label = { Text(androidx.compose.ui.res.stringResource(com.example.R.string.office_address)) },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Text(
                                text = androidx.compose.ui.res.stringResource(com.example.R.string.network_status),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf(androidx.compose.ui.res.stringResource(com.example.R.string.operational), androidx.compose.ui.res.stringResource(com.example.R.string.maintenance), androidx.compose.ui.res.stringResource(com.example.R.string.degraded)).forEach { status ->
                                    FilterChip(
                                        selected = (networkStatus == status),
                                        onClick = { networkStatus = status },
                                        label = { Text(status) }
                                    )
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                val updated = settings.copy(
                                    ispName = ispName.trim(),
                                    hotline = hotline.trim(),
                                    address = address.trim(),
                                    currencySymbol = currencySymbol.trim(),
                                    networkStatus = networkStatus,
                                    themeMode = themeMode
                                )
                                onUpdateSettings(updated)
                                showBusinessInfoDialog = false
                            }
                        ) {
                            Text(androidx.compose.ui.res.stringResource(com.example.R.string.save_business_info))
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showBusinessInfoDialog = false }) {
                            Text(androidx.compose.ui.res.stringResource(com.example.R.string.cancel))
                        }
                    }
                )
            }
        }"""

if target in content:
    new_content = content.replace(target, replacement)
    with open('app/src/main/java/com/example/ui/screens/MoreScreen.kt', 'w') as f:
        f.write(new_content)
    print("Success")
else:
    print("Target not found. Let's find what is actually there.")
    
