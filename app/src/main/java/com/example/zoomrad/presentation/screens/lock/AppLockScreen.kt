package com.example.zoomrad.presentation.screens.lock

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.entity.local.PrefsManager
import com.example.presenter.vm.auth.AuthViewModel
import com.example.zoomrad.R
import org.orbitmvi.orbit.compose.collectSideEffect

enum class LockState {
    CREATE, CONFIRM, ENTER, RESET
}

@Composable
fun AppLockScreen(
    prefsManager: PrefsManager,
    viewModel: AuthViewModel = hiltViewModel(),
    isResetMode: Boolean = false,
    onSuccess: () -> Unit,
) {
    val state by viewModel.container.stateFlow.collectAsState()
    val context = LocalContext.current
    var currentState by remember {
        mutableStateOf(
            when {
                isResetMode -> LockState.RESET
                prefsManager.appPassword == null -> LockState.CREATE
                else -> LockState.ENTER
            }
        )
    }
    
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var enteredPassword by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is AuthViewModel.AuthSideEffect.SetPinSuccess -> {
                prefsManager.appPassword = password
                onSuccess()
            }
            is AuthViewModel.AuthSideEffect.ShowToast -> {
                android.widget.Toast.makeText(context, sideEffect.message, android.widget.Toast.LENGTH_SHORT).show()
                if (currentState == LockState.CONFIRM) {
                    confirmPassword = ""
                }
            }
            else -> {}
        }
    }

    val title = when (currentState) {
        LockState.CREATE -> stringResource(R.string.lock_create_title)
        LockState.CONFIRM -> stringResource(R.string.lock_confirm_title)
        LockState.ENTER -> stringResource(R.string.lock_enter_title)
        LockState.RESET -> stringResource(R.string.lock_reset_title)
    }

    val subtitle = when (currentState) {
        LockState.CREATE -> stringResource(R.string.lock_create_desc)
        LockState.CONFIRM -> stringResource(R.string.lock_confirm_desc)
        LockState.ENTER -> stringResource(R.string.lock_enter_desc)
        LockState.RESET -> stringResource(R.string.lock_reset_desc)
    }

    val currentInput = when (currentState) {
        LockState.CREATE -> password
        LockState.CONFIRM -> confirmPassword
        LockState.ENTER -> enteredPassword
        LockState.RESET -> password
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
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        if (state.isLoading) {
            CircularProgressIndicator(color = Color(0xFF00A67E))
        } else {
            PinDots(currentInput, isError)
        }

        Spacer(modifier = Modifier.weight(1f))

        NumberPad { char ->
            if (state.isLoading) return@NumberPad
            
            if (isError) isError = false
            
            when (currentState) {
                LockState.CREATE, LockState.RESET -> {
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
                                viewModel.setPin(password)
                            } else {
                                isError = true
                                confirmPassword = ""
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
                                isError = true
                                enteredPassword = ""
                            }
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(54.dp))
    }
}

@Composable
fun PinDots(password: String, isError: Boolean = false) {
    val shakeOffset = remember { Animatable(0f) }

    LaunchedEffect(isError) {
        if (isError) {
            repeat(1) {
                shakeOffset.animateTo(
                    targetValue = 5f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessMedium)
                )
                shakeOffset.animateTo(
                    targetValue = -5f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessMedium)
                )
            }
            shakeOffset.animateTo(0f)
        }
    }

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .offset(x = shakeOffset.value.dp)
    ) {
        repeat(4) { index ->
            val isActive = (index < password.length) || (isError && (index < 4))
            val dotColor = when {
                isError -> Color.Red
                isActive -> Color(0xFF00A67E)
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(dotColor)
                    .border(
                        width = 1.dp,
                        color = if (isError) Color.Red 
                        else if (isActive) Color(0xFF00A67E)
                        else MaterialTheme.colorScheme.outline,
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
        listOf("", "0", "C"),
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
                                .size(74.dp)
                                .clip(CircleShape)
                                .background(
                                    Color.Gray.copy(alpha = 0.2f)
//                                    MaterialTheme.colorScheme.surfaceVariant
                                )
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
