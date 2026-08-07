with open("app/src/main/java/com/example/data/model/Entities.kt", "r") as f:
    content = f.read()

content = content.replace("val themeMode: String = \"SYSTEM\" // SYSTEM, DARK, LIGHT",
                          "val themeMode: String = \"SYSTEM\", // SYSTEM, DARK, LIGHT\n    val logoUri: String? = null")

with open("app/src/main/java/com/example/data/model/Entities.kt", "w") as f:
    f.write(content)
