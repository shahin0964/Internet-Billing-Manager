with open("app/src/main/java/com/example/ui/screens/DashboardScreen.kt", "r") as f:
    content = f.read()

# Replace the specific block
old_block = """QuickActionButton(
                            label = androidx.compose.ui.res.stringResource(com.example.R.string.unpaid_dues),
                            icon = Icons.Default.Payments,"""
                            
new_block = """QuickActionButton(
                            label = androidx.compose.ui.res.stringResource(com.example.R.string.unpaid_dues),
                            icon = Icons.Default.MoneyOff,"""

content = content.replace(old_block, new_block)

with open("app/src/main/java/com/example/ui/screens/DashboardScreen.kt", "w") as f:
    f.write(content)
