import re

with open("app/src/main/java/com/example/ui/screens/MoreScreen.kt", "r") as f:
    content = f.read()

old_block = """                                val updated = settings.copy(
                                    ispName = ispName.trim(),
                                    hotline = hotline.trim(),
                                    address = address.trim(),
                                    currencySymbol = currencySymbol.trim(),
                                    networkStatus = networkStatus,
                                    themeMode = themeMode
                                )"""

new_block = """                                val updated = settings.copy(
                                    ispName = ispName.trim(),
                                    hotline = hotline.trim(),
                                    address = address.trim(),
                                    currencySymbol = currencySymbol.trim(),
                                    networkStatus = networkStatus,
                                    themeMode = themeMode,
                                    logoUri = logoUri
                                )"""

content = content.replace(old_block, new_block)

with open("app/src/main/java/com/example/ui/screens/MoreScreen.kt", "w") as f:
    f.write(content)
