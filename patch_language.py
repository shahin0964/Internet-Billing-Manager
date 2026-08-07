import re

with open("app/src/main/java/com/example/ui/screens/MoreScreen.kt", "r") as f:
    content = f.read()

# We can match from `// Language Preference\n        item {` up to the next `// Theme Preference`
start_marker = "// Language Preference"
end_marker = "// Theme Preference"

start_idx = content.find(start_marker)
end_idx = content.find(end_marker)

if start_idx != -1 and end_idx != -1:
    new_lang_block = """// Language Preference
        item {
            var showLanguageDialog by remember { mutableStateOf(false) }
            val sharedPrefs = context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
            var currentLang by remember { androidx.compose.runtime.mutableStateOf(sharedPrefs.getString("app_lang", "en") ?: "en") }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showLanguageDialog = true },
                shape = RoundedCornerShape(18.dp),
                shadowElevation = 6.dp,
                tonalElevation = 3.dp,
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
                        title = androidx.compose.ui.res.stringResource(com.example.R.string.language),
                        subtitle = if (currentLang == "bn") "বাংলা" else "English"
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Language,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            if (showLanguageDialog) {
                androidx.compose.material3.AlertDialog(
                    onDismissRequest = { showLanguageDialog = false },
                    title = { Text(androidx.compose.ui.res.stringResource(com.example.R.string.language)) },
                    text = {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        sharedPrefs.edit().putString("app_lang", "en").apply()
                                        currentLang = "en"
                                        showLanguageDialog = false
                                        (context as? android.app.Activity)?.recreate()
                                    }
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = currentLang == "en",
                                    onClick = {
                                        sharedPrefs.edit().putString("app_lang", "en").apply()
                                        currentLang = "en"
                                        showLanguageDialog = false
                                        (context as? android.app.Activity)?.recreate()
                                    }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("English")
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        sharedPrefs.edit().putString("app_lang", "bn").apply()
                                        currentLang = "bn"
                                        showLanguageDialog = false
                                        (context as? android.app.Activity)?.recreate()
                                    }
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = currentLang == "bn",
                                    onClick = {
                                        sharedPrefs.edit().putString("app_lang", "bn").apply()
                                        currentLang = "bn"
                                        showLanguageDialog = false
                                        (context as? android.app.Activity)?.recreate()
                                    }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("বাংলা")
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { showLanguageDialog = false }) {
                            Text(androidx.compose.ui.res.stringResource(com.example.R.string.cancel))
                        }
                    }
                )
            }
        }
        
        """
    content = content[:start_idx] + new_lang_block + content[end_idx:]
    
    # ensure Icons.Default.Language is imported or use explicit import
    if "import androidx.compose.material.icons.filled.Language" not in content:
        content = content.replace("import androidx.compose.material.icons.filled.Add", "import androidx.compose.material.icons.filled.Add\nimport androidx.compose.material.icons.filled.Language")
        
    with open("app/src/main/java/com/example/ui/screens/MoreScreen.kt", "w") as f:
        f.write(content)
    print("Patched successfully")
else:
    print("Not found")

