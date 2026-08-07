import re

en_str = '''
    <string name="msg_collected">Collected: %1$s%2$s</string>
    <string name="msg_generate_customers">Generate (%1$d Customers)</string>
    <string name="msg_generate_desc">This action will generate monthly bills for all %1$d active customer(s) who haven\\'t been billed for the selected month.</string>
    <string name="msg_outstanding">Outstanding: %1$s%2$s</string>
    <string name="msg_total_generated">Total Generated: %1$s%2$s</string>
    <string name="msg_total">Total: %1$d</string>
    <string name="msg_outstanding_balance">Outstanding Balance: %1$s%2$s</string>
    <string name="monthly_fee_currency">Monthly Fee (%1$s)</string>
    <string name="monthly_price_currency">Monthly Price (%1$s) *</string>
</resources>
'''

bn_str = '''
    <string name="msg_collected">কালেকশন: %1$s%2$s</string>
    <string name="msg_generate_customers">তৈরি করুন (%1$d গ্রাহক)</string>
    <string name="msg_generate_desc">এই অপশনটি দিয়ে %1$d জন সক্রিয় গ্রাহকের বিল তৈরি করা হবে, যাদের বিল এখনো এই মাসের জন্য তৈরি করা হয়নি।</string>
    <string name="msg_outstanding">বকেয়া: %1$s%2$s</string>
    <string name="msg_total_generated">মোট তৈরি করা বিল: %1$s%2$s</string>
    <string name="msg_total">মোট: %1$d</string>
    <string name="msg_outstanding_balance">বকেয়া পরিমাণ: %1$s%2$s</string>
    <string name="monthly_fee_currency">মাসিক ফি (%1$s)</string>
    <string name="monthly_price_currency">মাসিক মূল্য (%1$s) *</string>
</resources>
'''

with open("app/src/main/res/values/strings.xml", "r") as f:
    en_content = f.read().replace('</resources>', en_str)
with open("app/src/main/res/values-bn/strings.xml", "r") as f:
    bn_content = f.read().replace('</resources>', bn_str)

with open("app/src/main/res/values/strings.xml", "w") as f:
    f.write(en_content)
with open("app/src/main/res/values-bn/strings.xml", "w") as f:
    f.write(bn_content)

files_to_check = [
    "app/src/main/java/com/example/ui/screens/DashboardScreen.kt",
    "app/src/main/java/com/example/ui/components/BillGenerateDialog.kt",
    "app/src/main/java/com/example/ui/components/PackageDialog.kt"
]

for file_path in files_to_check:
    try:
        with open(file_path, "r") as f:
            content = f.read()
    except FileNotFoundError:
        continue
    
    content = content.replace('"Collected: $currency$totalCollectedAmount"', 'androidx.compose.ui.res.stringResource(com.example.R.string.msg_collected, currency, totalCollectedAmount.toString())')
    content = content.replace('"Generate ($activeCustomerCount Customers)"', 'androidx.compose.ui.res.stringResource(com.example.R.string.msg_generate_customers, activeCustomerCount)')
    content = content.replace('"This action will generate monthly bills for all $activeCustomerCount active customer(s) who haven\'t been billed for the selected month."', 'androidx.compose.ui.res.stringResource(com.example.R.string.msg_generate_desc, activeCustomerCount)')
    content = content.replace('"Outstanding: $currency$totalDueAmount"', 'androidx.compose.ui.res.stringResource(com.example.R.string.msg_outstanding, currency, totalDueAmount.toString())')
    content = content.replace('"Total Generated: $currency$totalBillingAmount"', 'androidx.compose.ui.res.stringResource(com.example.R.string.msg_total_generated, currency, totalBillingAmount.toString())')
    content = content.replace('"Total: $totalCount"', 'androidx.compose.ui.res.stringResource(com.example.R.string.msg_total, totalCount)')
    content = content.replace('"Outstanding Balance: $currencySymbol$totalOutstanding"', 'androidx.compose.ui.res.stringResource(com.example.R.string.msg_outstanding_balance, currencySymbol, totalOutstanding.toString())')
    content = content.replace('"Monthly Fee ($currencySymbol)"', 'androidx.compose.ui.res.stringResource(com.example.R.string.monthly_fee_currency, currencySymbol)')
    content = content.replace('"Monthly Price ($currencySymbol) *"', 'androidx.compose.ui.res.stringResource(com.example.R.string.monthly_price_currency, currencySymbol)')

    with open(file_path, "w") as f:
        f.write(content)

