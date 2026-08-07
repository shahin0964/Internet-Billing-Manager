import re

with open("app/src/main/java/com/example/ui/screens/MoreScreen.kt", "r") as f:
    content = f.read()

old_block = """                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(androidx.compose.foundation.rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField("""

new_block = """                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(androidx.compose.foundation.rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Logo Management
                            Text(
                                text = androidx.compose.ui.res.stringResource(com.example.R.string.company_logo),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                if (logoUri != null) {
                                    androidx.compose.foundation.layout.Box(
                                        modifier = Modifier.size(64.dp).androidx.compose.foundation.background(MaterialTheme.colorScheme.surfaceVariant, androidx.compose.foundation.shape.CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        coil.compose.AsyncImage(
                                            model = logoUri,
                                            contentDescription = null,
                                            modifier = Modifier.size(64.dp).androidx.compose.ui.draw.clip(androidx.compose.foundation.shape.CircleShape),
                                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                        )
                                    }
                                } else {
                                    androidx.compose.foundation.layout.Box(
                                        modifier = Modifier.size(64.dp).androidx.compose.foundation.background(MaterialTheme.colorScheme.surfaceVariant, androidx.compose.foundation.shape.CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(imageVector = Icons.Default.Business, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                                Column {
                                    Button(
                                        onClick = { imagePickerLauncher.launch(arrayOf("image/*")) },
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(if (logoUri == null) androidx.compose.ui.res.stringResource(com.example.R.string.select_logo) else androidx.compose.ui.res.stringResource(com.example.R.string.change_logo))
                                    }
                                    if (logoUri != null) {
                                        androidx.compose.material3.TextButton(
                                            onClick = { logoUri = null }
                                        ) {
                                            Text(androidx.compose.ui.res.stringResource(com.example.R.string.remove_logo), color = MaterialTheme.colorScheme.error)
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            OutlinedTextField("""

content = content.replace(old_block, new_block)

with open("app/src/main/java/com/example/ui/screens/MoreScreen.kt", "w") as f:
    f.write(content)
