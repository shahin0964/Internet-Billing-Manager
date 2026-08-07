import re

with open("app/src/main/java/com/example/ui/screens/MoreScreen.kt", "r") as f:
    content = f.read()

old_block = """    var networkStatus by remember(settings) { mutableStateOf(settings.networkStatus) }
    var themeMode by remember(settings) { mutableStateOf(settings.themeMode) }"""

new_block = """    var networkStatus by remember(settings) { mutableStateOf(settings.networkStatus) }
    var themeMode by remember(settings) { mutableStateOf(settings.themeMode) }
    var logoUri by remember(settings) { mutableStateOf(settings.logoUri) }

    val imagePickerLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                logoUri = uri.toString()
            } catch (e: Exception) {
                // Ignore
                logoUri = uri.toString()
            }
        }
    }"""

content = content.replace(old_block, new_block)

with open("app/src/main/java/com/example/ui/screens/MoreScreen.kt", "w") as f:
    f.write(content)
