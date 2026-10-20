package com.example.finance.pemasukan.repository

import com.example.finance.pemasukan.entity.Income
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IncomeRepository : JpaRepository<Income, Long>