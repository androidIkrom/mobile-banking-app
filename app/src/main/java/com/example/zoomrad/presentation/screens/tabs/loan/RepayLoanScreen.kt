package com.example.zoomrad.presentation.screens.tabs.loan

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.zoomrad.presentation.screens.tabs.home.formatAmount
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepayLoanScreen(
    navController: NavController,
    loanId: String,
    monthlyPayment: Double,
    loanViewModel: LoanViewModel = hiltViewModel(),
    cardViewModel: CardViewModel = hiltViewModel()
) {
    val loanState by loanViewModel.container.stateFlow.collectAsState()
    val cards by cardViewModel.cards.collectAsState()
    val context = LocalContext.current

    var amount by remember { mutableStateOf(monthlyPayment.toString()) }
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
            is LoanViewModel.LoanSideEffect.LoanRepaid -> {
                Toast.makeText(context, "To'lov muvaffaqiyatli amalga oshirildi", Toast.LENGTH_SHORT).show()
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
                title = { Text("Kredit so'ndirish", fontWeight = FontWeight.Bold) },
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
                label = { Text("To'lov summasi") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                suffix = { Text("so'm") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (cards.isNotEmpty()) {
                Text(
                    text = "To'lov kartasi",
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
                                Text("Balans: ${formatAmount(card.balance)}", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { 
                    val amountLong = amount.toLongOrNull() ?: 0L
                    if (amountLong > 0 && selectedCardId.isNotEmpty()) {
                        loanViewModel.repayLoan(loanId, selectedCardId, amountLong.toDouble())
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !loanState.isLoading && amount.isNotEmpty() && selectedCardId.isNotEmpty()
            ) {
                if (loanState.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("To'lash", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}
