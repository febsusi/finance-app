package com.example.finance.controller

import com.example.finance.pemasukan.repository.IncomeRepository
import com.example.finance.pengeluaran.repository.ExpenseRepository
import com.example.finance.repository.FinancialTargetRepository
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class DashboardResponse(
    val balance: Long,
    val income: Long,
    val expense: Long,
    val transactionCount: Int,
    val targetCount: Int
)

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = ["*"])
class DashboardController(
    private val incomeRepo: IncomeRepository,
    private val expenseRepo: ExpenseRepository,
    private val targetRepo: FinancialTargetRepository
) {

    @GetMapping
    fun dashboard(): DashboardResponse {
        val incomes = incomeRepo.findAll()
        val expenses = expenseRepo.findAll()

        val totalIncome = incomes.sumOf { it.amount.toLong() }
        val totalExpense = expenses.sumOf { it.amount.toLong() }

        return DashboardResponse(
            balance = totalIncome - totalExpense,
            income = totalIncome,
            expense = totalExpense,
            transactionCount = incomes.size + expenses.size,
            targetCount = targetRepo.count().toInt()
        )
    }
}
