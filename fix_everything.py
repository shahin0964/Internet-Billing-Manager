import re

with open("app/src/main/res/values/strings.xml", "r") as f:
    en_content = f.read().replace('</resources>', '''
    <string name="msg_due"> • Due: %1$s%2$s</string>
    <string name="msg_half">Half (%1$s%2$s)</string>
    <string name="monthly_price_currency">Monthly Price (%1$s) *</string>
</resources>''')

with open("app/src/main/res/values/strings.xml", "w") as f:
    f.write(en_content)

with open("app/src/main/res/values-bn/strings.xml", "r") as f:
    bn_content = f.read().replace('</resources>', '''
    <string name="msg_due"> • বকেয়া: %1$s%2$s</string>
    <string name="msg_half">অর্ধেক (%1$s%2$s)</string>
    <string name="monthly_price_currency">মাসিক মূল্য (%1$s) *</string>
</resources>''')

with open("app/src/main/res/values-bn/strings.xml", "w") as f:
    f.write(bn_content)

# Fix CustomersScreen
with open("app/src/main/java/com/example/ui/screens/CustomersScreen.kt", "r") as f:
    content = f.read()

content = content.replace('if (totalDue > 0) androidx.compose.ui.res.stringResource(com.example.R.string.msg_due, currencySymbol, totalDue.toString())) else ""', 'if (totalDue > 0) androidx.compose.ui.res.stringResource(com.example.R.string.msg_due, currencySymbol, totalDue.toString()) else ""')

with open("app/src/main/java/com/example/ui/screens/CustomersScreen.kt", "w") as f:
    f.write(content)

# CustomerDialog
with open("app/src/main/java/com/example/ui/components/CustomerDialog.kt", "r") as f:
    content = f.read()

content = content.replace('androidx.compose.ui.res.stringResource(com.example.R.string.custom_package)', 'androidx.compose.ui.res.stringResource(id = com.example.R.string.custom_package)')

with open("app/src/main/java/com/example/ui/components/CustomerDialog.kt", "w") as f:
    f.write(content)

