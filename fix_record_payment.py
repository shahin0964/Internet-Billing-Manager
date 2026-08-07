import re

with open("app/src/main/java/com/example/data/repository/IspRepository.kt", "r") as f:
    content = f.read()

replacement = """    suspend fun recordPayment(
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
    }"""

# Use regex to replace the entire recordPayment function
content = re.sub(r'    suspend fun recordPayment\([\s\S]*?return true\n    \}', replacement, content)

with open("app/src/main/java/com/example/data/repository/IspRepository.kt", "w") as f:
    f.write(content)
