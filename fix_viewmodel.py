import re

with open("app/src/main/java/com/example/ui/viewmodel/IspViewModel.kt", "r") as f:
    content = f.read()

# 1. Add autoGenerateCurrentMonthBills call in init block
init_block_replacement = """        todayCollectionAmount = repository.getCollectedAmountForDate(todayStr).stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0
        )

        seedDefaultPackagesAndSettingsIfNeeded()
        autoGenerateCurrentMonthBills()
    }"""
content = content.replace("        seedDefaultPackagesAndSettingsIfNeeded()\n    }", init_block_replacement)

# 2. Add billingScreenBills flow and autoGenerate function
additional_code = """
    val billingScreenBills: StateFlow<List<BillEntity>> = bills.map { rawBills ->
        val unpaidBills = rawBills.filter { it.status == "UNPAID" || it.status == "PARTIAL" }
        val billsByCustomer = unpaidBills.groupBy { it.customerId }
        val displayBills = mutableListOf<BillEntity>()

        for ((custId, cBills) in billsByCustomer) {
            val sorted = cBills.sortedByDescending { it.id }
            val currentBill = sorted.first()
            val previousBills = sorted.drop(1)
            val previousDue = previousBills.sumOf { it.dueAmount }

            if (previousDue > 0) {
                val totalDue = currentBill.dueAmount + previousDue
                val virtualBill = currentBill.copy(
                    billNumber = "Cur: ${currentBill.dueAmount} | Prev Due: $previousDue",
                    amount = totalDue,
                    dueAmount = totalDue,
                    paidAmount = 0.0 // Representing remaining aggregate
                )
                displayBills.add(virtualBill)
            } else {
                displayBills.add(currentBill)
            }
        }
        
        // Also add paid bills just in case they are needed? BillingScreen filters by UNPAID/PARTIAL
        val paidBills = rawBills.filter { it.status == "PAID" }
        displayBills.addAll(paidBills)
        
        displayBills
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private fun autoGenerateCurrentMonthBills() {
        viewModelScope.launch {
            val sdfMonth = java.text.SimpleDateFormat("MMMM yyyy", java.util.Locale.getDefault())
            val sdfDay = java.text.SimpleDateFormat("yyyy-MM-10", java.util.Locale.getDefault())
            val currentMonth = sdfMonth.format(java.util.Date())
            val dueDate = sdfDay.format(java.util.Date())
            repository.generateMonthlyBills(currentMonth, dueDate)
        }
    }
"""
content = content.replace("    private fun seedDefaultPackagesAndSettingsIfNeeded() {", additional_code + "\n    private fun seedDefaultPackagesAndSettingsIfNeeded() {")

# 3. Import combine, map if needed
# Actually map is already imported maybe?
if "import kotlinx.coroutines.flow.map" not in content:
    content = content.replace("import kotlinx.coroutines.flow.combine", "import kotlinx.coroutines.flow.combine\nimport kotlinx.coroutines.flow.map")

with open("app/src/main/java/com/example/ui/viewmodel/IspViewModel.kt", "w") as f:
    f.write(content)
