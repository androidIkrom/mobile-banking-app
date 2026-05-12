package com.example.zoomrad.presentation.screens.chucker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.presenter.vm.chucker.ChuckerViewModel
import com.example.usecase.model.NetworkLog
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MyChuckerActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: ChuckerViewModel = hiltViewModel()
            val logs by viewModel.logs.collectAsState()
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Network Logs") },
                        actions = {
                            IconButton(onClick = { viewModel.clearLogs() }) {
                                Icon(Icons.Default.Delete, contentDescription = "Clear Logs")
                            }
                        }
                    )
                }
            ) { padding ->
                LazyColumn(modifier = Modifier.padding(padding)) {
                    items(logs) { log ->
                        LogItem(log)
                    }
                }
            }
        }
    }
}

@Composable
fun LogItem(log: NetworkLog) {
    var expanded by remember { mutableStateOf(false) }
    val statusColor = if (log.code.startsWith("2")) Color(0xFF4CAF50) else Color(0xFFF44336)
    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${log.method} ${log.code}",
                    color = statusColor,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = timeFormat.format(Date(log.timeStamp)),
                    fontSize = 12.sp
                )
            }
            Text(text = log.url, fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp))

            if (expanded) {
                HorizontalDivider(
                    Modifier.padding(vertical = 8.dp),
                    DividerDefaults.Thickness,
                    DividerDefaults.color
                )
                Text(text = "Duration: ${log.duration}ms", fontSize = 12.sp)

                Text(
                    text = "Request Body:",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = log.requestBody ?: "None", fontSize = 12.sp, modifier = Modifier
                        .background(Color.LightGray.copy(alpha = 0.2f))
                        .fillMaxWidth()
                )

                Text(
                    text = "Response Body:",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = log.responseBody, fontSize = 12.sp, modifier = Modifier
                        .background(Color.LightGray.copy(alpha = 0.2f))
                        .fillMaxWidth()
                )

                Text(
                    text = "Message: ${log.message}",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}