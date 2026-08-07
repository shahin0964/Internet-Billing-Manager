import re

with open("app/src/main/java/com/example/ui/screens/DashboardScreen.kt", "r") as f:
    content = f.read()

# Replace inactiveCount with monthlyBillAmount calculation
old_calc = """    val inactiveCount = customers.count { it.status == "INACTIVE" || it.status == "SUSPENDED" }"""
new_calc = """    val currentMonthStr = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date())
    val monthlyBillAmount = bills.filter { it.billingMonth.equals(currentMonthStr, ignoreCase = true) }.sumOf { it.amount }"""
content = content.replace(old_calc, new_calc)

# Replace the card values
old_card = """                    KpiCard(
                        title = androidx.compose.ui.res.stringResource(com.example.R.string.inactive_susp),
                        value = "$inactiveCount",
                        icon = Icons.Default.Warning,
                        iconColor = AmberWarning,
                        modifier = Modifier.weight(1f),
                        subtitle = "Needs follow up"
                    )"""

new_card = """                    KpiCard(
                        title = androidx.compose.ui.res.stringResource(com.example.R.string.inactive_susp),
                        value = "$currency${monthlyBillAmount.formatAmount()}",
                        icon = Icons.Default.Warning,
                        iconColor = AmberWarning,
                        modifier = Modifier.weight(1f),
                        subtitle = currentMonthStr
                    )"""
content = content.replace(old_card, new_card)

with open("app/src/main/java/com/example/ui/screens/DashboardScreen.kt", "w") as f:
    f.write(content)

print("Patched DashboardScreen.kt")
