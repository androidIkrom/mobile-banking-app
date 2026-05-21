package com.example.zoomrad.presentation.screens.kyc

import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.presenter.vm.kyc.KycViewModel
import com.example.zoomrad.R
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.io.ByteArrayOutputStream
import java.io.InputStream

val OvalShape = GenericShape { size, _ ->
    addOval(Rect(0f, 0f, size.width, size.height))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KycScreen(
    navController: NavController,
    viewModel: KycViewModel = hiltViewModel()
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current

    var passportSeries by remember { mutableStateOf("") }
    var passportNumber by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var selfieBase64 by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showImageSourceDialog by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        capturedBitmap = null
        uri?.let {
            val base64 = uriToBase64(context, it)
            if (base64 != null) {
                selfieBase64 = base64
            } else {
                Toast.makeText(context, "Rasmni yuklashda xatolik", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        capturedBitmap = bitmap
        selectedImageUri = null
        bitmap?.let {
            selfieBase64 = bitmapToBase64(it)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchKycStatus()
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is KycViewModel.KycSideEffect.ShowError -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }
            KycViewModel.KycSideEffect.SubmitSuccess -> {
                Toast.makeText(context, "Hujjatlar yuborildi!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text(stringResource(R.string.kyc_dialog_title)) },
            text = { Text(stringResource(R.string.kyc_dialog_desc)) },
            confirmButton = {
                TextButton(onClick = {
                    cameraLauncher.launch()
                    showImageSourceDialog = false
                }) {
                    Text(stringResource(R.string.kyc_camera))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    photoPickerLauncher.launch("image/*")
                    showImageSourceDialog = false
                }) {
                    Text(stringResource(R.string.kyc_gallery))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.kyc_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.isLoading && state.kycStatus == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF00A67E))
                }
            } else {
                state.kycStatus?.let { status ->
                    KycStatusHeader(status)
                    Spacer(modifier = Modifier.height(32.dp))

                    if (status.status == "NOT_SUBMITTED" || status.status == "REJECTED") {
                        KycForm(
                            passportSeries = passportSeries,
                            onPassportSeriesChange = { passportSeries = it.uppercase().take(2) },
                            passportNumber = passportNumber,
                            onPassportNumberChange = { if (it.length <= 7) passportNumber = it },
                            birthDate = birthDate,
                            onBirthDateChange = { birthDate = it },
                            isLoading = state.isLoading,
                            onSubmit = {
                                viewModel.submitKyc(
                                    passportSeries,
                                    passportNumber,
                                    birthDate,
                                    selfieBase64
                                )
                            },
                            selectedImageUri = selectedImageUri,
                            capturedBitmap = capturedBitmap,
                            onPickImage = { showImageSourceDialog = true }
                        )
                    } else if (status.status == "PENDING") {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        ) {
                            Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Info, null, tint = Color(0xFF1976D2), modifier = Modifier.size(48.dp))
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = stringResource(R.string.kyc_pending_desc),
                                    textAlign = TextAlign.Center,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KycStatusHeader(status: com.example.entity.model.kyc.KycStatusData) {
    val (icon, color, textRes) = when (status.status) {
        "VERIFIED" -> Triple(Icons.Default.CheckCircle, Color(0xFF00A67E), R.string.kyc_status_verified)
        "PENDING" -> Triple(Icons.Default.Info, Color(0xFF1976D2), R.string.kyc_status_pending)
        "REJECTED" -> Triple(Icons.Default.Warning, Color.Red, R.string.kyc_status_rejected)
        else -> Triple(Icons.Default.Info, Color.Gray, R.string.kyc_status_not_submitted)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = stringResource(textRes), fontWeight = FontWeight.Bold, color = color, fontSize = 18.sp)
                status.rejectionReason?.let {
                    Text(text = stringResource(R.string.kyc_rejection_reason, it), fontSize = 14.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun KycForm(
    passportSeries: String,
    onPassportSeriesChange: (String) -> Unit,
    passportNumber: String,
    onPassportNumberChange: (String) -> Unit,
    birthDate: String,
    onBirthDateChange: (String) -> Unit,
    isLoading: Boolean,
    onSubmit: () -> Unit,
    selectedImageUri: Uri?,
    capturedBitmap: Bitmap?,
    onPickImage: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(stringResource(R.string.kyc_passport_info), fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = passportSeries,
                onValueChange = onPassportSeriesChange,
                label = { Text(stringResource(R.string.kyc_series)) },
                placeholder = { Text("AA") },
                modifier = Modifier.width(100.dp),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(12.dp))
            OutlinedTextField(
                value = passportNumber,
                onValueChange = onPassportNumberChange,
                label = { Text(stringResource(R.string.kyc_number)) },
                placeholder = { Text("1234567") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = birthDate,
            onValueChange = onBirthDateChange,
            label = { Text(stringResource(R.string.kyc_birth_date)) },
            placeholder = { Text("1995-06-15") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Gray.copy(alpha = 0.1f))
                .clickable { onPickImage() },
            contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri != null || capturedBitmap != null) {
                val painter = if (selectedImageUri != null) {
                    rememberAsyncImagePainter(selectedImageUri)
                } else {
                    rememberAsyncImagePainter(capturedBitmap)
                }
                
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(0.8f)
                        .clip(OvalShape),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(48.dp)
                    )
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(0.8f)
                        .background(Color.White.copy(alpha = 0.5f), OvalShape)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        stringResource(R.string.kyc_selfie_hint), 
                        color = Color.Gray,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00A67E)),
            enabled = !isLoading && passportSeries.length == 2 && passportNumber.length == 7 && birthDate.isNotEmpty() && (selectedImageUri != null || capturedBitmap != null)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(stringResource(R.string.kyc_submit_button), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

private fun uriToBase64(context: android.content.Context, uri: Uri): String? {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val bytes = inputStream?.readBytes()
        inputStream?.close()
        bytes?.let { android.util.Base64.encodeToString(it, android.util.Base64.DEFAULT) }
    } catch (e: Exception) {
        null
    }
}

private fun bitmapToBase64(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT)
}
