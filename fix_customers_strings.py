import re

with open("app/src/main/java/com/example/ui/screens/CustomersScreen.kt", "r") as f:
    content = f.read()

content = content.replace('androidx.compose.ui.res.stringResource(com.example.R.string.msg_due, currencySymbol, totalDue.toString()', 'androidx.compose.ui.res.stringResource(com.example.R.string.msg_due, currencySymbol, totalDue.toString())')

with open("app/src/main/java/com/example/ui/screens/CustomersScreen.kt", "w") as f:
    f.write(content)

