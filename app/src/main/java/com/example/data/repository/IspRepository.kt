package com.example.data.repository

import com.example.data.dao.BillDao
import com.example.data.dao.BusinessSettingsDao
import com.example.data.dao.CustomerDao
import com.example.data.dao.IspPackageDao
import com.example.data.dao.PaymentDao
import com.example.data.model.BillEntity
import com.example.data.model.BusinessSettingsEntity
import com.example.data.model.CustomerEntity
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
    private val settingsDao: BusinessSettingsDao
) {
    val customers: Flow<List<CustomerEntity>> = customerDao.getAllCustomers()
    val packages: Flow<List<IspPackageEntity>> = packageDao.getAllPackages()
    val bills: Flow<List<BillEntity>> = billDao.getAllBills()
    val payments: Flow<List<PaymentEntity>> = paymentDao.getAllPayments()
    val settings: Flow<BusinessSettingsEntity?> = settingsDao.getSettings()

    fun getCollectedAmountForDate(date: String): Flow<Double> {
        return paymentDao.getCollectedAmountForDate(date)
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

        root.put("customers", custArray)
        root.put("packages", pkgArray)
        root.put("billsCount", bls.size)
        root.put("paymentsCount", pymts.size)
        root.put("exportedAt", System.currentTimeMillis())
        return root.toString(2)
    }
}
