import os
import re

strings = {
    "Add New Customer": ("add_new_customer", "নতুন গ্রাহক যোগ করুন"),
    "Customer Name *": ("customer_name_req", "গ্রাহকের নাম *"),
    "Phone Number *": ("phone_number_req", "ফোন নম্বর *"),
    "PPPoE Username / ID *": ("pppoe_req", "পিপিপিওই ইউজারনেম / আইডি *"),
    "Address / Location": ("address_location", "ঠিকানা / অবস্থান"),
    "Internet Package *": ("internet_package_req", "ইন্টারনেট প্যাকেজ *"),
    "Connection Date": ("connection_date", "সংযোগের তারিখ"),
    "Static IP (Optional)": ("static_ip_opt", "স্ট্যাটিক আইপি (ঐচ্ছিক)"),
    "Save Customer": ("save_customer", "গ্রাহক সেভ করুন"),
    "Cancel": ("cancel", "বাতিল"),
    "Select Package": ("select_package", "প্যাকেজ নির্বাচন করুন"),
    "Edit Customer": ("edit_customer", "গ্রাহক সম্পাদনা করুন"),
    "Delete Customer": ("delete_customer", "গ্রাহক মুছুন"),
    "Are you sure you want to permanently delete customer \\\"": ("delete_customer_confirm", "আপনি কি নিশ্চিত যে আপনি গ্রাহককে স্থায়ীভাবে মুছে ফেলতে চান \\\""),
    "Customer Preview": ("customer_preview", "গ্রাহক প্রিভিউ"),
    "Connection Status": ("connection_status", "সংযোগ স্ট্যাটাস"),
    "Financial Overview": ("financial_overview", "আর্থিক বিবরণ"),
    "Recent Bills": ("recent_bills", "সাম্প্রতিক বিল"),
    "Recent Payments": ("recent_payments", "সাম্প্রতিক পেমেন্ট"),
    "Back to List": ("back_to_list", "তালিকায় ফিরে যান"),
    "No customers added yet": ("no_customers_added", "এখনও কোন গ্রাহক যোগ করা হয়নি"),
    "Search name, phone, PPPoE user, code...": ("search_customers_hint", "নাম, ফোন, পিপিওই ইউজার, কোড খুঁজুন..."),
    "No matching customers": ("no_matching_customers", "কোন গ্রাহক পাওয়া যায়নি"),
    "Sort:": ("sort", "সাজান:"),
    "NAME": ("sort_name", "নাম"),
    "DUE_DESC": ("sort_due_desc", "বকেয়া (বেশি থেকে কম)"),
    "DUE_ASC": ("sort_due_asc", "বকেয়া (কম থেকে বেশি)"),

    # Billing
    "Monthly Billing Control": ("monthly_billing_control", "মাসিক বিলিং কন্ট্রোল"),
    "Generate Bills": ("generate_bills", "বিল তৈরি করুন"),
    "Generate monthly bills for your subscribers to track payments and dues.": ("generate_bills_desc", "সাবস্ক্রাইবারদের বকেয়া ও পেমেন্ট ট্র্যাক করতে মাসিক বিল তৈরি করুন।"),
    "No bills generated yet": ("no_bills_generated", "এখনও কোন বিল তৈরি হয়নি"),
    "Search customer name, bill #, month...": ("search_bills_hint", "গ্রাহকের নাম, বিল নম্বর, মাস খুঁজুন..."),
    "No matching bills": ("no_matching_bills", "কোন বিল পাওয়া যায়নি"),
    "Generate Monthly Bills": ("generate_monthly_bills", "মাসিক বিল তৈরি করুন"),
    "Billing Month": ("billing_month", "বিলিং মাস"),
    "e.g. August 2026": ("eg_august_2026", "যেমন আগস্ট ২০২৬"),
    "Payment Due Date": ("payment_due_date", "পেমেন্টের শেষ তারিখ"),

    # Collection
    "Today Collection": ("today_collection", "আজকের কালেকশন"),
    "Total Collection": ("total_collection", "মোট কালেকশন"),
    "Record Customer Payment": ("record_customer_payment", "পেমেন্ট রেকর্ড করুন"),
    "Collect": ("collect", "কালেকশন"),
    "Search customer, receipt #, payment method...": ("search_payments_hint", "গ্রাহক, রসিদ নম্বর, পেমেন্ট মাধ্যম খুঁজুন..."),
    "Payment Receipts History": ("payment_receipts_history", "পেমেন্ট রসিদের ইতিহাস"),
    "No payment receipts yet": ("no_payment_receipts", "এখনও কোন পেমেন্ট রসিদ নেই"),
    "When customer payments are collected, payment receipts will appear here.": ("payment_receipts_desc", "গ্রাহকের পেমেন্ট নেওয়া হলে এখানে রসিদ দেখা যাবে।"),
    "Record First Payment": ("record_first_payment", "প্রথম পেমেন্ট রেকর্ড করুন"),
    "No matching receipts": ("no_matching_receipts", "কোন রসিদ পাওয়া যায়নি"),
    "Confirm Payment": ("confirm_payment", "পেমেন্ট নিশ্চিত করুন"),
    "Payment Method": ("payment_method", "পেমেন্ট মাধ্যম"),
    "Cash": ("cash", "ক্যাশ"),
    "bKash": ("bkash", "বিকাশ"),
    "Bank Transfer": ("bank_transfer", "ব্যাংক ট্রান্সফার"),
    "Card": ("card", "কার্ড"),
    "Online": ("online", "অনলাইন"),
    "Receipt Notes / Reference": ("receipt_notes", "রসিদ নোট / রেফারেন্স"),
    "e.g. bKash TrxID #8X92M": ("eg_bkash_trx", "যেমন বিকাশ TrxID #8X92M"),

    # Due
    "Zero Outstanding Dues!": ("zero_dues", "কোন বকেয়া নেই!"),
    "Great news! All customer bills are fully paid up.": ("all_paid_desc", "খুশির খবর! সকল গ্রাহকের বিল সম্পূর্ণ পরিশোধিত।"),
    "Search unpaid subscriber name or month...": ("search_dues_hint", "বকেয়া সাবস্ক্রাইবার বা মাস খুঁজুন..."),
    "No matching dues": ("no_matching_dues", "কোন বকেয়া পাওয়া যায়নি"),
    "Unpaid Accounts Follow-Up": ("unpaid_accounts_follow_up", "বকেয়া হিসাবের ফলোআপ"),
    "Total Outstanding Dues": ("total_outstanding_dues", "মোট বকেয়া"),
    "Unpaid Subscribers": ("unpaid_subscribers", "বকেয়া থাকা সাবস্ক্রাইবার"),
    "Call": ("call", "কল করুন"),
    "SMS Notice": ("sms_notice", "এসএমএস নোটিশ"),
    "Collect Payment": ("collect_payment", "পেমেন্ট নিন"),

    # More Screen / Settings
    "ISP Business Information": ("isp_business_info", "আইএসপি ব্যবসার তথ্য"),
    "Configure NOC branding and network status": ("configure_noc_desc", "এনওসি ব্র্যান্ডিং এবং নেটওয়ার্ক স্ট্যাটাস কনফিগার করুন"),
    "ISP Name / Reseller Brand": ("isp_name_brand", "আইএসপি নাম / রিসেলার ব্র্যান্ড"),
    "Support Hotline": ("support_hotline", "সাপোর্ট হটলাইন"),
    "Office Address / NOC Location": ("office_address", "অফিসের ঠিকানা / এনওসি লোকেশন"),
    "Network Status": ("network_status", "নেটওয়ার্ক স্ট্যাটাস"),
    "Operational": ("operational", "সচল (Operational)"),
    "Degraded": ("degraded", "ধীরগতি (Degraded)"),
    "Maintenance": ("maintenance", "রক্ষণাবেক্ষণ (Maintenance)"),
    "Save Business Info": ("save_business_info", "ব্যবসার তথ্য সেভ করুন"),
    "Speed Packages": ("speed_packages", "স্পিড প্যাকেজ"),
    "Manage your ISP subscriber list, package assignments, and billing statuses.": ("manage_isp_desc", "আপনার আইএসপি সাবস্ক্রাইবার তালিকা, প্যাকেজ এবং বিলিং পরিচালনা করুন।"),
    "+ Add Package": ("add_package_btn", "+ প্যাকেজ যোগ করুন"),
    "No packages created yet.": ("no_packages_created", "এখনও কোন প্যাকেজ তৈরি করা হয়নি।"),
    "App Theme Appearance": ("app_theme_appearance", "অ্যাপ থিমের চেহারা"),
    "System": ("theme_system", "সিস্টেম"),
    "Light": ("theme_light", "হালকা"),
    "Dark": ("theme_dark", "গাঢ়"),
    "Data Utilities & Backup": ("data_utilities_backup", "ডেটা ইউটিলিটি ও ব্যাকআপ"),
    "Export JSON Data Backup": ("export_json_backup", "জেসন ডেটা ব্যাকআপ এক্সপোর্ট করুন"),

    # Package Dialog
    "Add Internet Package": ("add_internet_package", "ইন্টারনেট প্যাকেজ যোগ করুন"),
    "Edit Package": ("edit_package", "প্যাকেজ সম্পাদনা করুন"),
    "Package Name *": ("package_name_req", "প্যাকেজ নাম *"),
    "e.g. 20 Mbps Ultra Fiber": ("eg_package_name", "যেমন ২০ এমবিপিএস আল্ট্রা ফাইবার"),
    "Speed (Mbps) *": ("speed_mbps_req", "স্পিড (এমবিপিএস) *"),
    "Description / Features": ("description_features", "বিবরণ / বৈশিষ্ট্য"),
    "e.g. Dedicated bandwidth, 24/7 support": ("eg_features", "যেমন ডেডিকেটেড ব্যান্ডউইথ, ২৪/৭ সাপোর্ট"),

    "Notes (Optional)": ("notes_optional", "নোট (ঐচ্ছিক)"),
    "Previous Month": ("previous_month", "আগের মাস"),
    "Next Month": ("next_month", "পরের মাস"),

    "Billing Analytics": ("billing_analytics", "বিলিং অ্যানালিটিক্স"),
    "Active Users": ("active_users", "সক্রিয় গ্রাহক"),
    "Inactive/Susp": ("inactive_susp", "নিষ্ক্রিয়/স্থগিত"),
    "Business KPIs": ("business_kpis", "বিজনেস কেপিআই"),
    "Recent Collections": ("recent_collections", "সাম্প্রতিক কালেকশন"),
    "View All": ("view_all", "সবগুলো দেখুন"),
    "Unpaid Dues": ("unpaid_dues", "বকেয়া বিল"),
    "Quick Operations": ("quick_operations", "কুইক অপারেশনস"),
    "View Bills": ("view_bills", "বিলগুলো দেখুন"),
    "Clear": ("clear", "ক্লিয়ার"),
}

# 1. Add these to strings.xml (both en and bn)
# Read existing en
with open("app/src/main/res/values/strings.xml", "r") as f:
    en_content = f.read()
# Read existing bn
with open("app/src/main/res/values-bn/strings.xml", "r") as f:
    bn_content = f.read()

for en_str, (key, bn_str) in strings.items():
    # Only add if not already present
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
            replacement = f'androidx.compose.ui.res.stringResource(com.example.R.string.{key})'
            # special handling for default parameter strings in components
            modified = re.sub(pattern, replacement, modified)
            has_changes = True

    if has_changes:
        with open(file_path, "w") as f:
            f.write(modified)
        print(f"Modified {file_path}")
