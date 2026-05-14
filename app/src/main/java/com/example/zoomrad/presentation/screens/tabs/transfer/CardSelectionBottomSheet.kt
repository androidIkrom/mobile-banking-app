package com.example.zoomrad.presentation.screens.tabs.transfer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.entity.model.card.CardData
import com.example.zoomrad.presentation.screens.cards.BankCardItem
import com.example.zoomrad.ui.utils.dashedBorder
import androidx.compose.ui.tooling.preview.Preview
import com.example.zoomrad.ui.theme.ZoomradTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import java.util.Locale
import com.example.zoomrad.presentation.screens.cards.CardData as CardUiData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardSelectionBottomSheet(
    cards: List<CardData>,
    selectedCard: CardData?,
    onCardSelected: (CardData) -> Unit,
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    onAddCardClick: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Kartani tanlang",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismissRequest) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val filters = listOf("Barchasi", "Visa", "Uzcard", "Humo")
            var selectedFilter by remember { mutableStateOf("Barchasi") }

            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.onSurface,
                            selectedLabelColor = MaterialTheme.colorScheme.surface,
                            containerColor = Color.Transparent,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        border = null,
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val filteredCards = when (selectedFilter) {
                    "Barchasi" -> cards
                    else -> cards.filter { it.type.equals(selectedFilter, ignoreCase = true) }
                }

                items(filteredCards) { card ->
                    Box(modifier = Modifier.clickable { onCardSelected(card) }) {
                        BankCardItem(
                            card = CardUiData(
                                id = card.id,
                                bankName = if (card.type == "UZCARD") "Uzcard Bank" else "Humo Bank",
                                isPrimary = card.isMain,
                                cardNumber = card.maskedNumber,
                                cardHolderName = card.holderName,
                                balance = String.format(Locale.getDefault(), "%,d", card.balance.toLong()).replace(',', ' '),
                                currency = card.currency,
                                cardType = card.type,
                                backgroundUrl = card.backgroundUrl,
                                gradientColors = if (card.type == "HUMO")
                                    listOf(Color(0xFF0D47A1), Color(0xFF1976D2))
                                else
                                    listOf(Color(0xFF003D2B), Color(0xFF007958), Color(0xFF004D40))
                            )
                        )
                        if (card.id == selectedCard?.id) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Selected",
                                tint = Color(0xFF00A67E),
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(12.dp)
                                    .size(28.dp)
                                    .background(Color.White, CircleShape)
                            )
                        }
                    }
                }

                item {
                    AddCardDashedButton(onClick = onAddCardClick)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun CardSelectionBottomSheetPreview() {
    val cards = listOf(
        CardData(
            id = "1",
            maskedNumber = "4916 99** **** 9210",
            holderName = "Aloqa Baraka",
            expiry = "12/25",
            balance = 459603.00,
            currency = "UZS",
            isMain = true,
            isBlocked = false,
            type = "VISA",
            backgroundUrl = null
        ),
        CardData(
            id = "2",
            maskedNumber = "8600 12** **** 3456",
            holderName = "My Humo Card",
            expiry = "01/27",
            balance = 1200000.00,
            currency = "UZS",
            isMain = false,
            isBlocked = false,
            type = "HUMO",
            backgroundUrl = null
        )
    )
    ZoomradTheme {
        CardSelectionBottomSheet(
            cards = cards,
            selectedCard = cards[0],
            onCardSelected = {},
            onDismissRequest = {},
            sheetState = rememberModalBottomSheetState(),
            onAddCardClick = {}
        )
    }
}

@Composable
fun AddCardDashedButton(onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .dashedBorder(
                width = 1.dp,
                color = Color(0xFF00A67E).copy(alpha = 0.5f),
                shape = RoundedCornerShape(24.dp),
                on = 8.dp,
                off = 4.dp
            )
            .clickable { onClick() },
        color = Color.Transparent,
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = Color(0xFF00A67E),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Kartani qo'shing",
                color = Color(0xFF00A67E),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
