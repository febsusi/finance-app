package com.example.financeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.financeapp.data.Api
import com.example.financeapp.data.Expense
import com.example.financeapp.data.FinancialTarget
import com.example.financeapp.data.Income
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

private val FinanceNavy = Color(0xFF14213D)

private val FinanceGreen = Color(0xFF16865C)
private val FinanceGreenDark = Color(0xFF10513C)
private val FinanceGreenBackground = Color(0xFFE4F5EA)

private val FinanceRed = Color(0xFFE34D5B)
private val FinanceRedDark = Color(0xFF762632)
private val FinanceRedBackground = Color(0xFFFFE5E8)

private val FinanceBlue = Color(0xFF4169E1)
private val FinanceBlueDark = Color(0xFF2344A0)
private val FinanceBlueBackground = Color(0xFFE4ECFF)

private val FinanceBackground = Color(0xFFF4F6FA)
private val FinanceMuted = Color(0xFF7B8495)

private fun rupiah(value: Long): String =
    NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        .format(value).replace(",00", "")

private fun rupiahDouble(value: Double): String =
    NumberFormat.getCurrencyInstance(Locale("id", "ID"))
        .format(value).replace(",00", "")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = android.graphics.Color.rgb(228, 245, 234)
        window.navigationBarColor = android.graphics.Color.rgb(255, 255, 255)
        window.decorView.systemUiVisibility =
            android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = FinanceGreen,
                    secondary = FinanceBlue,
                    background = FinanceBackground,
                    surface = Color.White,
                    error = FinanceRed
                )
            ) {
                FinanceApp()
            }
        }
    }
}

@Composable
fun FinanceApp() {
    var selectedTab by remember { mutableIntStateOf(0) }

    var targets by remember { mutableStateOf<List<FinancialTarget>>(emptyList()) }
    var incomes by remember { mutableStateOf<List<Income>>(emptyList()) }
    var expenses by remember { mutableStateOf<List<Expense>>(emptyList()) }

    var loadingTargets by remember { mutableStateOf(true) }
    var loadingFinance by remember { mutableStateOf(true) }
    var targetError by remember { mutableStateOf<String?>(null) }
    var financeError by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    fun refreshFinance() {
        scope.launch {
            loadingFinance = true
            financeError = null

            try {
                incomes = Api.service.incomes()
                expenses = Api.service.expenses()
            } catch (e: Exception) {
                financeError =
                    "Gagal memuat saldo: ${e.message ?: "Periksa koneksi server."}"
            } finally {
                loadingFinance = false
            }
        }
    }

    fun refreshTargets() {
        scope.launch {
            loadingTargets = true
            targetError = null

            try {
                targets = Api.service.targets()
            } catch (e: Exception) {
                targetError =
                    "Gagal memuat target: ${e.message ?: "Periksa koneksi server."}"
            } finally {
                loadingTargets = false
            }
        }
    }

    LaunchedEffect(Unit) {
        refreshFinance()
        refreshTargets()
    }

    val totalIncome = incomes.sumOf { it.amount }
    val totalExpense = expenses.sumOf { it.amount }
    val balance = totalIncome - totalExpense

    val pageColor = when (selectedTab) {
        0 -> FinanceGreen
        1 -> FinanceRed
        else -> FinanceBlue
    }

    val pageBackground = when (selectedTab) {
        0 -> FinanceGreenBackground
        1 -> FinanceRedBackground
        else -> FinanceBlueBackground
    }

    Scaffold(
        containerColor = pageBackground,
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = pageColor
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                    },
                    icon = {
                        Icon(
                            Icons.Default.ArrowUpward,
                            contentDescription = "Pemasukan"
                        )
                    },
                    label = { Text("Pemasukan") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = FinanceGreen,
                        selectedTextColor = FinanceGreen,
                        indicatorColor = FinanceGreenBackground,
                        unselectedIconColor = FinanceMuted,
                        unselectedTextColor = FinanceMuted
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(
                            Icons.Default.ArrowDownward,
                            contentDescription = "Pengeluaran"
                        )
                    },
                    label = { Text("Pengeluaran") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = FinanceRed,
                        selectedTextColor = FinanceRed,
                        indicatorColor = FinanceRedBackground,
                        unselectedIconColor = FinanceMuted,
                        unselectedTextColor = FinanceMuted
                    )
                )

                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = {
                        selectedTab = 2
                        refreshTargets()
                    },
                    icon = {
                        Icon(
                            Icons.Default.Savings,
                            contentDescription = "Target"
                        )
                    },
                    label = { Text("Target") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = FinanceBlue,
                        selectedTextColor = FinanceBlue,
                        indicatorColor = FinanceBlueBackground,
                        unselectedIconColor = FinanceMuted,
                        unselectedTextColor = FinanceMuted
                    )
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(pageBackground)
        ) {
            FinanceSummaryCard(
                totalIncome = totalIncome,
                totalExpense = totalExpense,
                balance = balance,
                loading = loadingFinance,
                error = financeError,
                accent = pageColor,
                onRefresh = { refreshFinance() },
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 12.dp
                )
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when (selectedTab) {
                    0 -> IncomeScreen(
                        onDataChanged = { refreshFinance() }
                    )

                    1 -> ExpenseScreen(
                        onDataChanged = { refreshFinance() }
                    )

                    2 -> TargetScreen(
                        items = targets,
                        balance = balance,
                        refresh = { refreshTargets() },
                        loading = loadingTargets,
                        error = targetError
                    )
                }
            }
        }
    }
}

