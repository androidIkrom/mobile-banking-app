package com.example.zoomrad.presentation.screens.profile

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presenter.vm.profile.ProfileViewModel
import com.example.zoomrad.R
import com.example.zoomrad.ui.theme.GreenPrimary
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    viewModel: ProfileViewModel
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current
    var showEditDialog by remember { mutableStateOf(false) }
    var newFullName by remember { mutableStateOf("") }

    LaunchedEffect(state.isNewUser) {
        if (state.isNewUser) {
            showEditDialog = true
        }
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is ProfileViewModel.ProfileSideEffect.ShowError -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }
            is ProfileViewModel.ProfileSideEffect.ShowSuccess -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
                showEditDialog = false
            }
        }
    }

    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { 
                if (!state.isNewUser) showEditDialog = false 
            },
            title = { Text(if (state.isNewUser) "Xush kelibsiz! Ma'lumotlaringizni kiriting" else "Profilni tahrirlash") },
            text = {
                OutlinedTextField(
                    value = newFullName,
                    onValueChange = { newFullName = it },
                    label = { Text("To'liq ism") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = { 
                        if (newFullName.isNotBlank()) {
                            viewModel.updateProfile(newFullName)
                        } else {
                            Toast.makeText(context, "Iltimos, ismingizni kiriting", Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = !state.isLoading
                ) {
                    Text("Saqlash")
                }
            },
            dismissButton = {
                if (!state.isNewUser) {
                    TextButton(onClick = { showEditDialog = false }) {
                        Text("Bekor qilish")
                    }
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "back_button",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = "Mening ma'lumotlarim",
                    modifier = Modifier.weight(1f),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                IconButton(onClick = {
                    newFullName = state.userProfile?.fullName ?: ""
                    showEditDialog = true
                }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "edit",
                        tint = GreenPrimary
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_profile),
                        contentDescription = "user_avatar",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(80.dp),
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (state.userProfile?.isKycVerified == true) {
                    Surface(
                        color = Color(0xFF003D2B).copy(alpha = 0.9f),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, Color(0xFF19B387))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .background(Color(0xFF19B387), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "TASDIQLANGAN MIJOZ",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                ProfileInfoCard {
                    ProfileInfoRow("F.I.O:", state.userProfile?.fullName ?: "Nomalum")
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                    ProfileInfoRow("Telefon:", state.userProfile?.phone ?: "Nomalum")
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                    ProfileInfoRow("Ro'yxatdan o'tgan sana:", state.userProfile?.createdAt?.take(10) ?: "Nomalum")
                }

                Spacer(modifier = Modifier.height(16.dp))

                ProfileInfoCard {
                    ProfileInfoRow("ID:", state.userProfile?.id ?: "Nomalum", hasCopy = true)
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                    ProfileInfoRow("Holati:", if (state.userProfile?.isKycVerified == true) "Tasdiqlangan" else "Tasdiqlanmagan")
                }
            }
        }

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = GreenPrimary)
            }
        }
    }
}

@Composable
fun ProfileInfoCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                alpha = 0.4f
            )
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            content()
        }
    }
}

@Composable
fun ProfileInfoRow(label: String, value: String, hasCopy: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = value,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            if (hasCopy) {
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterResource(R.drawable.ic_copy),
                    contentDescription = "ic_copy",
                    modifier = Modifier.size(18.dp),
                    colorFilter = ColorFilter.tint(GreenPrimary)
                )
            }
        }
    }
}
