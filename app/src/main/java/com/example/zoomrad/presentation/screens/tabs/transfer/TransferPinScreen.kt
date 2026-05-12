package com.example.zoomrad.presentation.screens.tabs.transfer

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presenter.vm.transfer.TransferViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun TransferPinScreen(
    viewModel: TransferViewModel,
    onSuccess: () -> Unit,
    onNavigateToOtp: () -> Unit
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current
    var pinText by remember { mutableStateOf("") }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is TransferViewModel.TransferSideEffect.ShowSuccess -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
                onSuccess()
            }
            is TransferViewModel.TransferSideEffect.ShowError -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }
            is TransferViewModel.TransferSideEffect.NavigateToConfirm -> {
                onNavigateToOtp()
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .statusBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Color(0xFF00A67E)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Tasdiqlash",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "O'tkazmani tasdiqlash uchun PIN-kodni kiriting",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(48.dp))

        PinInputRow(
            value = pinText,
            onValueChange = { if (it.length <= 4) pinText = it }
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (state.confirmToken == null) {
                    viewModel.initiateTransfer(
                        fromCardId = state.fromCardId,
                        toCardNumber = state.toCardNumber,
                        amount = state.amount,
                        pin = pinText,
                        description = "O`tkazma",
                        isPinMethod = true
                    )
                } else {
                    viewModel.confirmWithPin(pinText)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A67E)),
            enabled = pinText.length == 4 && !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("PIN-kod orqali tasdiqlash", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                viewModel.initiateTransfer(
                    fromCardId = state.fromCardId,
                    toCardNumber = state.toCardNumber,
                    amount = state.amount,
                    pin = "1234",
                    description = "O`tkazma",
                    isPinMethod = false
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            enabled = !state.isLoading
        ) {
            Text("SMS ko'd orqali tasdiqlash", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PinInputRow(
    value: String,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        visualTransformation = PasswordVisualTransformation(),
        decorationBox = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(4) { index ->
                    val isFilled = index < value.length
                    val isFocused = value.length == index

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(
                                if (isFilled) Color(0xFF00A67E)
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .border(
                                width = 1.dp,
                                color = if (isFocused) Color(0xFF00A67E) else Color.Transparent,
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    )
}
