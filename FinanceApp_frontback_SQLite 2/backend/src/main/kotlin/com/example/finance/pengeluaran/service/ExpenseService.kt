package com.example.finance.pengeluaran.service

import com.example.finance.pengeluaran.entity.Expense
import com.example.finance.pengeluaran.repository.ExpenseRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class ExpenseService(private val expenseRepository: ExpenseRepository) {

    fun addExpense(expense: Expense): Expense =
        expenseRepository.save(expense)

    fun getAllExpenses(): List<Expense> =
        expenseRepository.findAll()

    fun getExpenseById(id: Long): Expense {
        return expenseRepository.findById(id)
            .orElseThrow {
                ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Pengeluaran dengan ID $id tidak ditemukan"
                )
            }
    }

    fun updateExpense(id: Long, expense: Expense): Expense {
        val existingExpense = expenseRepository.findById(id)
            .orElseThrow {
                ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Pengeluaran dengan ID $id tidak ditemukan"
                )
            }

        existingExpense.description = expense.description
        existingExpense.amount = expense.amount
        existingExpense.date = expense.date

        return expenseRepository.save(existingExpense)
    }

    fun deleteExpense(id: Long) {
        val existingExpense = expenseRepository.findById(id)
            .orElseThrow {
                ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Pengeluaran dengan ID $id tidak ditemukan"
                )
            }

        expenseRepository.delete(existingExpense)
    }
}