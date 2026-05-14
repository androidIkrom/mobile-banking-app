package com.example.zoomrad.presentation.screens.tabs.transfer

import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.entity.model.card.CardData
import com.example.presenter.vm.cards.CardViewModel
import com.example.presenter.vm.profile.ProfileViewModel
import com.example.presenter.vm.transfer.TransferViewModel
import com.example.zoomrad.ui.theme.ZoomradTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferScreen(
    navController: androidx.navigation.NavController,
    transferViewModel: TransferViewModel = hiltViewModel(),
    cardViewModel: CardViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val transferState by transferViewModel.collectAsState()
    val context = LocalContext.current
    val cards by cardViewModel.cards.collectAsState()
    val profileState by profileViewModel.collectAsState()

    transferViewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is TransferViewModel.TransferSideEffect.ShowSuccess -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }

            is TransferViewModel.TransferSideEffect.NavigateToConfirm -> {
                navController.navigate("transfer_otp")
            }

            is TransferViewModel.TransferSideEffect.ShowError -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()

            }

            else -> {}
        }
    }

    LaunchedEffect(Unit) {
        cardViewModel.fetchCards()
    }

    var showCardSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var selectedCard by remember { mutableStateOf<CardData?>(null) }

    LaunchedEffect(cards) {
        if (selectedCard == null && cards.isNotEmpty()) {
            selectedCard = cards.find { it.isMain } ?: cards.firstOrNull()
        }
    }
    var cardNumber by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Kartaga o'tkazma",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(Modifier.padding(bottom = 8.dp)) {
                TextField(
                    value = cardNumber,
                    onValueChange = {
                        if (it.length <= 16) {
                            cardNumber = it
                            transferViewModel.checkCard(cardNumber)
                        }
                    },
                    placeholder = { Text("Karta raqami", color = Color.Gray, fontSize = 16.sp) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = Color(0xFF00A67E),
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        }
        transferState.recipientName?.let { name ->
            Text(
                text = name,
                color = if (name == "Topilmadi") Color.Red else MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 8.dp, start = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "O'tkazma summasi",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(10.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            TextField(
                value = amount,
                onValueChange = { if (it.all { char -> char.isDigit() }) amount = it },
                placeholder = { Text("0 so'm", color = Color.Gray, fontSize = 12.sp) },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFF00A67E),
                        modifier = Modifier.size(24.dp)
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color(0xFF00A67E)
                ),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ShortcutButton(
                modifier = Modifier.weight(1f),
                text = "Mening kartamga\no'tkazma",
                icon = Icons.Default.Info
            )
            ShortcutButton(
                modifier = Modifier.weight(1f),
                text = "Qabul qiluvchini\ntanlash",
                icon = Icons.Default.Info
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clickable { showCardSheet = true },
            shape = RoundedCornerShape(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF001F1A), Color(0xFF004D40))
                        )
                    )
            ) {
                selectedCard?.backgroundUrl?.let { url ->
                    AsyncImage(
                        model = url,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "AloqaBank",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = Color.White.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(50)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Asosiy",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Surface(
                                color = Color.White.copy(alpha = 0.15f),
                                shape = CircleShape,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = selectedCard?.maskedNumber ?: "",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = formatAmount(selectedCard?.balance ?: 0.0),
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = selectedCard?.currency ?: "UZS",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(bottom = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = selectedCard?.holderName ?: "",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (selectedCard == null) {
                    Toast.makeText(context, "Sizda card mavjus emas", Toast.LENGTH_SHORT).show()
                } else if (cardNumber.length != 16) {
                    Toast.makeText(
                        context,
                        "karta 16 xonali son bo`lishi kerak",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (amount.isEmpty() || amount.toDouble() < 5000) {
                    Toast.makeText(
                        context,
                        "Summani 5000 yoki balandroq kiritishingiz kerak",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val userPhone = profileState.userProfile?.phone ?: ""
                    if (userPhone != "") {
                        transferViewModel.setPhone(userPhone)
                        transferViewModel.setDraft(
                            fromCardId = selectedCard?.id ?: "",
                            toCardNumber = cardNumber,
                            amount = amount.toDouble()
                        )
                        navController.navigate("check_transfer")
                    } else {
                        Toast.makeText(
                            context,
                            "Raqam topilmadi , kiritishingiz kerak !",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            },
            enabled = !transferState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (MaterialTheme.colorScheme.background == Color(0xFFF7F9F8)) Color(
                    0xFFEBEBEB
                ) else Color(0xFF2D3843), contentColor = Color.Gray
            )
        ) {
            Text(
                text = "Davom ettirish", fontSize = 16.sp, fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "\"Davom ettirish\"ni bosish orqali siz\noferta shartlariga o'z roziligingizni bildirasiz",
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            lineHeight = 14.sp
        )

        Spacer(modifier = Modifier.height(16.dp))
    }

    if (showCardSheet) {
        CardSelectionBottomSheet(
            cards = cards,
            selectedCard = selectedCard,
            onCardSelected = {
                selectedCard = it
                showCardSheet = false
            },
            onDismissRequest = { showCardSheet = false },
            sheetState = sheetState,
            onAddCardClick = {
                showCardSheet = false
                navController.navigate("cards") // Navigate to cards screen to add a card
            }
        )
    }
}

@Composable
fun ShortcutButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector
) {
    Surface(
        modifier = modifier.height(76.dp),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(44.dp),
                shape = CircleShape,
                color = Color(0xFF00A67E).copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color(0xFF00A67E),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = text,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 14.sp
            )
        }
    }
}

@Preview
@Composable
fun TransferPreview() {
    ZoomradTheme {
        TransferScreen(rememberNavController())
    }
}
