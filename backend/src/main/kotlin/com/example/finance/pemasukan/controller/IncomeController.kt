package com.example.finance.pemasukan.controller

import com.example.finance.pemasukan.entity.Income
import com.example.finance.pemasukan.service.IncomeService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/incomes")
class IncomeController(private val incomeService: IncomeService) {

    @PostMapping
    fun addIncome(@Valid @RequestBody income: Income): Income =
        incomeService.addIncome(income)

    @GetMapping
    fun getAllIncomes(): List<Income> =
        incomeService.getAllIncomes()

    @GetMapping("/{id}")
    fun getIncomeById(@PathVariable id: Long): Income =
        incomeService.getIncomeById(id)

    @PutMapping("/{id}")
    fun updateIncome(
        @PathVariable id: Long,
        @Valid @RequestBody income: Income
    ): Income =
        incomeService.updateIncome(id, income)

    @DeleteMapping("/{id}")
    fun deleteIncome(@PathVariable id: Long) =
        incomeService.deleteIncome(id)
}