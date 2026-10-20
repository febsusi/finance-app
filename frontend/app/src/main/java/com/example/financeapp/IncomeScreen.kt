
package com.example.financeapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowUpward
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
import com.example.financeapp.data.Income
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val IncomeGreen = Color(0xFF16865C)
private val IncomeDark = Color(0xFF10513C)
private val IncomeBackground = Color(0xFFF0F8F2)
private val IncomeSoft = Color(0xFFDDF3E5)
private val IncomeMuted = Color(0xFF728477)

private fun incomeRupiah(amount: Double): String =
    NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        .format(amount).replace(",00", "")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeScreen(onDataChanged: () -> Unit = {}) {
    val scope = rememberCoroutineScope()

    var incomes by remember { mutableStateOf<List<Income>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var editingIncome by remember { mutableStateOf<Income?>(null) }
    var actionError by remember { mutableStateOf<String?>(null) }

    fun loadIncomes() {
        scope.launch {
            loading = true
            try {
                incomes = Api.service.incomes()
                error = null
            } catch (e: Exception) {
                error = "Gagal memuat pemasukan: ${e.message ?: "Periksa server."}"
            } finally {
                loading = false
            }
        }
    }

    LaunchedEffect(Unit) { loadIncomes() }

    val totalIncome = incomes.sumOf { it.amount }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(IncomeBackground)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    IncomeGreen,
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
                        text = "Pemasukan",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )

                    Text(
                        text = "Catat dan kelola uang masukmu",
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
                        Icons.Default.ArrowUpward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }

        // Kartu ringkasan
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
                            .background(IncomeSoft, RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Payments,
                            contentDescription = null,
                            tint = IncomeGreen
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("TOTAL PEMASUKAN", color = IncomeMuted,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold)
                        Text(
                            if (loading) "Memuat..." else incomeRupiah(totalIncome),
                            color = IncomeDark,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))
                HorizontalDivider(color = IncomeSoft)
                Spacer(Modifier.height(12.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Transaksi tercatat", color = IncomeMuted)
                    Text(
                        "${incomes.size} transaksi",
                        color = IncomeGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Tombol tambah
        Button(
            onClick = {
                editingIncome = null
                actionError = null
                showDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = IncomeGreen,
                contentColor = Color.White
            )
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Tambah pemasukan", fontWeight = FontWeight.Bold)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Riwayat pemasukan",
                modifier = Modifier.weight(1f),
                color = IncomeDark,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold
            )
            IconButton(onClick = { loadIncomes() }) {
                Icon(Icons.Default.Refresh, contentDescription = "Muat ulang",
                    tint = IncomeGreen)
            }
        }

        actionError?.let {
            Text(it, color = Color(0xFFB42318),
                modifier = Modifier.padding(horizontal = 16.dp))
        }

        when {
            loading -> Box(
                Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = IncomeGreen)
            }

            error != null -> Column(
                Modifier.fillMaxWidth().weight(1f).padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(error!!, color = Color(0xFFB42318))
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = { loadIncomes() },
                    colors = ButtonDefaults.buttonColors(containerColor = IncomeGreen)
                ) { Text("Coba lagi") }
            }

            incomes.isEmpty() -> Column(
                Modifier.fillMaxWidth().weight(1f).padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    Modifier.size(76.dp).background(IncomeSoft, RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Payments, contentDescription = null,
                        tint = IncomeGreen, modifier = Modifier.size(38.dp))
                }
                Spacer(Modifier.height(14.dp))
                Text("Belum ada pemasukan", color = IncomeDark,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold)
                Text("Tekan tombol di atas untuk menambahkan transaksi.",
                    color = IncomeMuted)
            }

            else -> LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(incomes, key = { it.id }) { income ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            Modifier.fillMaxWidth().padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                Modifier.size(46.dp)
                                    .background(IncomeSoft, RoundedCornerShape(15.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.ArrowUpward, contentDescription = null,
                                    tint = IncomeGreen)
                            }

                            Spacer(Modifier.width(12.dp))

                            Column(Modifier.weight(1f)) {
                                Text(income.description, color = IncomeDark,
                                    fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(3.dp))
                                Text(income.date, color = IncomeMuted,
                                    style = MaterialTheme.typography.bodySmall)
                                Spacer(Modifier.height(5.dp))
                                Text("+${incomeRupiah(income.amount)}",
                                    color = IncomeGreen,
                                    fontWeight = FontWeight.ExtraBold)
                            }

                            IconButton(onClick = {
                                editingIncome = income
                                actionError = null
                                showDialog = true
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit",
                                    tint = IncomeGreen)
                            }

                            IconButton(onClick = {
                                scope.launch {
                                    try {
                                        Api.service.deleteIncome(income.id)
                                        loadIncomes()
                                        onDataChanged()
                                    } catch (e: Exception) {
                                        actionError = "Gagal menghapus pemasukan."
                                    }
                                }
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Hapus",
                                    tint = Color(0xFFB42318))
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        IncomeDialog(
            oldIncome = editingIncome,
            onDismiss = {
                showDialog = false
                editingIncome = null
            },
            onSave = { description, amount, date ->
                scope.launch {
                    try {
                        val old = editingIncome
                        val data = Income(
                            id = old?.id ?: 0L,
                            description = description,
                            amount = amount,
                            date = date
                        )

                        if (old == null) {
                            Api.service.addIncome(data)
                        } else {
                            Api.service.updateIncome(old.id, data)
                        }

                        showDialog = false
                        editingIncome = null
                        loadIncomes()
                        onDataChanged()
                    } catch (e: Exception) {
                        actionError = "Gagal menyimpan pemasukan: ${e.message ?: "Periksa server."}"
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IncomeDialog(
    oldIncome: Income?,
    onDismiss: () -> Unit,
    onSave: (String, Double, String) -> Unit
) {
    var description by remember(oldIncome) {
        mutableStateOf(oldIncome?.description ?: "")
    }
    var amount by remember(oldIncome) {
        mutableStateOf(oldIncome?.amount?.toLong()?.toString() ?: "")
    }
    var date by remember(oldIncome) {
        mutableStateOf(
            oldIncome?.date ?: SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(Date())
        )
    }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFF0F8F2),
        titleContentColor = IncomeDark,
        title = {
            Text(
                if (oldIncome == null) "Tambah Pemasukan" else "Edit Pemasukan",
                fontWeight = FontWeight.ExtraBold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it; errorMessage = null },
                    label = { Text("Deskripsi") },
                    placeholder = { Text("Contoh: Gaji bulanan") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = IncomeGreen,
                        focusedLabelColor = IncomeGreen,
                        cursorColor = IncomeGreen
                    )
                )

                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        amount = it.filter(Char::isDigit)
                        errorMessage = null
                    },
                    label = { Text("Nominal (Rp)") },
                    placeholder = { Text("500000") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = IncomeGreen,
                        focusedLabelColor = IncomeGreen,
                        cursorColor = IncomeGreen
                    )
                )

                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it; errorMessage = null },
                    label = { Text("Tanggal (YYYY-MM-DD)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = IncomeGreen,
                        focusedLabelColor = IncomeGreen,
                        cursorColor = IncomeGreen
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
                colors = ButtonDefaults.buttonColors(containerColor = IncomeGreen)
            ) { Text("Simpan", color = Color.White) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, colors = ButtonDefaults.textButtonColors(
                contentColor = IncomeGreen
            )) { Text("Batal") }
        }
    )
}
