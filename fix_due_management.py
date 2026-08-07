import re

with open("app/src/main/java/com/example/ui/screens/DueManagementScreen.kt", "r") as f:
    content = f.read()

content = content.replace("val sortDueDesc = sortDueDesc", "val sortDueDesc = androidx.compose.ui.res.stringResource(com.example.R.string.sort_due_desc)")
content = content.replace("val sortDueAsc = sortDueAsc", "val sortDueAsc = androidx.compose.ui.res.stringResource(com.example.R.string.sort_due_asc)")
content = content.replace("val sortName = sortName", "val sortName = androidx.compose.ui.res.stringResource(com.example.R.string.sort_name)")

with open("app/src/main/java/com/example/ui/screens/DueManagementScreen.kt", "w") as f:
    f.write(content)
