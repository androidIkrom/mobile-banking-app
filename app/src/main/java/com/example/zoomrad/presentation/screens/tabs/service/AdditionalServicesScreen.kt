package com.example.zoomrad.presentation.screens.tabs.service

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.entity.model.loan.LoanData
import com.example.presenter.vm.loan.LoanViewModel
import com.example.zoomrad.R
import com.example.zoomrad.presentation.screens.tabs.home.formatAmount
import java.util.Locale

data class AdditionalServiceItemData(
    val title: String,
    val iconDescription: String
)

@Composable
fun AdditionalServicesScreen(
    viewModel: LoanViewModel = hiltViewModel()
) {
    val state by viewModel.container.stateFlow.collectAsState()

    val services = listOf(
        AdditionalServiceItemData("Onlayn to'lov Alipay+", "Alipay+ logo icon"),
        AdditionalServiceItemData("Kids Card", "Child face icon"),
        AdditionalServiceItemData("Jamg'arma", "Treasure box icon"),
        AdditionalServiceItemData("Mastercard MoneySend", "Mastercard logo"),
        AdditionalServiceItemData("Investitsiya", "Growth chart icon"),
        AdditionalServiceItemData("Sayohat sug'urtasi", "Airplane icon"),
        AdditionalServiceItemData("Identifikatsiya", "User identity scan icon"),
        AdditionalServiceItemData("Kartalarim", "Credit cards icon"),
        AdditionalServiceItemData("Monitoring", "Clock history icon"),
        AdditionalServiceItemData("To'lovlar", "Wallet icon"),
        AdditionalServiceItemData("O'tkazma", "Money transfer logout icon"),
        AdditionalServiceItemData("Mening uyim", "House icon"),
        AdditionalServiceItemData("Mening avtomobilim", "Car icon"),
        AdditionalServiceItemData("Face Pay", "Face recognition scan icon"),
        AdditionalServiceItemData("Visa Direct", "Visa logo icon")
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Qo'shimcha",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.List,
                contentDescription = "Menu icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        AdditionalServicesSearchBar()

        Spacer(modifier = Modifier.height(24.dp))

        if (state.loans.isNotEmpty()) {
            Text(
                text = "Mening kreditlarim",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            state.loans.forEach { loan ->
                LoanCard(loan)
                Spacer(modifier = Modifier.height(10.dp))
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        Text(
            text = "Xizmatlar",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(services) { service ->
                AdditionalServiceGridCard(service)
            }
        }
    }
}

@Composable
fun LoanCard(loan: LoanData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Kredit #${loan.id.take(8).uppercase(Locale.getDefault())}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Surface(
                    color = if (loan.status == "ACTIVE") Color(0xFFE8F5E9) else Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (loan.status == "ACTIVE") "Faol" else "Yopilgan",
                        color = if (loan.status == "ACTIVE") Color(0xFF2E7D32) else Color.Gray,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Qolgan qarz", color = Color.Gray, fontSize = 12.sp)
                    Text(
                        text = formatAmount(loan.remaining),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    Text(text = "Keyingi to'lov", color = Color.Gray, fontSize = 12.sp)
                    Text(
                        text = loan.nextDueDate,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { (loan.totalAmount - loan.remaining).toFloat() / loan.totalAmount },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Jami: ${formatAmount(loan.totalAmount)}",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
                Text(
                    text = "${loan.termMonths} oy",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
        }
    }
}


@Composable
fun AdditionalServicesSearchBar() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search icon",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Xizmat nomi",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun AdditionalServiceGridCard(service: AdditionalServiceItemData) {
    Card(
        modifier = Modifier
            .aspectRatio(0.85f)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(18.dp),
                clip = false
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),MaterialTheme.colorScheme.primary)
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "Icon",
                    color = Color.White,
                    fontSize = 10.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = service.title,
                fontSize = 11.sp,
                lineHeight = 13.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AdditionalServicesScreenPreview() {
    AdditionalServicesScreen()
}