/*
 * Kartu saldo utama.
 */
@Composable
private fun FinanceSummaryCard(
    totalIncome: Double,
    totalExpense: Double,
    balance: Double,
    loading: Boolean,
    error: String?,
    accent: Color,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = accent
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            accent,
                            accent.copy(alpha = 0.78f)
                        )
                    )
                )
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(
                            Color.White.copy(alpha = 0.18f),
                            RoundedCornerShape(15.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(Modifier.weight(1f)) {
                    Text(
                        "SALDO SAAT INI",
                        color = Color.White.copy(alpha = 0.88f),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = if (loading) {
                            "Memuat saldo..."
                        } else {
                            rupiahDouble(balance)
                        },
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                TextButton(onClick = onRefresh) {
                    Text(
                        "Muat ulang",
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            HorizontalDivider(
                color = Color.White.copy(alpha = 0.28f)
            )

            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.ArrowUpward,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.size(17.dp)
                        )

                        Spacer(Modifier.width(5.dp))

                        Text(
                            "Pemasukan",
                            color = Color.White.copy(alpha = 0.9f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(Modifier.height(5.dp))

                    Text(
                        rupiahDouble(totalIncome),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.ArrowDownward,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.size(17.dp)
                        )

                        Spacer(Modifier.width(5.dp))

                        Text(
                            "Pengeluaran",
                            color = Color.White.copy(alpha = 0.9f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(Modifier.height(5.dp))

                    Text(
                        rupiahDouble(totalExpense),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            if (error != null) {
                Spacer(Modifier.height(10.dp))
                Text(
                    error,
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun TargetScreen(
    items: List<FinancialTarget>,
    balance: Double,
    refresh: () -> Unit,
    loading: Boolean,
    error: String?
) {
    val scope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }
    var editingTarget by remember { mutableStateOf<FinancialTarget?>(null) }
    var actionError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FinanceBlueBackground)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    "Target Keuangan",
                    color = FinanceBlueDark,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    "Rencanakan tabungan untuk tujuanmu.",
                    color = FinanceMuted,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        Color.White.copy(alpha = 0.8f),
                        RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Savings,
                    contentDescription = null,
                    tint = FinanceBlue,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Button(
            onClick = {
                editingTarget = null
                actionError = null
                showDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = FinanceBlue
            )
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(
                "Tambah Target",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(14.dp))

        actionError?.let {
            Text(
                it,
                color = Color(0xFFB42318),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        when {
            loading -> Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = FinanceBlue)
            }

            error != null -> Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(error, color = Color(0xFFB42318))

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = refresh,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FinanceBlue
                    )
                ) {
                    Text("Coba lagi")
                }
            }

            items.isEmpty() -> Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    Modifier
                        .size(80.dp)
                        .background(
                            Color.White.copy(alpha = 0.8f),
                            RoundedCornerShape(24.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Savings,
                        contentDescription = null,
                        tint = FinanceBlue,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(Modifier.height(14.dp))

                Text(
                    "Belum ada target",
                    color = FinanceBlueDark,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    "Buat target untuk mulai menabung.",
                    color = FinanceMuted
                )
            }

            else -> LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(
                    top = 2.dp,
                    bottom = 20.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items, key = { it.id }) { target ->
                    val currentSavings = balance.coerceAtLeast(0.0)
                    val progress = if (target.targetAmount > 0L) {
                        (currentSavings / target.targetAmount.toDouble())
                            .toFloat()
                            .coerceIn(0f, 1f)
                    } else {
                        0f
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 3.dp
                        )
                    ) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    Modifier
                                        .size(46.dp)
                                        .background(
                                            FinanceBlueBackground,
                                            RoundedCornerShape(14.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Savings,
                                        contentDescription = null,
                                        tint = FinanceBlue,
                                        modifier = Modifier.size(25.dp)
                                    )
                                }

                                Spacer(Modifier.width(12.dp))

                                Column(Modifier.weight(1f)) {
                                    Text(
                                        target.name,
                                        color = FinanceBlueDark,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    if (!target.deadline.isNullOrBlank()) {
                                        Spacer(Modifier.height(3.dp))

                                        Text(
                                            "Tenggat: ${target.deadline}",
                                            color = FinanceMuted,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }

                                IconButton(
                                    onClick = {
                                        editingTarget = target
                                        actionError = null
                                        showDialog = true
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Edit target",
                                        tint = FinanceBlue
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        scope.launch {
                                            try {
                                                Api.service.deleteTarget(target.id)
                                                refresh()
                                            } catch (e: Exception) {
                                                actionError =
                                                    "Gagal menghapus target: ${e.message ?: "Periksa server."}"
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Hapus target",
                                        tint = FinanceRed
                                    )
                                }
                            }

                            Spacer(Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Tabungan terkumpul",
                                    color = FinanceMuted,
                                    style = MaterialTheme.typography.bodySmall
                                )

                                Text(
                                    "${(progress * 100).toInt()}%",
                                    color = FinanceBlue,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }

                            Spacer(Modifier.height(7.dp))

                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(9.dp),
                                color = FinanceBlue,
                                trackColor = FinanceBlueBackground,
                                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                            )

                            Spacer(Modifier.height(12.dp))

                            Text(
                                rupiahDouble(currentSavings),
                                color = FinanceBlue,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold
                            )

                            Spacer(Modifier.height(2.dp))

                            Text(
                                "dari ${rupiah(target.targetAmount)}",
                                color = FinanceMuted,
                                style = MaterialTheme.typography.bodySmall
                            )

                            if (!target.description.isNullOrBlank()) {
                                Spacer(Modifier.height(8.dp))

                                Text(
                                    target.description!!,
                                    color = FinanceMuted,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            Spacer(Modifier.height(14.dp))
                            FinancialInsightCard(target = target, currentBalance = balance)
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        TargetDialog(
            oldTarget = editingTarget,
            onDismiss = {
                showDialog = false
                editingTarget = null
            },
            onSave = { name, targetAmount, deadline, description ->
                scope.launch {
                    try {
                        val old = editingTarget

                        val target = FinancialTarget(
                            id = old?.id ?: 0L,
                            name = name,
                            targetAmount = targetAmount,
                            savedAmount = balance.coerceAtLeast(0.0).toLong(),
                            deadline = deadline,
                            description = description
                        )

                        if (old == null) {
                            Api.service.addTarget(target)
                        } else {
                            Api.service.updateTarget(old.id, target)
                        }

                        showDialog = false
                        editingTarget = null
                        refresh()
                    } catch (e: Exception) {
                        actionError =
                            "Gagal menyimpan target: ${e.message ?: "Periksa server."}"
                    }
                }
            }
        )
    }
}


/**
 * Analisis berjalan sepenuhnya di perangkat (tanpa API/layanan cloud).
 * Skor berbasis aturan lokal menggunakan progres, sisa nominal, dan waktu.
 * Ini merupakan estimasi heuristik, bukan model yang dilatih dari data historis.
 */
@Composable
private fun FinancialInsightCard(target: FinancialTarget, currentBalance: Double) {
    val today = remember { java.time.LocalDate.now() }
    val deadlineDate = remember(target.deadline) {
        try {
            target.deadline?.takeIf { it.isNotBlank() }?.let {
                java.time.LocalDate.parse(it)
            }
        } catch (_: Exception) { null }
    }
    val targetAmount = target.targetAmount.coerceAtLeast(0L)
    val saved = currentBalance.coerceAtLeast(0.0).toLong()
    val remaining = (targetAmount - saved).coerceAtLeast(0L)
    val progress = if (targetAmount > 0L) {
        (saved.toDouble() / targetAmount.toDouble()).coerceIn(0.0, 1.0)
    } else 0.0
    val daysLeft = deadlineDate?.let {
        java.time.temporal.ChronoUnit.DAYS.between(today, it)
    }
    val monthsLeft = daysLeft?.let { kotlin.math.ceil(it.coerceAtLeast(1) / 30.4375).toInt().coerceAtLeast(1) }
    val monthlySaving = when {
        remaining == 0L -> 0L
        monthsLeft != null -> kotlin.math.ceil(remaining.toDouble() / monthsLeft).toLong()
        else -> kotlin.math.ceil(remaining.toDouble() / 6.0).toLong()
    }
    // Skor lokal sederhana: kombinasi progres dan kecukupan waktu.
    val requiredProgress = if (daysLeft != null && daysLeft > 0) {
        (1.0 - daysLeft.toDouble() / 365.0).coerceIn(0.0, 1.0)
    } else if (daysLeft != null && daysLeft <= 0) 1.0 else progress
    val riskScore = ((0.65 * (1.0 - progress) + 0.35 * (requiredProgress - progress).coerceAtLeast(0.0))
        .coerceIn(0.0, 1.0) * 100).toInt()
    val statusColor = when {
        remaining == 0L -> FinanceGreen
        daysLeft != null && daysLeft < 0 -> FinanceRed
        riskScore >= 65 -> FinanceRed
        riskScore >= 35 -> Color(0xFFB7791F)
        else -> FinanceGreen
    }
    val status = when {
        remaining == 0L -> "Target tercapai"
        daysLeft != null && daysLeft < 0 -> "Tenggat terlewati"
        riskScore >= 65 -> "Perlu perhatian"
        riskScore >= 35 -> "Perlu konsistensi"
        else -> "Progres cukup baik"
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7FAF8)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.padding(13.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Savings,
                    contentDescription = null,
                    tint = FinanceGreen,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(7.dp))
                Text(
                    "Financial Insight",
                    color = FinanceGreenDark,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(Modifier.weight(1f))
                Text(
                    status,
                    color = statusColor,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelSmall
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                when {
                    remaining == 0L -> "Hebat! Target tabungan ini sudah tercapai."
                    daysLeft != null && daysLeft < 0 ->
                        "Tenggat sudah lewat. Perbarui tenggat atau sesuaikan nominal tabungan agar rencana lebih realistis."
                    deadlineDate != null ->
                        "Sisa ${rupiah(remaining)} dalam ${daysLeft} hari. Rekomendasi sisihkan sekitar ${rupiah(monthlySaving)} per bulan."
                    else ->
                        "Sisa ${rupiah(remaining)}. Tanpa tenggat, estimasi awal menggunakan rencana 6 bulan: ${rupiah(monthlySaving)} per bulan."
                },
                color = FinanceNavy,
                style = MaterialTheme.typography.bodySmall
            )
            if (remaining > 0L) {
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Skor risiko rencana", color = FinanceMuted, style = MaterialTheme.typography.labelSmall)
                    Text("$riskScore/100", color = statusColor, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                }
                LinearProgressIndicator(
                    progress = { riskScore / 100f },
                    modifier = Modifier.fillMaxWidth().height(6.dp),
                    color = statusColor,
                    trackColor = Color(0xFFE2E8E4),
                    strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                )
                Spacer(Modifier.height(5.dp))
            }
        }
    }
}

@Composable
private fun TargetDialog(
    oldTarget: FinancialTarget?,
    onDismiss: () -> Unit,
    onSave: (String, Long, String, String) -> Unit
) {
    var name by remember(oldTarget) {
        mutableStateOf(oldTarget?.name ?: "")
    }

    var targetAmount by remember(oldTarget) {
        mutableStateOf(oldTarget?.targetAmount?.toLong()?.toString() ?: "")
    }

    var deadline by remember(oldTarget) {
        mutableStateOf(oldTarget?.deadline.orEmpty())
    }

    var description by remember(oldTarget) {
        mutableStateOf(oldTarget?.description.orEmpty())
    }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        titleContentColor = FinanceBlueDark,
        title = {
            Text(
                if (oldTarget == null) "Tambah Target" else "Edit Target",
                fontWeight = FontWeight.ExtraBold
            )
        },
        text = {
            Column(
                modifier = Modifier.heightIn(max = 420.dp),
                verticalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        errorMessage = null
                    },
                    label = { Text("Nama target") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(13.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FinanceBlue,
                        focusedLabelColor = FinanceBlue,
                        cursorColor = FinanceBlue
                    )
                )

                OutlinedTextField(
                    value = targetAmount,
                    onValueChange = {
                        targetAmount = it.filter(Char::isDigit)
                        errorMessage = null
                    },
                    label = { Text("Nominal target (Rp)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(13.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FinanceBlue,
                        focusedLabelColor = FinanceBlue,
                        cursorColor = FinanceBlue
                    )
                )

                OutlinedTextField(
                    value = deadline,
                    onValueChange = { deadline = it },
                    label = { Text("Tenggat (opsional)") },
                    placeholder = { Text("YYYY-MM-DD") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(13.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FinanceBlue,
                        focusedLabelColor = FinanceBlue,
                        cursorColor = FinanceBlue
                    )
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Deskripsi (opsional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(13.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FinanceBlue,
                        focusedLabelColor = FinanceBlue,
                        cursorColor = FinanceBlue
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
                    val targetValue = targetAmount.toLongOrNull()

                    when {
                        name.isBlank() ->
                            errorMessage = "Nama target wajib diisi."

                        targetValue == null || targetValue <= 0 ->
                            errorMessage = "Nominal target harus lebih dari 0."

                        else -> onSave(
                            name.trim(),
                            targetValue,
                            deadline.trim(),
                            description.trim()
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = FinanceBlue
                )
            ) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = FinanceBlue
                )
            ) {
                Text("Batal")
            }
        }
    )
}