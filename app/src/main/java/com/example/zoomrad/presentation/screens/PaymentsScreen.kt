package com.example.zoomrad.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zoomrad.ui.theme.*

/**
 * Data class for the UI elements in the Payments LazyGrid
 */
data class ServiceItemData(
    val title: String,
    val iconDescription: String
)

@Composable
fun PaymentsScreen() {
    val services = listOf(
        ServiceItemData("Mobil operatorlar", "Smartphone icon"),
        ServiceItemData("Internet provayderlar", "Wifi icon"),
        ServiceItemData("Uy telefoni", "Landline phone icon"),
        ServiceItemData("Kommunal xizmatlar", "House icon"),
        ServiceItemData("Transport", "Car icon"),
        ServiceItemData("Televidenie va onlayn uzatuv", "TV icon"),
        ServiceItemData("Bank kreditlarini so'ndirish", "Bank icon with arrow"),
        ServiceItemData("Davlat xizmatlari", "Government building icon"),
        ServiceItemData("Xayriya", "Heart in hands icon"),
        ServiceItemData("Internet do'konlar", "Shopping basket icon"),
        ServiceItemData("E'lonlar va reklama", "List/Ads icon"),
        ServiceItemData("Xizmatlar", "Checklist icon")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9F8)) // Exact light grey background
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // "To'lovlar" Title
        Text(
            text = "To'lovlar",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Search Bar Component
        PaymentsSearchBar()

        Spacer(modifier = Modifier.height(24.dp))

        // "Barcha xizmatlar" Header
        Text(
            text = "Barcha xizmatlar",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        // LazyVerticalGrid for services
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(services) { service ->
                ServiceGridCard(service)
            }
        }
    }
}

@Composable
fun PaymentsSearchBar() {
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
                text = "Qidiruv",
                color = Color(0xFFB2B2B2),
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun ServiceGridCard(service: ServiceItemData) {
    Card(
        modifier = Modifier
            .aspectRatio(0.9f)
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
            // Gradient Circle for Icon
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
                Text(
                    text = "Icon",
                    color = Color.White,
                    fontSize = 10.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Service Name Text
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

@Preview(showBackground = true)
@Composable
fun PaymentsScreenPreview() {
    PaymentsScreen()
}
