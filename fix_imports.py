with open("app/src/main/java/com/example/ui/screens/MoreScreen.kt", "r") as f:
    lines = f.readlines()

new_lines = []
for line in lines:
    if line.startswith("import androidx.compose.ui.draw.clip"): continue
    if line.startswith("import androidx.compose.ui.Alignment") and "MoreScreen.kt" not in line: continue
    if line.startswith("import androidx.compose.ui.layout.ContentScale"): continue
    new_lines.append(line)

content = "".join(new_lines)
content = content.replace("package com.example.ui.screens", "package com.example.ui.screens\n\nimport androidx.compose.ui.draw.clip\nimport androidx.compose.ui.Alignment\nimport androidx.compose.ui.layout.ContentScale")

# Remove duplicate Alignment imports if any
content = content.replace("import androidx.compose.ui.Alignment\nimport androidx.compose.ui.Alignment", "import androidx.compose.ui.Alignment")

with open("app/src/main/java/com/example/ui/screens/MoreScreen.kt", "w") as f:
    f.write(content)
