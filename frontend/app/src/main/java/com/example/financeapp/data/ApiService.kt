package com.example.financeapp.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

data class Transaction(
    val id: Long = 0L,
    val title: String,
    val amount: Long,
    val type: String,
    val category: String,
    val date: String,
    val note: String? = null
)

data class Income(
    val id: Long = 0L,
    val description: String,
    val amount: Double,
    val date: String
)

data class Expense(
    val id: Long = 0L,
    val description: String,
    val amount: Double,
    val date: String
)

data class FinancialTarget(
    val id: Long = 0L,
    val name: String,
    val targetAmount: Long,
    val savedAmount: Long = 0L,
    val deadline: String? = null,
    val description: String? = null
)

data class Dashboard(
    val balance: Long,
    val income: Long,
    val expense: Long,
    val transactionCount: Int,
    val targetCount: Int
)

interface ApiService {

    // =========================
    // TRANSACTIONS
    // =========================

    @GET("api/transactions")
    suspend fun transactions(): List<Transaction>

    @POST("api/transactions")
    suspend fun addTransaction(
        @Body transaction: Transaction
    ): Transaction

    @PUT("api/transactions/{id}")
    suspend fun updateTransaction(
        @Path("id") id: Long,
        @Body transaction: Transaction
    ): Transaction

    @DELETE("api/transactions/{id}")
    suspend fun deleteTransaction(
        @Path("id") id: Long
    )

    // =========================
    // PEMASUKAN
    // =========================

    @GET("api/incomes")
    suspend fun incomes(): List<Income>

    @POST("api/incomes")
    suspend fun addIncome(
        @Body income: Income
    ): Income

    @PUT("api/incomes/{id}")
    suspend fun updateIncome(
        @Path("id") id: Long,
        @Body income: Income
    ): Income

    @DELETE("api/incomes/{id}")
    suspend fun deleteIncome(
        @Path("id") id: Long
    )

    // =========================
    // PENGELUARAN
    // =========================

    @GET("api/expenses")
    suspend fun expenses(): List<Expense>

    @POST("api/expenses")
    suspend fun addExpense(
        @Body expense: Expense
    ): Expense

    @PUT("api/expenses/{id}")
    suspend fun updateExpense(
        @Path("id") id: Long,
        @Body expense: Expense
    ): Expense

    @DELETE("api/expenses/{id}")
    suspend fun deleteExpense(
        @Path("id") id: Long
    )

    // =========================
    // TARGET
    // =========================

    @GET("api/targets")
    suspend fun targets(): List<FinancialTarget>

    @POST("api/targets")
    suspend fun addTarget(
        @Body target: FinancialTarget
    ): FinancialTarget

    @PUT("api/targets/{id}")
    suspend fun updateTarget(
        @Path("id") id: Long,
        @Body target: FinancialTarget
    ): FinancialTarget

    @DELETE("api/targets/{id}")
    suspend fun deleteTarget(
        @Path("id") id: Long
    )

    // =========================
    // DASHBOARD
    // =========================

    @GET("api/dashboard")
    suspend fun dashboard(): Dashboard
}


object Api {

    // Android Emulator mengakses backend di komputer host melalui 10.0.2.2.
    // Jangan gunakan 127.0.0.1 di emulator karena itu menunjuk ke emulator itu sendiri.
    // Untuk perangkat Android fisik, ganti dengan IP LAN komputer yang menjalankan backend.
    private const val BASE_URL = "http://10.0.2.2:8080/"

    val service: ApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}
