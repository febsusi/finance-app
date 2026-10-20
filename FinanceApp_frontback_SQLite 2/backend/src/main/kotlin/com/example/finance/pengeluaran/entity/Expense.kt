package com.example.finance.pengeluaran.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.time.LocalDate

@Entity
@Table(name = "expenses")
data class Expense(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @field:NotBlank(message = "Deskripsi tidak boleh kosong")
    @Column(nullable = false)
    var description: String,

    @field:NotNull(message = "Jumlah pengeluaran wajib diisi")
    @field:Positive(message = "Jumlah pengeluaran harus lebih dari 0")
    @Column(nullable = false)
    var amount: Double,

    @field:NotNull(message = "Tanggal wajib diisi")
    @Column(nullable = false)
    var date: LocalDate
)