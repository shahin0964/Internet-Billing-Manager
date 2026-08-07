import re

files_to_check = [
    "app/src/main/java/com/example/ui/screens/DashboardScreen.kt",
    "app/src/main/java/com/example/ui/components/BillGenerateDialog.kt",
    "app/src/main/java/com/example/ui/components/PackageDialog.kt"
]

print("Checking strings in files...")
for file_path in files_to_check:
    print(f"\n--- {file_path} ---")
    try:
        with open(file_path, "r") as f:
            content = f.read()
        
        matches = re.finditer(r'androidx\.compose\.ui\.res\.stringResource\([^)]*\)', content)
        for m in matches:
            if "com.example.R.string.msg" in m.group(0) or "com.example.R.string.monthly" in m.group(0):
                print(m.group(0))
    except FileNotFoundError:
        print("File not found")
