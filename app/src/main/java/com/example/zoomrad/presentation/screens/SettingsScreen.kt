package com.example.zoomrad.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.zoomrad.MainViewModel
import com.example.zoomrad.ui.theme.*

/**
 * Data classes for Settings UI elements
 */
data class SettingsItemData(
    val title: String,
    val iconDescription: String,
    val value: String? = null,
    val hasSwitch: Boolean = false,
    val isSwitchOn: Boolean = false,
    val onClick : (SettingsItemData) -> Unit = {}
)

data class SettingsGroupData(
    val items: List<SettingsItemData>
)

@Composable
fun SettingsScreen(viewModel: MainViewModel) {
    val currentTheme = viewModel.themeMode
    val themeItem = SettingsItemData("Theme","", value = currentTheme.value.title){
    }
    val settingsGroups = listOf(
        SettingsGroupData(listOf(
            SettingsItemData("Xabarnoma", "Bell notification icon")
        )),
        SettingsGroupData(listOf(
            SettingsItemData("Ilova tili", "Language translation icon", value = "O'zbekcha"),
            SettingsItemData("Ilova mavzusi", "Sun brightness theme icon", value = "Yorqin"){
                showThemeDialog()
            }
        )),
        SettingsGroupData(listOf(
            SettingsItemData("Tezkor to'lov", "Credit card icon", value = "**** 9319"),
            SettingsItemData("Monitoring", "Clock history icon", hasSwitch = true, isSwitchOn = true)
        )),
        SettingsGroupData(listOf(
            SettingsItemData("Ommaviy oferta", "Document text icon")
        ))


    )

    Scaffold(
        topBar = { SettingsTopBar() },
        containerColor = Color(0xFFF7F9F8)
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
        ) {
            items(settingsGroups) { group ->
                SettingsCard(group)
            }
        }
    }
}
@Composable
fun showThemeDialog(onClick: (AppTheme) -> Unit){
    Dialog(onDismissRequest = {
    }) {

    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Sozlamalar",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        },
        navigationIcon = {
            IconButton(onClick = { /* Handle back */ }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.White
        ),
        modifier = Modifier.shadow(elevation = 1.dp)
    )
}

@Composable
fun SettingsCard(group: SettingsGroupData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            group.items.forEachIndexed { index, item ->
                SettingsRow(item)
                if (index < group.items.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 64.dp, end = 16.dp),
                        thickness = 0.5.dp,
                        color = Color(0xFFEEEEEE)
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsRow(item: SettingsItemData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable { item.onClick(item) }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon background
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFF5F5F5), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // INFO: Icon - item.iconDescription
            // Using a simple placeholder as requested
            Text(
                text = "Icon",
                fontSize = 10.sp,
                color = Color(0xFFB2B2B2)
            )
            /* 
               Add actual icon here:
               Icon(
                   painter = painterResource(id = R.drawable.your_icon),
                   contentDescription = item.iconDescription,
                   tint = Color.Black,
                   modifier = Modifier.size(22.dp)
               )
            */
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Text(
            text = item.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        
        if (item.value != null) {
            Surface(
                color = Color(0xFFF5F5F5),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(
                    text = item.value,
                    fontSize = 13.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
        
        if (item.hasSwitch) {
            Switch(
                checked = item.isSwitchOn,
                onCheckedChange = { /* Handle toggle */ },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF00A67E),
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFFE0E0E0),
                    uncheckedBorderColor = Color.Transparent
                )
            )
        } else {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFFB2B2B2),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}
