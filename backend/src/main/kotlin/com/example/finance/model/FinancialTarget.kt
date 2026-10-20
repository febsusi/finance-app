package com.example.finance.model

import jakarta.persistence.*

@Entity
@Table(name = "financial_targets")
data class FinancialTarget(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,
    val targetAmount: Long,
    val savedAmount: Long = 0,
    val deadline: String? = null,
    val description: String? = null
)
