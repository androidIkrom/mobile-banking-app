package com.example.zoomrad.presentation.screens.settings

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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.zoomrad.MainViewModel
import com.example.zoomrad.ui.theme.AppTheme
import com.example.zoomrad.ui.theme.GreenPrimary

data class SettingsItemData(
    val title: String,
    val iconDescription: String,
    val value: String? = null,
    val hasSwitch: Boolean = false,
    val isSwitchOn: Boolean = false,
    val onClick: () -> Unit = {}
)

data class SettingsGroupData(
    val items: List<SettingsItemData>
)

@Composable
fun SettingsScreen(viewModel: MainViewModel,onBack: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    val currentThemeState by viewModel.themeMode

    if (showDialog) {
        ThemeDialog(
            currentTheme = currentThemeState,
            onDismiss = { showDialog = false },
            onSelect = { selected ->
                viewModel.onThemeChange(selected)
                showDialog = false
            }
        )
    }

    val settingsGroups = listOf(
        SettingsGroupData(
            listOf(
                SettingsItemData("Xabarnoma", "Bell notification icon")
            )
        ),
        SettingsGroupData(
            listOf(
                SettingsItemData("Ilova tili", "Language translation icon", value = "O'zbekcha"),
                SettingsItemData(
                    "Ilova mavzusi",
                    "Sun brightness theme icon",
                    value = currentThemeState.title,
                    onClick = { showDialog = true }
                )
            )
        ),
        SettingsGroupData(
            listOf(
                SettingsItemData("Tezkor to'lov", "Credit card icon", value = "**** 9319"),
                SettingsItemData(
                    "Monitoring",
                    "Clock history icon",
                    hasSwitch = true,
                    isSwitchOn = true
                )
            )
        ),
        SettingsGroupData(
            listOf(
                SettingsItemData("Ommaviy oferta", "Document text icon")
            )
        )
    )

    Column(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        SettingsTopBar {
            onBack()
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            modifier = Modifier
                .fillMaxSize()
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
fun ThemeDialog(
    onSelect: (AppTheme) -> Unit,
    currentTheme: AppTheme,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onDismiss) {
                Text("Yopish", color = MaterialTheme.colorScheme.primary)
            }
        },
        title = {
            Text(
                "Mavzuni tanlang",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column {
                val themes = listOf(AppTheme.System, AppTheme.Dark, AppTheme.Light)
                themes.forEach { theme ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(theme) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (theme == currentTheme),
                            onClick = { onSelect(theme) },
                            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = theme.title,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = MaterialTheme.colorScheme.surface
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(onBack: () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Sozlamalar",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        navigationIcon = {
            IconButton(onClick = { onBack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.shadow(elevation = 1.dp)
    )
}

@Composable
fun SettingsCard(group: SettingsGroupData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            group.items.forEachIndexed { index, item ->
                SettingsRow(item)
                if (index < group.items.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 64.dp, end = 16.dp),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
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
            .clickable { item.onClick() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Icon",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = item.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        if (item.value != null) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(
                    text = item.value,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface,
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
                    checkedTrackColor = MaterialTheme.colorScheme.primary,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = MaterialTheme.colorScheme.outline,
                    uncheckedBorderColor = Color.Transparent
                )
            )
        } else {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    val mockViewModel: MainViewModel = viewModel()
    SettingsScreen(mockViewModel){

    }
}
