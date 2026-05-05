package com.example.zoomrad.presentation.screens.tabs.monitor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


data class MonitoringTransactionData(
    val id: String,
    val title: String,
    val subtitle: String,
    val amount: String,
    val time: String,
    val isIncome: Boolean,
    val date: String
)


data class MonitoringHeaderSummary(
    val incomeAmount: String,
    val expenseAmount: String,
    val dayIncome: String,
    val dayExpense: String,
    val date: String
)

@Composable
fun MonitoringScreen() {
    val summary = MonitoringHeaderSummary(
        incomeAmount = "2 014 670",
        expenseAmount = "2 065 530",
        dayIncome = "+ 1 000 000",
        dayExpense = "- 1 006 800",
        date = "30.04.2026"
    )

    val transactions = listOf(
        MonitoringTransactionData("1", "ATTO_POS", "491699***9319", "- 1 700", "30-aprel, 22:26", false, "30.04.2026"),
        MonitoringTransactionData("2", "ATTO_POS", "491699***9319", "- 1 700", "30-aprel, 21:06", false, "30.04.2026"),
        MonitoringTransactionData("3", "YATT RAXIMDJANOVA SHOIRA", "491699***9319", "- 1 000 000", "30-aprel, 20:21", false, "30.04.2026"),
        MonitoringTransactionData("4", "ATTO_POS", "491699***9319", "- 1 700", "30-aprel, 13:19", false, "30.04.2026"),
        MonitoringTransactionData("5", "ATTO_POS", "491699***9319", "- 1 700", "30-aprel, 07:35", false, "30.04.2026"),
        MonitoringTransactionData("6", "PAYME*MARGUBA S", "491699***9319", "+ 1 000 000", "30-aprel, 00:00", true, "30.04.2026"),
        MonitoringTransactionData("7", "ATTO_POS", "491699***9319", "- 1 700", "27-aprel, 18:24", false, "27.04.2026"),
        MonitoringTransactionData("8", "ATTO_POS", "491699***9319", "- 1 700", "27-aprel, 12:59", false, "27.04.2026")
    )

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
            // Main Screen Header: Monitoring, Tarix and Filter Icon
            item(span = { GridItemSpan(2) }) {
                MonitoringHeader()
            }

            // Summary Cards: Tushumlar (Green) and Chiqimlar (Red)
            item {
                SummaryCard(
                    amount = summary.incomeAmount,
                    label = "Tushumlar",
                    isIncome = true
                )
            }
            item {
                SummaryCard(
                    amount = summary.expenseAmount,
                    label = "Chiqimlar",
                    isIncome = false
                )
            }

            // Transaction groups separated by date
            val groupedTransactions = transactions.groupBy { it.date }
            groupedTransactions.forEach { (date, items) ->
                // Date separator with daily totals
                item(span = { GridItemSpan(2) }) {
                    DateSeparatorHeader(
                        date = date,
                        income = summary.dayIncome,
                        expense = if (date == "30.04.2026") summary.dayExpense else "- 207 100" // Example value for other dates
                    )
                }

                // List of transaction cards for this date (full width)
                items(items, span = { GridItemSpan(2) }) { transaction ->
                    TransactionItemCard(transaction)
                }
            }

            // Extra padding at the bottom for scrolling
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
            text = "Monitoring",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Tarix",
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
