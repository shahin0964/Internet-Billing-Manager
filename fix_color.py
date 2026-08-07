with open("app/src/main/java/com/example/ui/screens/DashboardScreen.kt", "r") as f:
    content = f.read()

old_block = """KpiCard(
                        title = androidx.compose.ui.res.stringResource(com.example.R.string.today_collection),
                        value = "$currency${todayCollectionAmount.formatAmount()}",
                        icon = Icons.Default.Payments,
                        iconColor = CrimsonDanger,"""
                            
new_block = """KpiCard(
                        title = androidx.compose.ui.res.stringResource(com.example.R.string.today_collection),
                        value = "$currency${todayCollectionAmount.formatAmount()}",
                        icon = Icons.Default.Payments,
                        iconColor = EmeraldSuccess,"""

content = content.replace(old_block, new_block)

with open("app/src/main/java/com/example/ui/screens/DashboardScreen.kt", "w") as f:
    f.write(content)
