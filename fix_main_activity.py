import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

# Replace the bills parameter in BillingScreen call
content = content.replace('                    BillingScreen(\n                        bills = bills,', '                    val billingScreenBills by viewModel.billingScreenBills.collectAsStateWithLifecycle()\n                    BillingScreen(\n                        bills = billingScreenBills,')

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
