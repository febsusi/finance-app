package com.example.finance.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "transactions")
data class Transaction(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val title: String,
    val amount: Long,
    @Enumerated(EnumType.STRING)
    val type: TransactionType,
    val category: String,
    val date: LocalDate,
    val note: String? = null
)

enum class TransactionType { INCOME, EXPENSE }
