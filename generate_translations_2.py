import os
import re

strings = {
    "Account Status": ("account_status", "অ্যাকাউন্ট স্ট্যাটাস"),
    "Connection & Package": ("connection_package", "সংযোগ ও প্যাকেজ"),
    "Currency ($)": ("currency_symbol", "মুদ্রা ($)"),
    "Customer Code": ("customer_code", "গ্রাহক কোড"),
    "Customer Name": ("customer_name", "গ্রাহকের নাম"),
    "Due Amount": ("due_amount", "বকেয়া পরিমাণ"),
    "Edit": ("edit", "সম্পাদনা"),
    "Generate": ("generate", "তৈরি করুন"),
    "Monthly Fee": ("monthly_fee", "মাসিক ফি"),
    "Notes": ("notes", "নোট"),
    "Package Name": ("package_name", "প্যাকেজের নাম"),
    "Paid Amount": ("paid_amount", "প্রদত্ত পরিমাণ"),
    "Payment Amount ($currencySymbol) *": ("payment_amount_req", "পেমেন্ট পরিমাণ ($currencySymbol) *"),
    "Phone Number": ("phone_number", "ফোন নম্বর"),
    "PPPoE Username": ("pppoe_username", "পিপিওই ইউজারনেম"),
    "Select Bill / Customer *": ("select_bill_req", "বিল / গ্রাহক নির্বাচন করুন *"),
    "Static IP Address": ("static_ip_address", "স্ট্যাটিক আইপি"),
    "Subscriber Details": ("subscriber_details", "গ্রাহকের বিবরণ"),
    "Subscriber Name": ("subscriber_name", "গ্রাহকের নাম"),
    "View Preview": ("view_preview", "প্রিভিউ দেখুন"),
    "Customer saved successfully": ("msg_customer_saved", "গ্রাহক সফলভাবে সেভ হয়েছে"),
    "Customer updated": ("msg_customer_updated", "গ্রাহক আপডেট হয়েছে"),
    "Package updated": ("msg_package_updated", "প্যাকেজ আপডেট হয়েছে"),
    "Package saved": ("msg_package_saved", "প্যাকেজ সেভ হয়েছে"),
    "Customer removed": ("msg_customer_removed", "গ্রাহক মুছে ফেলা হয়েছে"),
    "Package removed": ("msg_package_removed", "প্যাকেজ মুছে ফেলা হয়েছে"),
    "Error recording payment": ("msg_error_payment", "পেমেন্ট রেকর্ড করতে সমস্যা হয়েছে"),
    "Business settings updated": ("msg_business_updated", "ব্যবসার সেটিংস আপডেট হয়েছে"),
    "Database JSON copied to clipboard!": ("msg_db_copied", "ডেটাবেস জেসন কপি করা হয়েছে!"),
    "Backup ready": ("msg_backup_ready", "ব্যাকআপ প্রস্তুত"),
    "No new active customers to bill for $billingMonth": ("msg_no_active_customers_bill", "এই মাসের ($billingMonth) জন্য বিল করার মতো নতুন কোন সক্রিয় গ্রাহক নেই"),
    "No collection payments recorded yet.": ("no_collections_yet", "এখনও কোন পেমেন্ট কালেকশন রেকর্ড করা হয়নি।"),
    "No unpaid bill selected": ("no_unpaid_bill_selected", "কোন বকেয়া বিল নির্বাচন করা হয়নি"),
}

# 1. Add these to strings.xml (both en and bn)
with open("app/src/main/res/values/strings.xml", "r") as f:
    en_content = f.read()
with open("app/src/main/res/values-bn/strings.xml", "r") as f:
    bn_content = f.read()

for en_str, (key, bn_str) in strings.items():
    if f'name="{key}"' not in en_content:
        en_tag = f'    <string name="{key}">{en_str}</string>\n</resources>'
        en_content = en_content.replace('</resources>', en_tag)
    if f'name="{key}"' not in bn_content:
        bn_tag = f'    <string name="{key}">{bn_str}</string>\n</resources>'
        bn_content = bn_content.replace('</resources>', bn_tag)

with open("app/src/main/res/values/strings.xml", "w") as f:
    f.write(en_content)
with open("app/src/main/res/values-bn/strings.xml", "w") as f:
    f.write(bn_content)

# 2. Replace in all KT files
files_to_check = []
for root, dirs, files in os.walk("app/src/main/java/com/example/ui"):
    for file in files:
        if file.endswith(".kt"):
            files_to_check.append(os.path.join(root, file))
files_to_check.append("app/src/main/java/com/example/MainActivity.kt")

def escape_regex(s):
    return re.escape(s).replace(r'\"', r'\\\"')

for file_path in files_to_check:
    with open(file_path, "r") as f:
        content = f.read()
    
    modified = content
    has_changes = False
    
    for en_str, (key, bn_str) in strings.items():
        pattern = r'"' + escape_regex(en_str) + r'"'
        if re.search(pattern, modified):
            if file_path.endswith("IspViewModel.kt") or "toast" in modified.lower():
                # wait, if it's in a toast or viewmodel, we might need context.
                pass
            replacement = f'androidx.compose.ui.res.stringResource(com.example.R.string.{key})'
            modified = re.sub(pattern, replacement, modified)
            has_changes = True

    if has_changes:
        with open(file_path, "w") as f:
            f.write(modified)
        print(f"Modified {file_path}")
