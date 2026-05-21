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
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.entity.model.loan.LoanData
import com.example.presenter.vm.loan.LoanViewModel
import com.example.zoomrad.R
import com.example.zoomrad.presentation.navigation.BottomNavItem
import com.example.zoomrad.presentation.screens.tabs.home.formatAmount
import com.example.zoomrad.presentation.screens.tabs.home.getServiceIcon
import java.util.Locale

data class AdditionalServiceItemData(
    val titleRes: Int,
    val iconDescription: String,
    val route: String? = null,
    val icon: ImageVector = Icons.Default.Search
)

val allServices = listOf(
    AdditionalServiceItemData(R.string.service_online_payment, "Alipay+ logo icon"),
    AdditionalServiceItemData(R.string.service_kids_card, "Child face icon"),
    AdditionalServiceItemData(R.string.service_savings, "Treasure box icon"),
    AdditionalServiceItemData(R.string.service_moneysend, "Mastercard logo"),
    AdditionalServiceItemData(R.string.service_investment, "Growth chart icon"),
    AdditionalServiceItemData(R.string.service_travel_insurance, "Airplane icon"),
    AdditionalServiceItemData(R.string.service_identification, "User identity scan icon", "kyc"),
    AdditionalServiceItemData(R.string.service_get_loan, "Loan icon", "apply_loan"),
    AdditionalServiceItemData(R.string.service_my_cards, "Credit cards icon", "cards"),
    AdditionalServiceItemData(R.string.service_monitoring, "Clock history icon", BottomNavItem.Monitoring.route),
    AdditionalServiceItemData(R.string.service_payments, "Wallet icon", BottomNavItem.Tolovlar.route),
    AdditionalServiceItemData(R.string.service_transfer, "Money transfer logout icon", BottomNavItem.Otkazma.route),
    AdditionalServiceItemData(R.string.service_my_home, "House icon"),
    AdditionalServiceItemData(R.string.service_my_car, "Car icon"),
    AdditionalServiceItemData(R.string.service_face_pay, "Face recognition scan icon"),
    AdditionalServiceItemData(R.string.service_visa_direct, "Visa logo icon")
)

@Composable
fun AdditionalServicesScreen(
    navController: NavController,
    viewModel: LoanViewModel = hiltViewModel()
) {
    val state by viewModel.container.stateFlow.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val services = allServices
        .filter { stringResource(it.titleRes).contains(searchQuery, ignoreCase = true) }
        .sortedByDescending { it.route != null }
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
                text = stringResource(R.string.services_additional_title),
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

        AdditionalServicesSearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (state.loans.isNotEmpty()) {
            Text(
                text = stringResource(R.string.services_my_loans),
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
            text = stringResource(R.string.services),
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
                    text = stringResource(R.string.loan_id_label, loan.id.take(8).uppercase(Locale.getDefault())),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Surface(
                    color = if (loan.status == "ACTIVE") Color(0xFFE8F5E9) else Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (loan.status == "ACTIVE") stringResource(R.string.loan_status_active) else stringResource(R.string.loan_status_closed),
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
                    Text(text = stringResource(R.string.loan_remaining_debt), color = Color.Gray, fontSize = 12.sp)
                    Text(
                        text = formatAmount(loan.remaining),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    Text(text = stringResource(R.string.loan_next_payment), color = Color.Gray, fontSize = 12.sp)
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
                Text(stringResource(R.string.pay_button), fontWeight = FontWeight.Bold)
            }
        }
    }
}


@Composable
fun AdditionalServicesSearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
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
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.weight(1f),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { innerTextField ->
                    if (query.isEmpty()) {
                        Text(
                            text = stringResource(R.string.services_search_placeholder),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 16.sp
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}

@Composable
fun AdditionalServiceListCard(service: AdditionalServiceItemData, onClick: () -> Unit) {
    val titleStr = stringResource(service.titleRes)
    val fallbackIcon = painterResource(remember(service.titleRes) { getServiceIcon(service.titleRes) })
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
                AsyncImage(
                    contentDescription = null,
                    error = fallbackIcon,
                    colorFilter = ColorFilter.tint(Color.White),
                    modifier = Modifier.size(24.dp),
                    model = "",
                    placeholder = fallbackIcon,
                    fallback = fallbackIcon,
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = titleStr,
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
