with open("app/src/main/java/com/example/ui/screens/MoreScreen.kt", "r") as f:
    content = f.read()

content = content.replace("TextButton(                    Button(onClick = { onEditPackageClick(pkg) }) {", "TextButton(onClick = { onEditPackageClick(pkg) }) {")
content = content.replace("RadioButton(                    Button(\n                                    selected = (themeMode == mode),", "RadioButton(\n                                    selected = (themeMode == mode),")

with open("app/src/main/java/com/example/ui/screens/MoreScreen.kt", "w") as f:
    f.write(content)
