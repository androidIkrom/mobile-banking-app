package com.example.zoomrad.presentation.screens.lock

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.entity.local.PrefsManager

enum class LockState {
    CREATE, CONFIRM, ENTER
}

@Composable
fun AppLockScreen(
    prefsManager: PrefsManager,
    onSuccess: () -> Unit
) {
    var currentState by remember {
        mutableStateOf(
            if (prefsManager.appPassword == null) LockState.CREATE else LockState.ENTER
        )
    }
    
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var enteredPassword by remember { mutableStateOf("") }

    val title = when (currentState) {
        LockState.CREATE -> "Create Password"
        LockState.CONFIRM -> "Confirm Password"
        LockState.ENTER -> "Enter Code"
    }

    val subtitle = when (currentState) {
        LockState.CREATE -> "Set a 4-digit code for local access"
        LockState.CONFIRM -> "Please re-enter your code to confirm"
        LockState.ENTER -> "Enter your access code to continue"
    }

    val currentInput = when (currentState) {
        LockState.CREATE -> password
        LockState.CONFIRM -> confirmPassword
        LockState.ENTER -> enteredPassword
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Color(0xFF00A67E).copy(alpha = 0.9f)),
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
            text = title,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = subtitle,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(48.dp))

        PinDots(currentInput)

        Spacer(modifier = Modifier.weight(1f))

        NumberPad { char ->
            when (currentState) {
                LockState.CREATE -> {
                    if (char == "C") {
                        if (password.isNotEmpty()) password = password.dropLast(1)
                    } else if (password.length < 4) {
                        password += char
                        if (password.length == 4) {
                            currentState = LockState.CONFIRM
                        }
                    }
                }
                LockState.CONFIRM -> {
                    if (char == "C") {
                        if (confirmPassword.isNotEmpty()) confirmPassword = confirmPassword.dropLast(1)
                    } else if (confirmPassword.length < 4) {
                        confirmPassword += char
                        if (confirmPassword.length == 4) {
                            if (confirmPassword == password) {
                                prefsManager.appPassword = password
                                onSuccess()
                            } else {
                                confirmPassword = ""
                                // Optional: Show error or toast
                            }
                        }
                    }
                }
                LockState.ENTER -> {
                    if (char == "C") {
                        if (enteredPassword.isNotEmpty()) enteredPassword = enteredPassword.dropLast(1)
                    } else if (enteredPassword.length < 4) {
                        enteredPassword += char
                        if (enteredPassword.length == 4) {
                            if (enteredPassword == prefsManager.appPassword) {
                                onSuccess()
                            } else {
                                enteredPassword = ""
                            }
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun PinDots(password: String) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(4) { index ->
            val isActive = index < password.length
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(
                        if (isActive) Color(0xFF00A67E) 
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .border(
                        width = 1.dp,
                        color = if (isActive) Color(0xFF00A67E) 
                        else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
fun NumberPad(onNumberClick: (String) -> Unit) {
    val numbers = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("", "0", "C")
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        numbers.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                row.forEach { char ->
                    if (char.isEmpty()) {
                        Spacer(modifier = Modifier.size(64.dp))
                    } else {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                                .clickable { onNumberClick(char) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = char,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}
