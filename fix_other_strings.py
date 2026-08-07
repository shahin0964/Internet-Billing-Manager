import re

en_str = '''
    <string name="msg_delete_customer_confirm">Are you sure you want to permanently delete customer "%1$s"?</string>
    <string name="msg_due"> • Due: %1$s%2$s</string>
    <string name="msg_half">Half (%1$s%2$s)</string>
    <string name="msg_outstanding_balance">Outstanding Balance</string>
    <string name="generate_bills_all">Generate bills for all active subscribers</string>
    <string name="custom_package">Custom Package</string>
    <string name="dynamic_none">Dynamic / None</string>
    <string name="needs_follow_up">Needs follow up</string>
    <string name="not_provided">Not provided</string>
    <string name="highest_due_first">Highest Due First</string>
</resources>
'''

bn_str = '''
    <string name="msg_delete_customer_confirm">আপনি কি নিশ্চিত যে আপনি "%1$s" গ্রাহককে স্থায়ীভাবে মুছে ফেলতে চান?</string>
    <string name="msg_due"> • বকেয়া: %1$s%2$s</string>
    <string name="msg_half">অর্ধেক (%1$s%2$s)</string>
    <string name="msg_outstanding_balance">বকেয়া ব্যালেন্স</string>
    <string name="generate_bills_all">সকল সক্রিয় গ্রাহকের বিল তৈরি করুন</string>
    <string name="custom_package">কাস্টম প্যাকেজ</string>
    <string name="dynamic_none">ডাইনামিক / নেই</string>
    <string name="needs_follow_up">ফলো-আপ প্রয়োজন</string>
    <string name="not_provided">প্রদান করা হয়নি</string>
    <string name="highest_due_first">সর্বোচ্চ বকেয়া আগে</string>
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
    "app/src/main/java/com/example/ui/screens/CustomersScreen.kt",
    "app/src/main/java/com/example/ui/components/CustomerDialog.kt",
    "app/src/main/java/com/example/ui/components/PaymentDialog.kt",
    "app/src/main/java/com/example/ui/screens/BillingScreen.kt",
    "app/src/main/java/com/example/ui/screens/DueManagementScreen.kt"
]

for file_path in files_to_check:
    try:
        with open(file_path, "r") as f:
            content = f.read()
    except FileNotFoundError:
        continue
    
    content = content.replace('"Are you sure you want to permanently delete customer \\"${customerToDelete?.name}\\"?"', 'androidx.compose.ui.res.stringResource(com.example.R.string.msg_delete_customer_confirm, customerToDelete?.name ?: "")')
    content = content.replace('" • Due: $currencySymbol$totalDue"', 'androidx.compose.ui.res.stringResource(com.example.R.string.msg_due, currencySymbol, totalDue.toString())')
    content = content.replace('"Half ($currencySymbol$half)"', 'androidx.compose.ui.res.stringResource(com.example.R.string.msg_half, currencySymbol, half.toString())')
    content = content.replace('"Outstanding Balance"', 'androidx.compose.ui.res.stringResource(com.example.R.string.msg_outstanding_balance)')
    content = content.replace('"Generate bills for all active subscribers"', 'androidx.compose.ui.res.stringResource(com.example.R.string.generate_bills_all)')
    content = content.replace('"Custom Package"', 'androidx.compose.ui.res.stringResource(com.example.R.string.custom_package)')
    content = content.replace('"Dynamic / None"', 'androidx.compose.ui.res.stringResource(com.example.R.string.dynamic_none)')
    content = content.replace('"Needs follow up"', 'androidx.compose.ui.res.stringResource(com.example.R.string.needs_follow_up)')
    content = content.replace('"Not provided"', 'androidx.compose.ui.res.stringResource(com.example.R.string.not_provided)')
    content = content.replace('"Highest Due First"', 'androidx.compose.ui.res.stringResource(com.example.R.string.highest_due_first)')

    with open(file_path, "w") as f:
        f.write(content)

