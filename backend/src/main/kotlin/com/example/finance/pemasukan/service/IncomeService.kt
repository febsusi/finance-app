package com.example.finance.pemasukan.service

import com.example.finance.pemasukan.entity.Income
import com.example.finance.pemasukan.repository.IncomeRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class IncomeService(private val incomeRepository: IncomeRepository) {

    fun addIncome(income: Income): Income =
        incomeRepository.save(income)

    fun getAllIncomes(): List<Income> =
        incomeRepository.findAll()

    fun getIncomeById(id: Long): Income {
        return incomeRepository.findById(id)
            .orElseThrow {
                ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Pemasukan dengan ID $id tidak ditemukan"
                )
            }
    }

    fun updateIncome(id: Long, income: Income): Income {
        val existingIncome = incomeRepository.findById(id)
            .orElseThrow {
                ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Pemasukan dengan ID $id tidak ditemukan"
                )
            }

        existingIncome.description = income.description
        existingIncome.amount = income.amount
        existingIncome.date = income.date

        return incomeRepository.save(existingIncome)
    }

    fun deleteIncome(id: Long) {
        val existingIncome = incomeRepository.findById(id)
            .orElseThrow {
                ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Pemasukan dengan ID $id tidak ditemukan"
                )
            }

        incomeRepository.delete(existingIncome)
    }
}