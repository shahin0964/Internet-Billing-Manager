import re

with open('app/src/main/java/com/example/ui/screens/MoreScreen.kt', 'r') as f:
    content = f.read()

target = """.androidx.compose.foundation.verticalScroll(androidx.compose.foundation.rememberScrollState())"""
replacement = """.verticalScroll(androidx.compose.foundation.rememberScrollState())"""

if target in content:
    content = content.replace(target, replacement)
    
if "import androidx.compose.foundation.verticalScroll" not in content:
    content = content.replace("import androidx.compose.foundation.layout.fillMaxWidth", "import androidx.compose.foundation.layout.fillMaxWidth\nimport androidx.compose.foundation.verticalScroll")

with open('app/src/main/java/com/example/ui/screens/MoreScreen.kt', 'w') as f:
    f.write(content)
print("Success")
