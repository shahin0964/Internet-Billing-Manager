import re
with open("app/src/main/java/com/example/ui/screens/MoreScreen.kt", "r") as f:
    content = f.read()

# I will find "val context = LocalContext.current" and inject msgDbCopied there
content = content.replace("val context = LocalContext.current", "val context = LocalContext.current\n    val msgDbCopied = androidx.compose.ui.res.stringResource(com.example.R.string.msg_db_copied)")

with open("app/src/main/java/com/example/ui/screens/MoreScreen.kt", "w") as f:
    f.write(content)
