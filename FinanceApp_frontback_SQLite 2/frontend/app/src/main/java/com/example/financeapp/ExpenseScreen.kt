
package com.example.financeapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.financeapp.data.Api
import com.example.financeapp.data.Expense
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val ExpenseRed = Color(0xFFE34D5B)
private val ExpenseDark = Color(0xFF762632)
private val ExpenseBackground = Color(0xFFFFF1F2)
private val ExpenseSoft = Color(0xFFFFDDE1)
private val ExpenseMuted = Color(0xFF8D7378)

private fun expenseRupiah(amount: Double): String =
    NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        .format(amount).replace(",00", "")

@Composable
fun ExpenseScreen(onDataChanged: () -> Unit = {}) {
    val scope = rememberCoroutineScope()

    var expenses by remember { mutableStateOf<List<Expense>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var actionError by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var editingExpense by remember { mutableStateOf<Expense?>(null) }

    fun loadExpenses() {
        scope.launch {
            loading = true
            try {
                expenses = Api.service.expenses()
                error = null
            } catch (e: Exception) {
                error = "Gagal mengambil data pengeluaran: ${e.message ?: "Periksa koneksi server."}"
            } finally {
                loading = false
            }
        }
    }

    LaunchedEffect(Unit) {
        loadExpenses()
    }

    val totalExpense = expenses.sumOf { it.amount }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ExpenseBackground)
    ) {
        // Header merah
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    ExpenseRed,
                    RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                )
                .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Pengeluaran",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )

                    Text(
                        text = "Catat dan kelola uang keluarmu",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .background(
                            Color.White.copy(alpha = 0.18f),
                            RoundedCornerShape(18.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.ArrowDownward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }

        // Ringkasan pengeluaran
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .offset(y = (-12).dp),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(42.dp)
                            .background(ExpenseSoft, RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Payments,
                            contentDescription = null,
                            tint = ExpenseRed
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column {
                        Text(
                            "TOTAL PENGELUARAN",
                            color = ExpenseMuted,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            if (loading) "Memuat..." else expenseRupiah(totalExpense),
                            color = ExpenseDark,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))
                HorizontalDivider(color = ExpenseSoft)
                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Transaksi tercatat", color = ExpenseMuted)
                    Text(
                        "${expenses.size} transaksi",
                        color = ExpenseRed,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Tambah pengeluaran
        Button(
            onClick = {
                editingExpense = null
                actionError = null
                showDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ExpenseRed,
                contentColor = Color.White
            )
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Tambah pengeluaran", fontWeight = FontWeight.Bold)
        }

        // Judul riwayat
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Riwayat pengeluaran",
                modifier = Modifier.weight(1f),
                color = ExpenseDark,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold
            )

            IconButton(onClick = { loadExpenses() }) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = "Muat ulang",
                    tint = ExpenseRed
                )
            }
        }

        actionError?.let {
            Text(
                it,
                color = Color(0xFFB42318),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        when {
            loading -> {
                Box(
                    Modifier.fillMaxWidth().weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = ExpenseRed)
                }
            }

            error != null -> {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(error!!, color = Color(0xFFB42318))
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = { loadExpenses() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ExpenseRed
                        )
                    ) {
                        Text("Coba lagi")
                    }
                }
            }

            expenses.isEmpty() -> {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        Modifier
                            .size(76.dp)
                            .background(ExpenseSoft, RoundedCornerShape(24.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Payments,
                            contentDescription = null,
                            tint = ExpenseRed,
                            modifier = Modifier.size(38.dp)
                        )
                    }

                    Spacer(Modifier.height(14.dp))

                    Text(
                        "Belum ada pengeluaran",
                        color = ExpenseDark,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "Catat pengeluaran pertamamu lewat tombol di atas.",
                        color = ExpenseMuted
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 20.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(expenses, key = { it.id }) { expense ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 1.dp
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    Modifier
                                        .size(46.dp)
                                        .background(
                                            ExpenseSoft,
                                            RoundedCornerShape(15.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.ArrowDownward,
                                        contentDescription = null,
                                        tint = ExpenseRed
                                    )
                                }

                                Spacer(Modifier.width(12.dp))

                                Column(Modifier.weight(1f)) {
                                    Text(
                                        expense.description,
                                        color = ExpenseDark,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(Modifier.height(3.dp))

                                    Text(
                                        expense.date,
                                        color = ExpenseMuted,
                                        style = MaterialTheme.typography.bodySmall
                                    )

                                    Spacer(Modifier.height(5.dp))

                                    Text(
                                        "-${expenseRupiah(expense.amount)}",
                                        color = ExpenseRed,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        editingExpense = expense
                                        actionError = null
                                        showDialog = true
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Edit",
                                        tint = ExpenseRed
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        scope.launch {
                                            try {
                                                Api.service.deleteExpense(expense.id)
                                                loadExpenses()
                                                onDataChanged()
                                            } catch (e: Exception) {
                                                actionError =
                                                    "Gagal menghapus pengeluaran: ${e.message ?: "Periksa server."}"
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Hapus",
                                        tint = Color(0xFFB42318)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        ExpenseDialog(
            oldExpense = editingExpense,
            onDismiss = {
                showDialog = false
                editingExpense = null
            },
            onSave = { description, amount, date ->
                scope.launch {
                    try {
                        val old = editingExpense
                        val data = Expense(
                            id = old?.id ?: 0L,
                            description = description,
                            amount = amount,
                            date = date
                        )

                        if (old == null) {
                            Api.service.addExpense(data)
                        } else {
                            Api.service.updateExpense(old.id, data)
                        }

                        showDialog = false
                        editingExpense = null
                        loadExpenses()
                        onDataChanged()
                    } catch (e: Exception) {
                        actionError =
                            "Gagal menyimpan pengeluaran: ${e.message ?: "Periksa server."}"
                    }
                }
            }
        )
    }
}

@Composable
private fun ExpenseDialog(
    oldExpense: Expense?,
    onDismiss: () -> Unit,
    onSave: (String, Double, String) -> Unit
) {
    var description by remember(oldExpense) {
        mutableStateOf(oldExpense?.description ?: "")
    }
    var amount by remember(oldExpense) {
        mutableStateOf(oldExpense?.amount?.toLong()?.toString() ?: "")
    }
    var date by remember(oldExpense) {
        mutableStateOf(
            oldExpense?.date
                ?: SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        )
    }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ExpenseBackground,
        titleContentColor = ExpenseDark,
        title = {
            Text(
                if (oldExpense == null) "Tambah Pengeluaran" else "Edit Pengeluaran",
                fontWeight = FontWeight.ExtraBold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                        errorMessage = null
                    },
                    label = { Text("Deskripsi") },
                    placeholder = { Text("Contoh: Makan, transportasi") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ExpenseRed,
                        focusedLabelColor = ExpenseRed,
                        cursorColor = ExpenseRed
                    )
                )

                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        amount = it.filter(Char::isDigit)
                        errorMessage = null
                    },
                    label = { Text("Nominal (Rp)") },
                    placeholder = { Text("50000") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ExpenseRed,
                        focusedLabelColor = ExpenseRed,
                        cursorColor = ExpenseRed
                    )
                )

                OutlinedTextField(
                    value = date,
                    onValueChange = {
                        date = it
                        errorMessage = null
                    },
                    label = { Text("Tanggal (YYYY-MM-DD)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ExpenseRed,
                        focusedLabelColor = ExpenseRed,
                        cursorColor = ExpenseRed
                    )
                )

                errorMessage?.let {
                    Text(it, color = Color(0xFFB42318))
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val value = amount.toDoubleOrNull()

                    when {
                        description.isBlank() ->
                            errorMessage = "Deskripsi wajib diisi."

                        value == null || value <= 0 ->
                            errorMessage = "Nominal harus lebih dari 0."

                        !date.matches(Regex("\\d{4}-\\d{2}-\\d{2}")) ->
                            errorMessage = "Format tanggal harus YYYY-MM-DD."

                        else -> onSave(description.trim(), value, date.trim())
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ExpenseRed
                )
            ) {
                Text("Simpan", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = ExpenseRed
                )
            ) {
                Text("Batal")
            }
        }
    )
}
