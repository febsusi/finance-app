package com.example.finance.repository

import com.example.finance.model.FinancialTarget
import org.springframework.data.jpa.repository.JpaRepository

interface FinancialTargetRepository : JpaRepository<FinancialTarget, Long>
