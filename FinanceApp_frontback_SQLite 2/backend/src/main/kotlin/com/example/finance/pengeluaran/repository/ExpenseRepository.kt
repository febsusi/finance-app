package com.example.finance.pengeluaran.repository

import com.example.finance.pengeluaran.entity.Expense
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExpenseRepository : JpaRepository<Expense, Long>
