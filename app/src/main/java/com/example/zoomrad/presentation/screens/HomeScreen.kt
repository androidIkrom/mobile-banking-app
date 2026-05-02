package com.example.zoomrad.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zoomrad.ui.theme.*
import com.example.zoomrad.R

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        // Fixed Top Section: AppBar and Balance Card
        TopAppBar()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { BalanceCard() }
            item { PointsAndCashbackRow() }
            item { QuickActionsRow() }
            item { PaymentsSection() }
            item { ServicesHeader() }
            item { ServicesGrid() }
            item { GeolocationBanner() }
            item { CurrencyRatesSection() }
        }
    }
}

@Composable
fun TopAppBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconContainer(R.drawable.ic_menu)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconContainer(R.drawable.ic_scanner)
            Spacer(modifier = Modifier.width(12.dp))
            BadgedIconContainer(R.drawable.ic_notify, "53")
        }
    }
}

@Composable
fun IconContainer(iconRes: Int) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(Color.White, CircleShape)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = Color.Black
        )
    }
}

@Composable
fun BadgedIconContainer(iconRes: Int, badgeCount: String) {
    Box(contentAlignment = Alignment.TopEnd) {
        IconContainer(iconRes)
        Box(
            modifier = Modifier
                .offset(x = 4.dp, y = (-4).dp)
                .background(NotificationRed, CircleShape)
                .padding(horizontal = 4.dp, vertical = 1.dp)
        ) {
            Text(
                text = badgeCount,
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun BalanceCard() {
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFFD6F5EF), Color(0xFFA7E6D9))
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Jami balans", color = Color.Black.copy(alpha = 0.7f), fontSize = 14.sp)
                    Icon(
                        painter = painterResource(R.drawable.ic_eye),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "446 885 so'm",
                    color = Color.Black,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(text = "Kartalarga o'tish >", color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
fun PointsAndCashbackRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.ic_points),
                contentDescription = null,
                tint = GreenPrimary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "2710.0 ballar", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
        }
        Text(text = "Keshbek balansi >", color = TextGray, fontSize = 14.sp)
    }
}

@Composable
fun QuickActionsRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        QuickActionCard("Omonat", R.drawable.ic_deposit, Modifier.weight(1f))
        QuickActionCard("Kredit", R.drawable.ic_deposit, Modifier.weight(1f))
    }
}

@Composable
fun QuickActionCard(title: String, iconRes: Int, modifier: Modifier) {
    Row(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(GreenPrimary, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
    }
}

@Composable
fun PaymentsSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        PaymentCard("Avtobus va Metro", "To'lov", R.drawable.ic_deposit, Modifier.weight(1f))
        PaymentCard("Ta'lim", "To'lov", R.drawable.ic_deposit, Modifier.weight(1f))
    }
}

@Composable
fun PaymentCard(title: String, subtitle: String, iconRes: Int, modifier: Modifier) {
    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(text = subtitle, color = TextGray, fontSize = 12.sp)
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Image(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun ServicesHeader() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Xizmatlar", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(text = "Barchasi", color = TextGray, fontSize = 14.sp)
    }
}

@Composable
fun ServicesGrid() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ServiceItem("Mening uyim", R.drawable.ic_deposit, Modifier.weight(1f))
        ServiceItem("Mening avtomobilim", R.drawable.ic_deposit, Modifier.weight(1f))
        ServiceItem("Davlat xizmatlari", R.drawable.ic_deposit, Modifier.weight(1f))
    }
}

@Composable
fun ServiceItem(title: String, imageRes: Int, modifier: Modifier) {
    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            minLines = 2,
            lineHeight = 14.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            modifier = Modifier.size(50.dp)
        )
    }
}

@Composable
fun GeolocationBanner() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Включите геолокацию", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(text = "Это необходимо для работы сервиса", color = TextGray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text("Yoqish", color = Color.White, fontSize = 13.sp)
                }
            }
            Image(
                painter = painterResource(R.drawable.ic_deposit),
                contentDescription = null,
                modifier = Modifier.size(70.dp)
            )
        }
    }
}

@Composable
fun CurrencyRatesSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Valyuta kursi", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "Barchasi", color = TextGray, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Header
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Valyuta", color = TextGray, fontSize = 12.sp, modifier = Modifier.weight(1f))
            Text("Sotib olish", color = TextGray, fontSize = 12.sp)
            Spacer(modifier = Modifier.width(24.dp))
            Text("Sotish", color = TextGray, fontSize = 12.sp)
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = BorderColor, thickness = 0.5.dp)

        CurrencyItem("EUR", "13 500", "14 500", R.drawable.ic_deposit)
        CurrencyItem("USD", "12 080", "12 150", R.drawable.ic_deposit, "10 ↓", "20 ↓")

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Oxirgi yangilanish: 14.04.2026", color = TextGray, fontSize = 11.sp)
    }
}

@Composable
fun CurrencyItem(code: String, buy: String, sell: String, flagRes: Int, buyDiff: String? = null, sellDiff: String? = null) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painterResource(flagRes), null, modifier = Modifier.size(28.dp).clip(CircleShape))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = code, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))

        Column(horizontalAlignment = Alignment.End) {
            Text(text = buy, fontWeight = FontWeight.Bold)
            if (buyDiff != null) Text(text = buyDiff, color = Color.Red, fontSize = 10.sp)
        }
        Spacer(modifier = Modifier.width(24.dp))
        Column(horizontalAlignment = Alignment.End) {
            Text(text = sell, fontWeight = FontWeight.Bold)
            if (sellDiff != null) Text(text = sellDiff, color = Color.Red, fontSize = 10.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
