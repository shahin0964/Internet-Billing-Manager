import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

attach_base = """
    override fun attachBaseContext(newBase: android.content.Context) {
        val prefs = newBase.getSharedPreferences("app_prefs", android.content.Context.MODE_PRIVATE)
        val lang = prefs.getString("app_lang", "en") ?: "en"
        val locale = java.util.Locale(lang)
        java.util.Locale.setDefault(locale)
        val config = android.content.res.Configuration(newBase.resources.configuration)
        config.setLocale(locale)
        val context = newBase.createConfigurationContext(config)
        super.attachBaseContext(context)
    }

    override fun onCreate"""

content = content.replace("    override fun onCreate", attach_base)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
