import re

with open("app/src/main/java/com/example/ui/screens/DashboardScreen.kt", "r") as f:
    content = f.read()

content = content.replace('androidx.compose.ui.res.stringResource(com.example.R.string.msg_total_generated, currency, totalBillingAmount.toString()', 'androidx.compose.ui.res.stringResource(com.example.R.string.msg_total_generated, currency, totalBillingAmount.toString())')
content = content.replace('androidx.compose.ui.res.stringResource(com.example.R.string.msg_collected, currency, totalCollectedAmount.toString()', 'androidx.compose.ui.res.stringResource(com.example.R.string.msg_collected, currency, totalCollectedAmount.toString())')
content = content.replace('androidx.compose.ui.res.stringResource(com.example.R.string.msg_outstanding, currency, totalDueAmount.toString()', 'androidx.compose.ui.res.stringResource(com.example.R.string.msg_outstanding, currency, totalDueAmount.toString())')

with open("app/src/main/java/com/example/ui/screens/DashboardScreen.kt", "w") as f:
    f.write(content)

