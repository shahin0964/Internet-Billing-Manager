import re
with open("app/src/main/java/com/example/ui/screens/MoreScreen.kt", "r") as f:
    content = f.read()

content = re.sub(r'TextButton\(\s*Button\(onClick = \{ onEditPackageClick\(pkg\) \}\) \{', 'TextButton(onClick = { onEditPackageClick(pkg) }) {', content)
content = re.sub(r'RadioButton\(\s*Button\(', 'RadioButton(', content)

with open("app/src/main/java/com/example/ui/screens/MoreScreen.kt", "w") as f:
    f.write(content)
