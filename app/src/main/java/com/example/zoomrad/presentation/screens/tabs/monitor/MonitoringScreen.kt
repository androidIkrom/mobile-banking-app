package com.example.zoomrad.presentation.screens.tabs.monitor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.presenter.vm.transaction.TransactionViewModel
import com.example.zoomrad.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class MonitoringTransactionData(
    val id: String,
    val title: String,
    val subtitle: String,
    val amount: String,
    val time: String,
    val isIncome: Boolean,
    val date: String,
    val rawAmount: Long
)

@Composable
fun MonitoringScreen(
    viewModel: TransactionViewModel = hiltViewModel()
) {
    val apiTransactions by viewModel.transactions.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchTransactions()
    }

    val transactions = apiTransactions.map { api ->
        val isIncome = api.type == "TRANSFER_IN" || api.type == "LOAN_DISBURSEMENT"
        val amountPrefix = if (isIncome) "+ " else "- "
        
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val dateOutputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val timeOutputFormat = SimpleDateFormat("d-MMMM, HH:mm", Locale.getDefault())
        
        val dateObj = try { inputFormat.parse(api.createdAt) } catch (_: Exception) { Date() }

        MonitoringTransactionData(
            id = api.id,
            title = api.type.replace("_", " "),
            subtitle = api.description,
            amount = amountPrefix + String.format(Locale.getDefault(), "%,d", api.amount.toLong()).replace(',', ' '),
            time = timeOutputFormat.format(dateObj ?: Date()),
            isIncome = isIncome,
            date = dateOutputFormat.format(dateObj ?: Date()),
            rawAmount = api.amount.toLong()
        )
    }

    val totalIncome = remember(apiTransactions) {
        apiTransactions.filter { it.type == "TRANSFER_IN" || it.type == "LOAN_DISBURSEMENT" }.sumOf { it.amount }
    }
    val totalExpense = remember(apiTransactions) {
        apiTransactions.filter { it.type != "TRANSFER_IN" && it.type != "LOAN_DISBURSEMENT" }.sumOf { it.amount }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                MonitoringHeader()
            }

            if (isLoading && transactions.isEmpty()) {
                item(span = { GridItemSpan(2) }) {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                item {
                    SummaryCard(
                        amount = String.format(Locale.getDefault(), "%,d", totalIncome.toLong()).replace(',', ' '),
                        label = stringResource(R.string.monitor_incomes),
                        isIncome = true
                    )
                }
                item {
                    SummaryCard(
                        amount = String.format(Locale.getDefault(), "%,d", totalExpense.toLong()).replace(',', ' '),
                        label = stringResource(R.string.monitor_expenses),
                        isIncome = false
                    )
                }

                val groupedTransactions = transactions.groupBy { it.date }
                groupedTransactions.forEach { (date, items) ->
                    val dayIncome = items.filter { it.isIncome }.sumOf { it.rawAmount }
                    val dayExpense = items.filter { !it.isIncome }.sumOf { it.rawAmount }

                    item(span = { GridItemSpan(2) }) {
                        DateSeparatorHeader(
                            date = date,
                            income = "+ " + String.format(Locale.getDefault(), "%,d", dayIncome).replace(',', ' '),
                            expense = "- " + String.format(Locale.getDefault(), "%,d", dayExpense).replace(',', ' ')
                        )
                    }

                    items(items, span = { GridItemSpan(2) }) { transaction ->
                        TransactionItemCard(transaction)
                    }
                }
            }

            item(span = { GridItemSpan(2) }) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun MonitoringHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.monitor_title),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = stringResource(R.string.monitor_history),
            fontSize = 26.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.weight(1f))
        
        Surface(
            modifier = Modifier.size(42.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "▽", 
                    fontSize = 20.sp, 
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun SummaryCard(amount: String, label: String, isIncome: Boolean) {
    val bgColor = if (isIncome) Color(0xFF33B585) else Color(0xFFD85F5F)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(82.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(36.dp),
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.2f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = if (isIncome) "↓" else "↑",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = amount,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = label,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun DateSeparatorHeader(date: String, income: String, expense: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = date,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium
        )
        Row {
            Text(
                text = income,
                fontSize = 14.sp,
                color = Color(0xFF33B585),
                fontWeight = FontWeight.Medium
            )
            Text(
                text = " / ",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = expense,
                fontSize = 14.sp,
                color = Color(0xFFD85F5F),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun TransactionItemCard(transaction: MonitoringTransactionData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(18.dp),
                clip = false
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val statusColor = if (transaction.isIncome) Color(0xFF33B585) else Color(0xFFD85F5F)
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(statusColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (transaction.isIncome) "↓" else "↑",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.width(14.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
                Text(
                    text = transaction.subtitle,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = transaction.time,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = transaction.amount,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (transaction.isIncome) Color(0xFF33B585) else Color(0xFFD85F5F)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MonitoringScreenPreview() {
    MonitoringScreen()
}
