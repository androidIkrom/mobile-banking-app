package com.example.zoomrad.presentation.screens.settings

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presenter.vm.cards.CardViewModel
import com.example.zoomrad.MainViewModel
import com.example.zoomrad.R
import com.example.zoomrad.presentation.screens.tabs.transfer.CardSelectionBottomSheet
import com.example.zoomrad.ui.theme.AppTheme

data class SettingsItemData(
    val title: String,
    val iconDescription: String,
    val value: String? = null,
    var hasSwitch: Boolean = false,
    val isSwitchOn: Boolean = false,
    val onClick: () -> Unit = {}
)

data class SettingsGroupData(
    val items: List<SettingsItemData>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    cardViewModel: CardViewModel,
    onBack: () -> Unit,
    onNotificationClick: () -> Unit,
    onAddCardClick: () -> Unit
) {
    val context = LocalContext.current
    var showThemeDialog by remember { mutableStateOf(false) }
    val currentThemeState by viewModel.themeMode
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showCardSelection by remember { mutableStateOf(false) }

    val cards by cardViewModel.cards.collectAsState()
    val mainCard = cards.find { it.isMain }

    LaunchedEffect(Unit) {
        cardViewModel.fetchCards()
    }

    if (showThemeDialog) {
        ThemeDialog(
            currentTheme = currentThemeState,
            onDismiss = { showThemeDialog = false },
            onSelect = { selected ->
                viewModel.onThemeChange(selected)
                showThemeDialog = false
            }
        )
    }
    if (showLanguageDialog) {
        LanguageBottomSheet(
            currentLanguage = viewModel.appLanguage.value,
            onLanguageSelected = { lang ->
                viewModel.onLanguageChange(lang)
                showLanguageDialog = false
            },
            onDismiss = { showLanguageDialog = false }
        )
    }

    if (showCardSelection) {
        CardSelectionBottomSheet(
            cards = cards,
            selectedCard = mainCard,
            onCardSelected = { card ->
                cardViewModel.setMainCard(card.id)
                showCardSelection = false
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
            },
            onDismissRequest = { showCardSelection = false },
            sheetState = rememberModalBottomSheetState(),
            onAddCardClick = onAddCardClick
        )
    }

    val settingsGroups = listOf(
        SettingsGroupData(
            listOf(
                SettingsItemData(
                    title = stringResource(R.string.notifications),
                    iconDescription = "Bell notification icon",
                    onClick = onNotificationClick
                )
            )
        ),
        SettingsGroupData(
            listOf(
                SettingsItemData(
                    title = stringResource(R.string.app_language),
                    iconDescription = "Language translation icon",
                    value = when (viewModel.appLanguage.value) {
                        "uz" -> stringResource(R.string.lang_uz)
                        "ru" -> stringResource(R.string.lang_ru)
                        else -> stringResource(R.string.lang_en)
                    },
                    onClick = {
                        showLanguageDialog = true
                    }
                ),
                SettingsItemData(
                    title = stringResource(R.string.app_theme),
                    iconDescription = "Sun brightness theme icon",
                    value = when (currentThemeState) {
                        AppTheme.System -> stringResource(R.string.theme_system)
                        AppTheme.Dark -> stringResource(R.string.theme_dark)
                        AppTheme.Light -> stringResource(R.string.theme_light)
                    },
                    onClick = { showThemeDialog = true }
                )
            )
        ),
        SettingsGroupData(
            listOf(
                SettingsItemData(
                    title = stringResource(R.string.quick_payment),
                    iconDescription = "Credit card icon",
                    value = mainCard?.maskedNumber ?: "****",
                    onClick = { showCardSelection = true }
                ),
                SettingsItemData(
                    title = stringResource(R.string.monitoring),
                    iconDescription = "Clock history icon",
                    hasSwitch = true,
                    isSwitchOn = true
                )
            )
        ),
        SettingsGroupData(
            listOf(
                SettingsItemData(
                    title = stringResource(R.string.public_offer),
                    iconDescription = "Document text icon"
                )
            )
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                Text(
                    text = stringResource(R.string.close),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        title = {
            Text(
                text = stringResource(R.string.theme_select),
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
                            text = when(theme) {
                                AppTheme.System -> stringResource(R.string.theme_system)
                                AppTheme.Dark -> stringResource(R.string.theme_dark)
                                AppTheme.Light -> stringResource(R.string.theme_light)
                            },
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
                text = stringResource(R.string.menu_settings),
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
    val img = when (item.title) {
        stringResource(R.string.notifications) -> R.drawable.ic_notify
        stringResource(R.string.app_language) -> R.drawable.ic_language
        stringResource(R.string.quick_payment) -> R.drawable.ic_transfer
        stringResource(R.string.public_offer) -> R.drawable.ic_offrerta
        stringResource(R.string.monitoring) -> R.drawable.ic_history
        stringResource(R.string.app_theme) -> R.drawable.ic_theme
        else -> {
            R.drawable.ic_menu
        }
    }
    var switch by remember { mutableStateOf(true) }
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
                .background(MaterialTheme.colorScheme.primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(painter = painterResource(img), null, colorFilter = ColorFilter.tint(Color.White))
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
                checked = switch,
                onCheckedChange = {
                    switch = it
                },
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageBottomSheet(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val languages = listOf(
        LanguageData("O‘zbekcha", "uz", R.drawable.flag_uzb),
        LanguageData("Русский", "ru", R.drawable.flag_russia),
        LanguageData("English", "en", R.drawable.flag_usd)
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(),
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.select_language),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            languages.forEach { lang ->
                LanguageItem(
                    language = lang,
                    isSelected = lang.code == currentLanguage,
                    onClick = { onLanguageSelected(lang.code) }
                )
                if (lang != languages.last()) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
                    )
                }
            }
        }
    }
}

@Composable
fun LanguageItem(
    language: LanguageData,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(language.flagRes),
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = language.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = Color(0xFF19B387)
            )
        )
    }
}

data class LanguageData(val name: String, val code: String, val flagRes: Int)
