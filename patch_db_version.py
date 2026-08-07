with open("app/src/main/java/com/example/data/database/IspDatabase.kt", "r") as f:
    content = f.read()

content = content.replace("version = 1,", "version = 2,")
content = content.replace("import androidx.sqlite.db.SupportSQLiteDatabase", "import androidx.sqlite.db.SupportSQLiteDatabase\nimport androidx.room.migration.Migration")

migration_code = """
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE business_settings ADD COLUMN logoUri TEXT")
            }
        }
"""

content = content.replace("companion object {", "companion object {" + migration_code)
content = content.replace(".fallbackToDestructiveMigration()", ".addMigrations(MIGRATION_1_2)\n                    .fallbackToDestructiveMigration()")

with open("app/src/main/java/com/example/data/database/IspDatabase.kt", "w") as f:
    f.write(content)
