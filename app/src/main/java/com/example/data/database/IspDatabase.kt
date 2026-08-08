package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration
import com.example.data.dao.BillDao
import com.example.data.dao.BusinessSettingsDao
import com.example.data.dao.CustomerDao
import com.example.data.dao.ExpenseDao
import com.example.data.dao.IspPackageDao
import com.example.data.dao.PaymentDao
import com.example.data.model.BillEntity
import com.example.data.model.BusinessSettingsEntity
import com.example.data.model.CustomerEntity
import com.example.data.model.ExpenseCategoryEntity
import com.example.data.model.ExpenseEntity
import com.example.data.model.IspPackageEntity
import com.example.data.model.PaymentEntity

@Database(
    entities = [
        CustomerEntity::class,
        IspPackageEntity::class,
        BillEntity::class,
        PaymentEntity::class,
        BusinessSettingsEntity::class,
        ExpenseEntity::class,
        ExpenseCategoryEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class IspDatabase : RoomDatabase() {

    abstract fun customerDao(): CustomerDao
    abstract fun packageDao(): IspPackageDao
    abstract fun billDao(): BillDao
    abstract fun paymentDao(): PaymentDao
    abstract fun settingsDao(): BusinessSettingsDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE business_settings ADD COLUMN logoUri TEXT")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `expenses` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `title` TEXT NOT NULL,
                        `amount` REAL NOT NULL,
                        `category` TEXT NOT NULL,
                        `date` TEXT NOT NULL,
                        `paymentMethod` TEXT NOT NULL,
                        `note` TEXT NOT NULL,
                        `receiptPath` TEXT,
                        `createdAt` INTEGER NOT NULL,
                        `updatedAt` INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `expense_categories` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `name` TEXT NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        @Volatile
        private var INSTANCE: IspDatabase? = null

        fun getDatabase(context: Context): IspDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    IspDatabase::class.java,
                    "isp_control_center.db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    // Strict Data Preservation: Never use fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
