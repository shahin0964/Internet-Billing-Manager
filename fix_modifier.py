import re

with open("app/src/main/java/com/example/ui/screens/MoreScreen.kt", "r") as f:
    content = f.read()

content = content.replace("Modifier.size(64.dp).androidx.compose.foundation.background(MaterialTheme.colorScheme.surfaceVariant, androidx.compose.foundation.shape.CircleShape)", "Modifier.size(64.dp).background(MaterialTheme.colorScheme.surfaceVariant, androidx.compose.foundation.shape.CircleShape)")
content = content.replace("Modifier.size(64.dp).androidx.compose.ui.draw.clip(androidx.compose.foundation.shape.CircleShape)", "Modifier.size(64.dp).clip(androidx.compose.foundation.shape.CircleShape)")

with open("app/src/main/java/com/example/ui/screens/MoreScreen.kt", "w") as f:
    f.write(content)
