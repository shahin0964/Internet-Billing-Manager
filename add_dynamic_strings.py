import re

en_str = '    <string name="msg_generated_bills">Generated %1$d bills for %2$s</string>\n    <string name="msg_payment_recorded">Payment of %1$s%2$s recorded</string>\n    <string name="msg_customer_status">Customer status set to %1$s</string>\n</resources>'
bn_str = '    <string name="msg_generated_bills">%2$s মাসের জন্য %1$d টি বিল তৈরি করা হয়েছে</string>\n    <string name="msg_payment_recorded">%1$s%2$s পেমেন্ট রেকর্ড করা হয়েছে</string>\n    <string name="msg_customer_status">গ্রাহকের স্ট্যাটাস %1$s এ সেট করা হয়েছে</string>\n</resources>'

with open("app/src/main/res/values/strings.xml", "r") as f:
    en_content = f.read().replace('</resources>', en_str)
with open("app/src/main/res/values-bn/strings.xml", "r") as f:
    bn_content = f.read().replace('</resources>', bn_str)

with open("app/src/main/res/values/strings.xml", "w") as f:
    f.write(en_content)
with open("app/src/main/res/values-bn/strings.xml", "w") as f:
    f.write(bn_content)

with open("app/src/main/java/com/example/ui/viewmodel/IspViewModel.kt", "r") as f:
    content = f.read()

content = content.replace('"Generated $count bills for $billingMonth"', 'getApplication<Application>().getString(com.example.R.string.msg_generated_bills, count, billingMonth)')
content = content.replace('"Payment of ${settings.value.currencySymbol}$amount recorded"', 'getApplication<Application>().getString(com.example.R.string.msg_payment_recorded, settings.value.currencySymbol, amount.toString())')
content = content.replace('"Customer status set to $newStatus"', 'getApplication<Application>().getString(com.example.R.string.msg_customer_status, newStatus)')

with open("app/src/main/java/com/example/ui/viewmodel/IspViewModel.kt", "w") as f:
    f.write(content)

