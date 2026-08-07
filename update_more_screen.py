with open("app/src/main/java/com/example/ui/screens/MoreScreen.kt", "r") as f:
    content = f.read()

language_section = """
        // Language Preference
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
                    SectionHeader(title = androidx.compose.ui.res.stringResource(com.example.R.string.language))
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    val sharedPrefs = context.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
                    var currentLang by remember { androidx.compose.runtime.mutableStateOf(sharedPrefs.getString("app_lang", "en") ?: "en") }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = currentLang == "en",
                            onClick = { 
                                sharedPrefs.edit().putString("app_lang", "en").apply()
                                currentLang = "en"
                                (context as? android.app.Activity)?.recreate()
                            },
                            label = { Text("English") }
                        )
                        FilterChip(
                            selected = currentLang == "bn",
                            onClick = { 
                                sharedPrefs.edit().putString("app_lang", "bn").apply()
                                currentLang = "bn"
                                (context as? android.app.Activity)?.recreate()
                            },
                            label = { Text("বাংলা") }
                        )
                    }
                }
            }
        }
"""

content = content.replace("        // Theme Preference", language_section + "\n        // Theme Preference")

with open("app/src/main/java/com/example/ui/screens/MoreScreen.kt", "w") as f:
    f.write(content)
