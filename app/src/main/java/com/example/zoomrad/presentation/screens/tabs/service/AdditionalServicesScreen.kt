package com.example.zoomrad.presentation.screens.tabs.service

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.entity.model.loan.LoanData
import com.example.presenter.vm.loan.LoanViewModel
import com.example.zoomrad.presentation.navigation.BottomNavItem
import com.example.zoomrad.presentation.screens.tabs.home.formatAmount
import java.util.Locale

data class AdditionalServiceItemData(
    val title: String,
    val iconDescription: String,
    val route: String? = null,
    val icon: ImageVector = Icons.Default.Search
)

val allServices = listOf(
    AdditionalServiceItemData("Onlayn to'lov Alipay+", "Alipay+ logo icon"),
    AdditionalServiceItemData("Kids Card", "Child face icon"),
    AdditionalServiceItemData("Jamg'arma", "Treasure box icon"),
    AdditionalServiceItemData("Mastercard MoneySend", "Mastercard logo"),
    AdditionalServiceItemData("Investitsiya", "Growth chart icon"),
    AdditionalServiceItemData("Sayohat sug'urtasi", "Airplane icon"),
    AdditionalServiceItemData("Identifikatsiya", "User identity scan icon"),
    AdditionalServiceItemData("Kredit olish", "Loan icon", "apply_loan"),
    AdditionalServiceItemData("Kartalarim", "Credit cards icon", "cards"),
    AdditionalServiceItemData("Monitoring", "Clock history icon", BottomNavItem.Monitoring.route),
    AdditionalServiceItemData("To'lovlar", "Wallet icon", BottomNavItem.Tolovlar.route),
    AdditionalServiceItemData("O'tkazma", "Money transfer logout icon", BottomNavItem.Otkazma.route),
    AdditionalServiceItemData("Mening uyim", "House icon"),
    AdditionalServiceItemData("Mening avtomobilim", "Car icon"),
    AdditionalServiceItemData("Face Pay", "Face recognition scan icon"),
    AdditionalServiceItemData("Visa Direct", "Visa logo icon")
)

@Composable
fun AdditionalServicesScreen(
    navController: NavController,
    viewModel: LoanViewModel = hiltViewModel()
) {
    val state by viewModel.container.stateFlow.collectAsState()

    val services = allServices.sortedByDescending { it.route != null }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
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
                LoanCard(loan, onRepayClick = {
                    navController.navigate("repay_loan/${loan.id}/${loan.monthlyPayment}")
                })
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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            services.forEach { service ->
                AdditionalServiceListCard(service) {
                    service.route?.let { navController.navigate(it) }
                }
            }
        }
    }
}

@Composable
fun LoanCard(loan: LoanData, onRepayClick: () -> Unit) {
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
                progress = { (loan.totalAmount - loan.remaining).toFloat() / loan.totalAmount.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primaryContainer
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRepayClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("To'lash", fontWeight = FontWeight.Bold)
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
fun AdditionalServiceListCard(service: AdditionalServiceItemData, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (MaterialTheme.colorScheme.background == Color(0xFFF7F9F8))
                Color.White
            else
                Color(0xFF2D3843)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF33B585), Color(0xFF2E7D32))
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = service.icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = service.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            Surface(
                modifier = Modifier.size(32.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Navigate",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AdditionalServicesScreenPreview() {
    val navController = androidx.navigation.compose.rememberNavController()
    AdditionalServicesScreen(navController)
}
