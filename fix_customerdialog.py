with open("app/src/main/java/com/example/ui/components/CustomerDialog.kt", "r") as f:
    content = f.read()

content = content.replace('val pkgName = selectedPkg?.name ?: androidx.compose.ui.res.stringResource(id = com.example.R.string.custom_package)', 'val pkgName = selectedPkg?.name ?: "Custom Package"')

with open("app/src/main/java/com/example/ui/components/CustomerDialog.kt", "w") as f:
    f.write(content)
