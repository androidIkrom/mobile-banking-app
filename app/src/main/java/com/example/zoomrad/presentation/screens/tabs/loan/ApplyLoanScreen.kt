package com.example.zoomrad.presentation.screens.tabs.loan

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.presenter.vm.cards.CardViewModel
import com.example.presenter.vm.loan.LoanViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplyLoanScreen(
    navController: NavController,
    loanViewModel: LoanViewModel = hiltViewModel(),
    cardViewModel: CardViewModel = hiltViewModel()
) {
    val loanState by loanViewModel.container.stateFlow.collectAsState()
    val cards by cardViewModel.cards.collectAsState()
    val context = LocalContext.current

    var amount by remember { mutableStateOf("") }
    var termMonths by remember { mutableIntStateOf(12) }
    var selectedCardId by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        cardViewModel.fetchCards()
    }

    LaunchedEffect(cards) {
        if (cards.isNotEmpty() && selectedCardId.isEmpty()) {
            selectedCardId = cards.first().id
        }
    }

    loanViewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is LoanViewModel.LoanSideEffect.LoanApplied -> {
                Toast.makeText(context, "Kredit muvaffaqiyatli rasmiylashtirildi", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
            is LoanViewModel.LoanSideEffect.ShowError -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Kredit olish", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = amount,
                onValueChange = { if (it.all { char -> char.isDigit() }) amount = it },
                label = { Text("Summa") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                suffix = { Text("so'm") }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Muddati: $termMonths oy",
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Medium
            )
            Slider(
                value = termMonths.toFloat(),
                onValueChange = { termMonths = it.toInt() },
                valueRange = 3f..36f,
                steps = 33,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (cards.isNotEmpty()) {
                Text(
                    text = "Qabul qiluvchi karta",
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                cards.forEach { card ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedCardId == card.id) 
                                MaterialTheme.colorScheme.primaryContainer 
                            else MaterialTheme.colorScheme.surface
                        ),
                        onClick = { selectedCardId = card.id }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedCardId == card.id,
                                onClick = { selectedCardId = card.id }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(card.type, fontWeight = FontWeight.Bold)
                                Text(card.maskedNumber, fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { 
                    val amountDouble = amount.toDoubleOrNull() ?: 0.0
                    if (amountDouble > 0 && selectedCardId.isNotEmpty()) {
                        loanViewModel.applyLoan(amountDouble, termMonths, selectedCardId)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !loanState.isLoading && amount.isNotEmpty() && selectedCardId.isNotEmpty()
            ) {
                if (loanState.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Tasdiqlash", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}
