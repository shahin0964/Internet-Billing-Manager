package com.example.data.repository

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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class IspRepository(
    private val customerDao: CustomerDao,
    private val packageDao: IspPackageDao,
    private val billDao: BillDao,
    private val paymentDao: PaymentDao,
    private val settingsDao: BusinessSettingsDao,
    private val expenseDao: ExpenseDao
) {
    val customers: Flow<List<CustomerEntity>> = customerDao.getAllCustomers()
    val packages: Flow<List<IspPackageEntity>> = packageDao.getAllPackages()
    val bills: Flow<List<BillEntity>> = billDao.getAllBills()
    val payments: Flow<List<PaymentEntity>> = paymentDao.getAllPayments()
    val settings: Flow<BusinessSettingsEntity?> = settingsDao.getSettings()
    val expenses: Flow<List<ExpenseEntity>> = expenseDao.getAllExpenses()
    val expenseCategories: Flow<List<ExpenseCategoryEntity>> = expenseDao.getAllCategories()

    fun getCollectedAmountForDate(date: String): Flow<Double> {
        return paymentDao.getCollectedAmountForDate(date)
    }

    suspend fun saveExpense(expense: ExpenseEntity): Long {
        return expenseDao.insertExpense(expense)
    }

    suspend fun updateExpense(expense: ExpenseEntity) {
        expenseDao.updateExpense(expense)
    }

    suspend fun deleteExpense(expense: ExpenseEntity) {
        expenseDao.deleteExpense(expense)
    }

    suspend fun saveExpenseCategory(categoryName: String): Long {
        return expenseDao.insertCategory(ExpenseCategoryEntity(name = categoryName.trim()))
    }

    suspend fun saveCustomer(customer: CustomerEntity): Long {
        return customerDao.insertCustomer(customer)
    }

    suspend fun updateCustomer(customer: CustomerEntity) {
        customerDao.updateCustomer(customer)
    }

    suspend fun deleteCustomer(customer: CustomerEntity) {
        customerDao.deleteCustomer(customer)
    }

    suspend fun updateCustomerStatus(id: Long, status: String) {
        customerDao.updateCustomerStatus(id, status)
    }

    suspend fun savePackage(pkg: IspPackageEntity): Long {
        return packageDao.insertPackage(pkg)
    }

    suspend fun updatePackage(pkg: IspPackageEntity) {
        packageDao.updatePackage(pkg)
    }

    suspend fun deletePackage(pkg: IspPackageEntity) {
        packageDao.deletePackage(pkg)
    }

    suspend fun generateMonthlyBills(billingMonth: String, dueDate: String): Int {
        val currentCustomers = customers.first()
        val existingBills = bills.first()
        val activeCustomers = currentCustomers.filter { it.status == "ACTIVE" }
        
        var generatedCount = 0
        val newBills = mutableListOf<BillEntity>()

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayStr = sdf.format(Date())

        for (customer in activeCustomers) {
            val alreadyBilled = existingBills.any {
                it.customerId == customer.id && it.billingMonth.equals(billingMonth, ignoreCase = true)
            }
            if (!alreadyBilled) {
                val billNo = "BILL-${System.currentTimeMillis().toString().takeLast(6)}-${customer.id}"
                newBills.add(
                    BillEntity(
                        billNumber = billNo,
                        customerId = customer.id,
                        customerName = customer.name,
                        customerCode = customer.customerCode,
                        billingMonth = billingMonth,
                        amount = customer.monthlyFee,
                        paidAmount = 0.0,
                        dueAmount = customer.monthlyFee,
                        status = "UNPAID",
                        generatedDate = todayStr,
                        dueDate = dueDate
                    )
                )
                generatedCount++
            }
        }

        if (newBills.isNotEmpty()) {
            billDao.insertBills(newBills)
        }
        return generatedCount
    }

    suspend fun recordPayment(
        billId: Long,
        customerId: Long,
        amount: Double,
        paymentMethod: String,
        notes: String
    ): Boolean {
        // Find ALL unpaid bills for this customer, sorted chronologically (assuming older generatedDate or ID is older)
        val allBills = bills.first()
        val customerUnpaidBills = allBills.filter { 
            it.customerId == customerId && it.dueAmount > 0 
        }.sortedBy { it.id } // Sort by ID to pay oldest first

        if (customerUnpaidBills.isEmpty()) return false

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val todayStr = sdf.format(Date())
        val receiptNo = "PAY-${System.currentTimeMillis().toString().takeLast(6)}"

        var remainingPayment = amount

        for (bill in customerUnpaidBills) {
            if (remainingPayment <= 0) break

            val amountToApply = minOf(remainingPayment, bill.dueAmount)
            remainingPayment -= amountToApply

            val newPaid = bill.paidAmount + amountToApply
            val newDue = (bill.amount - newPaid).coerceAtLeast(0.0)
            val newStatus = when {
                newDue <= 0.0 -> "PAID"
                newPaid > 0.0 -> "PARTIAL"
                else -> "UNPAID"
            }

            val updatedBill = bill.copy(
                paidAmount = newPaid,
                dueAmount = newDue,
                status = newStatus
            )
            billDao.updateBill(updatedBill)
        }

        val payment = PaymentEntity(
            paymentReceiptNo = receiptNo,
            billId = billId, // Use the provided billId or a reference
            customerId = customerId,
            customerName = customerUnpaidBills.first().customerName,
            amount = amount,
            paymentDate = todayStr,
            paymentMethod = paymentMethod,
            notes = notes
        )
        paymentDao.insertPayment(payment)

        return true
    }

    suspend fun saveSettings(settings: BusinessSettingsEntity) {
        settingsDao.insertOrUpdateSettings(settings)
    }

    suspend fun exportDataJson(): String {
        val custs = customers.first()
        val pkgs = packages.first()
        val bls = bills.first()
        val pymts = payments.first()
        val sttngs = settings.first()
        val exps = expenses.first()
        val cats = expenseCategories.first()

        val root = JSONObject()
        val custArray = JSONArray()
        custs.forEach { c ->
            val obj = JSONObject()
            obj.put("id", c.id)
            obj.put("customerCode", c.customerCode)
            obj.put("name", c.name)
            obj.put("phone", c.phone)
            obj.put("address", c.address)
            obj.put("pppoeUsername", c.pppoeUsername)
            obj.put("ipAddress", c.ipAddress)
            obj.put("packageId", c.packageId)
            obj.put("packageName", c.packageName)
            obj.put("monthlyFee", c.monthlyFee)
            obj.put("status", c.status)
            obj.put("joiningDate", c.joiningDate)
            obj.put("notes", c.notes)
            custArray.put(obj)
        }

        val pkgArray = JSONArray()
        pkgs.forEach { p ->
            val obj = JSONObject()
            obj.put("id", p.id)
            obj.put("name", p.name)
            obj.put("speedMbps", p.speedMbps)
            obj.put("monthlyPrice", p.monthlyPrice)
            obj.put("description", p.description)
            pkgArray.put(obj)
        }

        val expArray = JSONArray()
        exps.forEach { e ->
            val obj = JSONObject()
            obj.put("id", e.id)
            obj.put("title", e.title)
            obj.put("amount", e.amount)
            obj.put("category", e.category)
            obj.put("date", e.date)
            obj.put("paymentMethod", e.paymentMethod)
            obj.put("note", e.note)
            obj.put("receiptPath", e.receiptPath ?: "")
            obj.put("createdAt", e.createdAt)
            obj.put("updatedAt", e.updatedAt)
            expArray.put(obj)
        }

        val catArray = JSONArray()
        cats.forEach { c ->
            val obj = JSONObject()
            obj.put("id", c.id)
            obj.put("name", c.name)
            catArray.put(obj)
        }

        root.put("customers", custArray)
        root.put("packages", pkgArray)
        root.put("expenses", expArray)
        root.put("expenseCategories", catArray)
        root.put("billsCount", bls.size)
        root.put("paymentsCount", pymts.size)
        root.put("exportedAt", System.currentTimeMillis())
        return root.toString(2)
    }

    suspend fun importDataJson(jsonStr: String): Boolean {
        return try {
            val root = JSONObject(jsonStr)

            if (root.has("expenses")) {
                val expArray = root.getJSONArray("expenses")
                val expenseList = mutableListOf<ExpenseEntity>()
                for (i in 0 until expArray.length()) {
                    val obj = expArray.getJSONObject(i)
                    expenseList.add(
                        ExpenseEntity(
                            id = if (obj.has("id")) obj.getLong("id") else 0L,
                            title = obj.optString("title", ""),
                            amount = obj.optDouble("amount", 0.0),
                            category = obj.optString("category", "Other"),
                            date = obj.optString("date", ""),
                            paymentMethod = obj.optString("paymentMethod", "Cash"),
                            note = obj.optString("note", ""),
                            receiptPath = obj.optString("receiptPath", "").ifEmpty { null },
                            createdAt = obj.optLong("createdAt", System.currentTimeMillis()),
                            updatedAt = obj.optLong("updatedAt", System.currentTimeMillis())
                        )
                    )
                }
                if (expenseList.isNotEmpty()) {
                    expenseDao.insertExpenses(expenseList)
                }
            }

            if (root.has("expenseCategories")) {
                val catArray = root.getJSONArray("expenseCategories")
                val catList = mutableListOf<ExpenseCategoryEntity>()
                for (i in 0 until catArray.length()) {
                    val obj = catArray.getJSONObject(i)
                    catList.add(
                        ExpenseCategoryEntity(
                            id = if (obj.has("id")) obj.getLong("id") else 0L,
                            name = obj.optString("name", "")
                        )
                    )
                }
                if (catList.isNotEmpty()) {
                    expenseDao.insertCategories(catList)
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
