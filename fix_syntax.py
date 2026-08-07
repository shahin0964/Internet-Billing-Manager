import re

with open("app/src/main/java/com/example/ui/screens/CustomersScreen.kt", "r") as f:
    content = f.read()

# Fix CustomersScreen Text expression
content = content.replace('text = "Fee: $currencySymbol${customer.monthlyFee}/mo" +\n                                    if (totalDue > 0) androidx.compose.ui.res.stringResource(com.example.R.string.msg_due, currencySymbol, totalDue.toString()) else "",', 'text = "Fee: $currencySymbol${customer.monthlyFee}/mo" + (if (totalDue > 0) androidx.compose.ui.res.stringResource(com.example.R.string.msg_due, currencySymbol, totalDue.toString()) else ""),')

with open("app/src/main/java/com/example/ui/screens/CustomersScreen.kt", "w") as f:
    f.write(content)

