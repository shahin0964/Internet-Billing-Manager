import re

with open("app/src/main/res/values-bn/strings.xml", "r") as f:
    content = f.read()

# Fix duplicates in strings.xml
content = re.sub(r'<string name="msg_outstanding_balance">.*?</string>\s*<string name="msg_outstanding_balance">.*?</string>', r'<string name="msg_outstanding_balance">বকেয়া ব্যালেন্স</string>', content, flags=re.DOTALL)

with open("app/src/main/res/values-bn/strings.xml", "w") as f:
    f.write(content)

with open("app/src/main/res/values/strings.xml", "r") as f:
    content = f.read()

content = re.sub(r'<string name="msg_outstanding_balance">.*?</string>\s*<string name="msg_outstanding_balance">.*?</string>', r'<string name="msg_outstanding_balance">Outstanding Balance</string>', content, flags=re.DOTALL)

with open("app/src/main/res/values/strings.xml", "w") as f:
    f.write(content)

# Fix DashboardScreen.kt Syntax Errors (extra closing parenthesis or missing closing parenthesis)
with open("app/src/main/java/com/example/ui/screens/DashboardScreen.kt", "r") as f:
    content = f.read()

content = content.replace('androidx.compose.ui.res.stringResource(com.example.R.string.msg_total_generated, currency, totalBillingAmount.toString()))', 'androidx.compose.ui.res.stringResource(com.example.R.string.msg_total_generated, currency, totalBillingAmount.toString())')
content = content.replace('androidx.compose.ui.res.stringResource(com.example.R.string.msg_collected, currency, totalCollectedAmount.toString()))', 'androidx.compose.ui.res.stringResource(com.example.R.string.msg_collected, currency, totalCollectedAmount.toString())')
content = content.replace('androidx.compose.ui.res.stringResource(com.example.R.string.msg_outstanding, currency, totalDueAmount.toString()))', 'androidx.compose.ui.res.stringResource(com.example.R.string.msg_outstanding, currency, totalDueAmount.toString())')

with open("app/src/main/java/com/example/ui/screens/DashboardScreen.kt", "w") as f:
    f.write(content)
