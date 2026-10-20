package com.example.finance.pengeluaran.controller

import com.example.finance.pengeluaran.entity.Expense
import com.example.finance.pengeluaran.service.ExpenseService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/expenses")
class ExpenseController(private val expenseService: ExpenseService) {

    @PostMapping
    fun addExpense(@Valid @RequestBody expense: Expense): Expense =
        expenseService.addExpense(expense)

    @GetMapping
    fun getAllExpenses(): List<Expense> =
        expenseService.getAllExpenses()

    @GetMapping("/{id}")
    fun getExpenseById(@PathVariable id: Long): Expense =
        expenseService.getExpenseById(id)

    @PutMapping("/{id}")
    fun updateExpense(
        @PathVariable id: Long,
        @Valid @RequestBody expense: Expense
    ): Expense =
        expenseService.updateExpense(id, expense)

    @DeleteMapping("/{id}")
    fun deleteExpense(@PathVariable id: Long) =
        expenseService.deleteExpense(id)
}