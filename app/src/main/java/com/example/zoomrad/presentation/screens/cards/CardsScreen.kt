package com.example.zoomrad.presentation.screens.cards

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.zoomrad.ui.theme.*

data class CardData(
    val id: String,
    val bankName: String,
    val bankLogo: ImageVector = Icons.Default.Info,
    val isPrimary: Boolean,
    val cardNumber: String,
    val cardHolderName: String,
    val balance: String,
    val currency: String,
    val cardType: String,
    val cardTypeLogo: ImageVector = Icons.Default.Info,
    val gradientColors: List<Color> = listOf(Color(0xFF004D40), Color(0xFF00796B))
)

data class AddCardOption(
    val title: String,
    val subtitle: String,
    val icon: ImageVector = Icons.Default.Info
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardsScreen(navController: NavController) {
    var showAddCardSheet by remember { mutableStateOf(false) } // Initially false
    val sheetState = rememberModalBottomSheetState()

    val cards = listOf(
        CardData(
            id = "1",
            bankName = "AloqaBank",
            isPrimary = true,
            cardNumber = "4916 99** **** 9319",
            cardHolderName = "Ikrombek Anvar o'g'li",
            balance = "89 055.00",
            currency = "UZS",
            cardType = "Visa",
            gradientColors = listOf(Color(0xFF003D2B), Color(0xFF007958), Color(0xFF004D40))
        )
    )

    val filters = listOf("Barchasi", "Visa", "Uzcard", "Humo")
    var selectedFilter by remember { mutableStateOf("Barchasi") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Kartalarim",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
//            Filter
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            selectedLabelColor = MaterialTheme.colorScheme.onSurface,
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        border = null,
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }

            // karta list
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(cards) { card ->
                    BankCardItem(card)
                }
            }

//            add
            Button(
                onClick = { showAddCardSheet = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenPrimary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(text = "Karta qo'shish", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }

        if (showAddCardSheet) {
            ModalBottomSheet(
                onDismissRequest = { showAddCardSheet = false },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surface,
                dragHandle = {
                    Box(
                        modifier = Modifier
                            .padding(vertical = 12.dp)
                            .width(40.dp)
                            .height(4.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
                    )
                }
            ) {
                AddCardContent(onClose = { showAddCardSheet = false })
            }
        }
    }
}

@Composable
fun BankCardItem(card: CardData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(card.gradientColors))
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 1.dp.toPx()
                val spacing = 15.dp.toPx()
                for (i in -20..40) {
                    drawLine(
                        color = Color.White.copy(alpha = 0.08f),
                        start = Offset(i * spacing, 0f),
                        end = Offset(i * spacing + size.height, size.height),
                        strokeWidth = strokeWidth
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = card.bankLogo,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = card.bankName,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    if (card.isPrimary) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.FavoriteBorder,
                                contentDescription = null,
                                tint = Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Asosiy",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 13.sp
                            )
                        }
                    }
                }
//                  Name, number
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = card.cardTypeLogo,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = card.cardNumber,
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 13.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Text(
                        text = card.cardHolderName,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 13.sp
                    )
                }

//                balans
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = card.balance,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = card.currency,
                        color = Color.White,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 3.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AddCardContent(onClose: () -> Unit) {
    val options = listOf(
        AddCardOption("Yangi karta", "Yangi karta qo'shish"),
        AddCardOption("Barcha Aloqabank kartalari", "Hammasini bir martada yuklang"),
        AddCardOption("Virtual karta", "Virtual karta ochish")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Karta qo'shish",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        options.forEach { option ->
            AddCardItem(option)
            Spacer(modifier = Modifier.height(12.dp))
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun AddCardItem(option: AddCardOption) {
    Surface(
        onClick = { /* Handle click */ },
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                 Icon(
                    imageVector = option.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = option.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = option.subtitle,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardsScreenPreview() {
    val navController = rememberNavController()
    QuoteReminderTheme(darkTheme = true) {
        CardsScreen(navController)
    }
}

@Preview(showBackground = true)
@Composable
fun LightCardsScreenPreview() {
    val navController = rememberNavController()
    QuoteReminderTheme(darkTheme = false) {
        CardsScreen(navController)
    }
}
