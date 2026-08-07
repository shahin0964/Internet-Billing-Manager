import os
import re

STRINGS_EN = {
    "Dashboard": "dashboard",
    "Customers": "customers",
    "Billing": "billing",
    "Collection": "collection",
    "More": "more",
    "Total Bill": "total_bill",
    "Total Collected": "total_collected",
    "Total Due": "total_due",
    "Add Customer": "add_customer",
    "Collect Payment": "collect_payment",
    "Generate Bills": "generate_bills",
    "Active Users": "active_users",
    "Inactive/Susp": "inactive_susp",
    "Business KPIs": "business_kpis",
    "Recent Collections": "recent_collections",
    "View All": "view_all",
    "Unpaid Dues": "unpaid_dues",
    "Quick Operations": "quick_operations",
    "Live network & billing status": "live_network_status",
    "Generate Monthly Bills": "generate_monthly_bills",
    "Select Package": "select_package",
    "Save Customer": "save_customer",
    "Save Package": "save_package",
    "Delete Customer": "delete_customer",
    "Cancel": "cancel",
    "Search": "search",
    "Language": "language"
}

STRINGS_BN = {
    "dashboard": "ড্যাশবোর্ড",
    "customers": "গ্রাহক",
    "billing": "বিলিং",
    "collection": "কালেকশন",
    "more": "আরও",
    "total_bill": "মোট বিল",
    "total_collected": "মোট কালেকশন",
    "total_due": "মোট বকেয়া",
    "add_customer": "গ্রাহক যোগ করুন",
    "collect_payment": "পেমেন্ট নিন",
    "generate_bills": "বিল তৈরি করুন",
    "active_users": "সক্রিয় গ্রাহক",
    "inactive_susp": "নিষ্ক্রিয়/স্থগিত",
    "business_kpis": "বিজনেস কেপিআই",
    "recent_collections": "সাম্প্রতিক কালেকশন",
    "view_all": "সবগুলো দেখুন",
    "unpaid_dues": "বকেয়া বিল",
    "quick_operations": "কুইক অপারেশনস",
    "live_network_status": "লাইভ নেটওয়ার্ক ও বিলিং স্ট্যাটাস",
    "generate_monthly_bills": "মাসিক বিল তৈরি করুন",
    "select_package": "প্যাকেজ নির্বাচন করুন",
    "save_customer": "গ্রাহক সেভ করুন",
    "save_package": "প্যাকেজ সেভ করুন",
    "delete_customer": "গ্রাহক ডিলিট করুন",
    "cancel": "বাতিল",
    "search": "অনুসন্ধান",
    "language": "ভাষা (Language)"
}

# Create values-bn directory
os.makedirs("app/src/main/res/values-bn", exist_ok=True)
os.makedirs("app/src/main/res/values", exist_ok=True)

# Generate strings.xml for English
with open("app/src/main/res/values/strings.xml", "w") as f:
    f.write('<?xml version="1.0" encoding="utf-8"?>\n<resources>\n')
    f.write('    <string name="app_name">ISP Control</string>\n')
    for en_val, key in STRINGS_EN.items():
        f.write(f'    <string name="{key}">{en_val}</string>\n')
    f.write('</resources>\n')

# Generate strings.xml for Bengali
with open("app/src/main/res/values-bn/strings.xml", "w") as f:
    f.write('<?xml version="1.0" encoding="utf-8"?>\n<resources>\n')
    f.write('    <string name="app_name">আইএসপি কন্ট্রোল</string>\n')
    for key, bn_val in STRINGS_BN.items():
        f.write(f'    <string name="{key}">{bn_val}</string>\n')
    f.write('</resources>\n')

# Now apply stringResource to files
files_to_check = []
for root, dirs, files in os.walk("app/src/main/java/com/example/ui"):
    for file in files:
        if file.endswith(".kt"):
            files_to_check.append(os.path.join(root, file))
files_to_check.append("app/src/main/java/com/example/MainActivity.kt")

for file_path in files_to_check:
    with open(file_path, "r") as f:
        content = f.read()
    
    modified = content
    has_changes = False
    
    for en_val, key in STRINGS_EN.items():
        # Replace exact string literals
        pattern = r'"' + re.escape(en_val) + r'"'
        if re.search(pattern, modified):
            modified = re.sub(pattern, f'androidx.compose.ui.res.stringResource(com.example.R.string.{key})', modified)
            has_changes = True

    if has_changes:
        with open(file_path, "w") as f:
            f.write(modified)
        print(f"Modified {file_path}")

print("Done")
