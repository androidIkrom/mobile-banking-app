package com.example.zoomrad.presentation.screens

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zoomrad.R

/**
 * Data class for the UI elements in the LazyGrid for Additional Services
 */
data class AdditionalServiceItemData(
    val title: String,
    val iconDescription: String
)

@Composable
fun AdditionalServicesScreen() {
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

    Scaffold(
        bottomBar = { AdditionalServicesBottomNavigationBar() },
        containerColor = Color(0xFFF7F9F8) // Light grey background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Header Section: Title and Menu Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Qo'shimcha",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = "Menu icon",
                    tint = Color(0xFF00A67E),
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Search Bar Component
            AdditionalServicesSearchBar()

            Spacer(modifier = Modifier.height(24.dp))

            // LazyVerticalGrid for services (3 columns as seen in image)
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
}

@Composable
fun AdditionalServicesSearchBar() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
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
                tint = Color(0xFFB2B2B2),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Xizmat nomi",
                color = Color(0xFFB2B2B2),
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
                clip = false,
                ambientColor = Color.Black.copy(alpha = 0.1f),
                spotColor = Color.Black.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Green Gradient Circle for Icon
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF2EBD96), Color(0xFF00A67E))
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                // ICON PLACEHOLDER: as requested, I'll use a description placeholder
                /* 
                   Add actual icons here like:
                   Icon(
                       painter = painterResource(id = R.drawable.your_icon),
                       contentDescription = service.iconDescription,
                       tint = Color.White,
                       modifier = Modifier.size(28.dp)
                   )
                */
                Text(
                    text = "Icon",
                    color = Color.White,
                    fontSize = 10.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Service Title
            Text(
                text = service.title,
                fontSize = 11.sp,
                lineHeight = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}

@Composable
fun AdditionalServicesBottomNavigationBar() {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.height(80.dp)
    ) {
        val navItems = listOf(
            Triple("Asosiy", "Home icon", false),
            Triple("To'lovlar", "Payments icon", false),
            Triple("O'tkazma", "Transfer icon", false),
            Triple("Monitoring", "Monitoring icon", false),
            Triple("Xizmatlar", "Services icon", true) // Selected for this screen
        )

        navItems.forEach { (label, iconDesc, isSelected) ->
            NavigationBarItem(
                icon = {
                    // Icon Placeholder
                    Icon(
                        painter = painterResource(id = R.drawable.ic_deposit), // Replace with actual assets
                        contentDescription = iconDesc,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(label, fontSize = 10.sp) },
                selected = isSelected,
                onClick = { },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF00A67E),
                    selectedTextColor = Color(0xFF00A67E),
                    unselectedIconColor = Color(0xFF757575),
                    unselectedTextColor = Color(0xFF757575),
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdditionalServicesScreenPreview() {
    AdditionalServicesScreen()
}
