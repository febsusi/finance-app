package com.example.financeapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Locale

@Composable
fun FinanceSummaryCard(
    totalIncome: Double,
    totalExpense: Double
) {
    val balance = totalIncome - totalExpense
    val currency = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF173D32)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Saldo saat ini",
                color = Color(0xFFD1E7DA)
            )

            Text(
                text = currency.format(balance),
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )

            HorizontalDivider(color = Color(0xFF547567))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Pemasukan",
                        color = Color(0xFFD1E7DA),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = currency.format(totalIncome),
                        color = Color(0xFFA9E7B9),
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Pengeluaran",
                        color = Color(0xFFD1E7DA),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = currency.format(totalExpense),
                        color = Color(0xFFFFD0B9),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

