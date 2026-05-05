package com.example.zoomrad.presentation.screens.tabs.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.zoomrad.ui.theme.*
import com.example.zoomrad.R
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavController, onMenuClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(onMenuClick)
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
fun DrawerContent(
    onItemClick: (String) -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF003D2B), Color(0xFF007958))
                    ),
                    shape = RoundedCornerShape(bottomEnd = 40.dp)
                )
                .padding(24.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        tint = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "IKROMBEK OBLOQULOV",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "+998950663020",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = Color(0xFF19B387),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Tasdiqlangan mijoz",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val menuItems = listOf(
            DrawerItemData("Profil", Icons.Default.Person),
            DrawerItemData("Sozlamalar", Icons.Default.Settings),
            DrawerItemData("Xavfsizlik", Icons.Default.Lock),
            DrawerItemData("Mening arizalarim", Icons.Default.Info), // Placeholder as requested
            DrawerItemData("Yordam", Icons.Default.Info), // Placeholder as requested
            DrawerItemData("Dasturni ulashing", Icons.Default.Share),
            DrawerItemData("Ilova haqida", Icons.Default.Info)
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(menuItems) { item ->
                DrawerItem(item) {
                    onItemClick(item.title)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onLogoutClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFFFFEDED), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Chiqish",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Versiya 2.1.893 (893 liveGoogle)",
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                fontSize = 12.sp
            )
        }
    }
}

data class DrawerItemData(val title: String, val icon: ImageVector)

@Composable
fun DrawerItem(item: DrawerItemData, onClick: () -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(horizontal = 24.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = item.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(start = 80.dp, end = 24.dp),
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
        )
    }
}

@Composable
fun TopAppBar(onMenuClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconContainer(R.drawable.ic_menu) {
            onMenuClick()
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconContainer(R.drawable.ic_scanner)
            Spacer(modifier = Modifier.width(12.dp))
            BadgedIconContainer(R.drawable.ic_notify, "53")
        }
    }
}

@Composable
fun IconContainer(iconRes: Int, onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
            .clickable { onClick() }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurface,
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
                        colors = listOf(
                            MaterialTheme.colorScheme.secondaryContainer,
                            MaterialTheme.colorScheme.onSecondaryContainer
                        )
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
                    Text(
                        text = "Jami balans",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_eye),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "446 885 so'm",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(
                        text = "Kartalarga o'tish >",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
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
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
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
            Text(
                text = "2710.0 ballar",
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = "Keshbek balansi >",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp
        )
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
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
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
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
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
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(text = subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Xizmatlar",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Barchasi",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp
        )
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
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            minLines = 2,
            lineHeight = 14.sp,
            color = MaterialTheme.colorScheme.onSurface
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Включите геолокацию",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Это необходимо для работы сервиса",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
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
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Valyuta kursi",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Barchasi",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Header
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                "Valyuta",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                "Sotib olish",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.width(24.dp))
            Text("Sotish", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.outline,
            thickness = 0.5.dp
        )

        CurrencyItem("EUR", "13 500", "14 500", R.drawable.ic_deposit)
        CurrencyItem("USD", "12 080", "12 150", R.drawable.ic_deposit, "10 ↓", "20 ↓")

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Oxirgi yangilanish: 14.04.2026",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 11.sp
        )
    }
}

@Composable
fun CurrencyItem(
    code: String,
    buy: String,
    sell: String,
    flagRes: Int,
    buyDiff: String? = null,
    sellDiff: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(flagRes), null, modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = code,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface
        )

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = buy,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (buyDiff != null) Text(text = buyDiff, color = Color.Red, fontSize = 10.sp)
        }
        Spacer(modifier = Modifier.width(24.dp))
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = sell,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (sellDiff != null) Text(text = sellDiff, color = Color.Red, fontSize = 10.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController) {

    }
}
