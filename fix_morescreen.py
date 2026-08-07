import re
with open("app/src/main/java/com/example/ui/screens/MoreScreen.kt", "r") as f:
    content = f.read()

# Fix broken TextButton / RadioButton
content = re.sub(r'Textval msgDbCopied = androidx.compose.ui.res.stringResource\(com.example.R.string.msg_db_copied\)', 'TextButton(', content)
content = re.sub(r'Radioval msgDbCopied = androidx.compose.ui.res.stringResource\(com.example.R.string.msg_db_copied\)', 'RadioButton(', content)
content = re.sub(r'val msgDbCopied = androidx.compose.ui.res.stringResource\(com.example.R.string.msg_db_copied\)\s*Button\(', 'Button(', content)

# Remove any remaining val msgDbCopied = ...
content = re.sub(r'val msgDbCopied = androidx.compose.ui.res.stringResource\(com.example.R.string.msg_db_copied\)', '', content)

# Inject ONE msgDbCopied at the start of MoreScreen
content = content.replace("fun MoreScreen(", "fun MoreScreen(\n")
content = re.sub(r'(fun MoreScreen\([^)]*\)\s*\{)', r'\1\n    val msgDbCopied = androidx.compose.ui.res.stringResource(com.example.R.string.msg_db_copied)\n', content)

with open("app/src/main/java/com/example/ui/screens/MoreScreen.kt", "w") as f:
    f.write(content)
