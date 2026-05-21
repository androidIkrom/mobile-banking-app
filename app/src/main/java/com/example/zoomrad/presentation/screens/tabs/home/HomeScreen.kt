package com.example.zoomrad.presentation.screens.tabs.home

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.entity.model.card.CardData
import com.example.entity.model.payment.PaymentProvider
import com.example.presenter.vm.cards.CardViewModel
import com.example.presenter.vm.payment.PaymentViewModel
import com.example.presenter.vm.transaction.TransactionViewModel
import com.example.zoomrad.R
import com.example.zoomrad.presentation.navigation.BottomNavItem
import com.example.zoomrad.presentation.screens.tabs.service.AdditionalServiceItemData
import com.example.zoomrad.presentation.screens.tabs.service.allServices
import com.example.zoomrad.ui.theme.GreenPrimary
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.Locale

@Composable
fun HomeScreen(
    navController: NavController,
    cardViewModel: CardViewModel = hiltViewModel(),
    paymentViewModel: PaymentViewModel = hiltViewModel(),
    transactionViewModel: TransactionViewModel = hiltViewModel(),
    exchangeViewModel: com.example.presenter.vm.exchange.ExchangeViewModel = hiltViewModel(),
    onMenuClick: () -> Unit,
) {
    val cards by cardViewModel.cards.collectAsState()
    val mainCard = cards.find { it.isMain } ?: cards.firstOrNull()

    val paymentState by paymentViewModel.container.stateFlow.collectAsState()
    val exchangeState by exchangeViewModel.container.stateFlow.collectAsState()
    val transactions by transactionViewModel.transactions.collectAsState()

    val totalPoints = remember(transactions) {
        transactions.sumOf { it.amount }
    }

    val context = LocalContext.current
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) ||
                (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true)
    }

    LaunchedEffect(Unit) {
        cardViewModel.fetchCards()
        paymentViewModel.fetchProviders()
        transactionViewModel.fetchTransactions()
        exchangeViewModel.fetchExchangeRates()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(onMenuClick)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { BalanceCard(navController, mainCard) }
            item { PointsAndCashbackRow(totalPoints) }
            item {
                QuickActionsRow { name ->
                    if (name == "omonat")
                        navController.navigate(BottomNavItem.Otkazma.route)
                    if (name == "kredit")
                        navController.navigate("apply_loan")
                }
            }
            item {
                PaymentsSection(
                    providers = paymentState.providers,
                    onProviderClick = { provider ->
                        val encodedUrl = URLEncoder.encode(
                            provider.logoUrl ?: "",
                            StandardCharsets.UTF_8.toString()
                        )
                        navController.navigate("make_payment/${provider.id}/${provider.name}/$encodedUrl")
                    }
                )
            }
            item { ServicesHeader() }
            item {
                ServicesLazyRow(
                    services = allServices,
                    onServiceClick = { service ->
                        service.route?.let { navController.navigate(it) }
                    }
                )
            }
            if (!hasLocationPermission) {
                item {
                    GeolocationBanner {
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                }
            }
            item { CurrencyRatesSection(exchangeState.rates) }
        }
    }
}

fun getProviderIcon(name: String): Int {
    return when {
        name.contains("Beeline", ignoreCase = true) -> R.drawable.beeline
        name.contains("Ucell", ignoreCase = true) -> R.drawable.ucell
        name.contains("UMS", ignoreCase = true) -> R.drawable.ums
        name.contains("Uzmobile", ignoreCase = true) -> R.drawable.uzmobile
        name.contains("Perfectum", ignoreCase = true) -> R.drawable.perfectum
        name.contains("лектр", ignoreCase = true) -> R.drawable.energy
        name.contains("Sarkor", ignoreCase = true) -> R.drawable.sarkor
        name.contains("Komunal", ignoreCase = true) -> R.drawable.comunal
        name.contains("газ", ignoreCase = true) -> R.drawable.gas
        name.contains("uztelecom", ignoreCase = true) -> R.drawable.uzmobile
        else -> R.drawable.soliq
    }
}

fun getServiceIcon(titleRes: Int): Int {
    return when (titleRes) {
        R.string.service_get_loan -> R.drawable.loan
        R.string.service_my_cards -> R.drawable.cards
        R.string.service_payments -> R.drawable.ic_payment
        R.string.service_transfer -> R.drawable.ic_transfer
        R.string.service_my_home -> R.drawable.ic_home
        R.string.service_online_payment -> R.drawable.payment
        R.string.service_face_pay -> R.drawable.face_pay
        R.string.service_monitoring -> R.drawable.ic_history
        R.string.service_identification -> R.drawable.face_pay
        R.string.service_kids_card -> R.drawable.kids
        R.string.service_moneysend -> R.drawable.send
        else -> {
            R.drawable.ic_deposit
        }
    }
}

@Composable
fun DrawerContent(
    profileViewModel: com.example.presenter.vm.profile.ProfileViewModel = hiltViewModel(),
    onItemClick: (String) -> Unit,
    onLogoutClick: () -> Unit
) {
    val profileState by profileViewModel.container.stateFlow.collectAsState()
    val userProfile = profileState.userProfile

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF003D2B), Color(0xFF007958))
                    ),
                    shape = RoundedCornerShape(bottomEnd = 40.dp)
                )
                .clickable { onItemClick("profile") }
                .padding(24.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(8.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_pr),
                        contentDescription = "ic_user",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(3.dp),
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = userProfile?.fullName?.uppercase(Locale.getDefault())
                        ?: stringResource(R.string.main_user),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = userProfile?.phone ?: "",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                if (userProfile?.isKycVerified == true) {
                    Surface(
                        color = Color(0xFF19B387),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = stringResource(R.string.verified_customer),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val menuItems = listOf(
            Triple("profile", R.string.menu_profile, Icons.Default.Person),
            Triple("kyc", R.string.menu_id, Icons.Default.Person),
            Triple("settings", R.string.menu_settings, Icons.Default.Settings),
            Triple("security", R.string.menu_security, Icons.Default.Lock),
            Triple("applications", R.string.menu_applications, Icons.Default.Email),
            Triple("help", R.string.menu_help, Icons.Default.Info),
            Triple("share", R.string.menu_share, Icons.Default.Share),
            Triple("about", R.string.menu_about, Icons.Default.Info)
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(menuItems) { (id, titleRes, icon) ->
                DrawerItem(DrawerItemData(stringResource(titleRes), icon, id)) {
                    onItemClick(id)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onLogoutClick() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFFFFEDED), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = stringResource(R.string.logout),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.app_version, "2.1.893"),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                fontSize = 12.sp
            )
        }
    }
}

data class DrawerItemData(val title: String, val icon: ImageVector, val id: String = "")

@Composable
fun DrawerItem(item: DrawerItemData, onClick: () -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(horizontal = 24.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (item.id == "kyc") Icons.Default.Face else item.icon ,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = item.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(start = 80.dp, end = 24.dp),
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
        )
    }
}

@Composable
fun TopAppBar(onMenuClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconContainer(R.drawable.ic_menu) {
            onMenuClick()
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconContainer(R.drawable.ic_scanner)
            Spacer(modifier = Modifier.width(12.dp))
            BadgedIconContainer(R.drawable.ic_notify, "53")
        }
    }
}

@Composable
fun IconContainer(iconRes: Int, onClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
            .clickable { onClick() }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
fun BadgedIconContainer(iconRes: Int, badgeCount: String) {
    Box(contentAlignment = Alignment.TopEnd) {
        IconContainer(iconRes)
//        Box(
//            modifier = Modifier
//                .offset(x = 4.dp, y = (-4).dp)
//                .background(NotificationRed, CircleShape)
//                .padding(horizontal = 4.dp, vertical = 0.dp)
//        ) {
//            Text(
//                text = badgeCount,
//                color = Color.White,
//                fontSize = 8.sp,
//                fontWeight = FontWeight.Bold
//            )
//        }
    }
}

@Composable
fun BalanceCard(navController: NavController, card: CardData?) {
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable { navController.navigate("cards") },
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF001F1A), Color(0xFF004D40))
                    )
                )
        ) {
            card?.backgroundUrl?.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = card?.maskedNumber ?: "Karta biriktirilmagan",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_eye),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (card != null) formatAmount(card.balance) else "0 so'm",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = card?.holderName ?: "ZOOMRAD",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                    Text(
                        text = card?.type ?: "",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

fun formatAmount(amount: Double): String {
    return formatMoney(amount) + " so'm"
}

fun formatMoney(amount: Double): String {
    return String.format(Locale.getDefault(), "%,.2f", amount).replace(',', ' ')
}

@Composable
fun PointsAndCashbackRow(points: Double) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.ic_points),
                contentDescription = null,
                tint = GreenPrimary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$points ballar",
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = stringResource(R.string.cashback_balance),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp
        )
    }
}

@Composable
fun QuickActionsRow(onClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        QuickActionCard("Omonat", R.drawable.ic_deposit, Modifier.weight(1f)) {
            onClick("omonat")
        }
        QuickActionCard("Kredit", R.drawable.loan, Modifier.weight(1f)) {
            onClick("kredit")
        }
    }
}

@Composable
fun QuickActionCard(title: String, iconRes: Int, modifier: Modifier, onClick: () -> Unit = {}) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .padding(16.dp)
            .clickable {

            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(GreenPrimary, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun PaymentsSection(
    providers: List<PaymentProvider>,
    onProviderClick: (PaymentProvider) -> Unit
) {
    Text(modifier = Modifier.padding(start = 24.dp), text = stringResource(R.string.payments),fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = MaterialTheme.colorScheme.onSurface)
    LazyRow(
        modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(providers) { index,provider ->
            PaymentCard(
                title = provider.name,
                index = index,
                subtitle = "To'lov",
                logoUrl = provider.logoUrl,
                onClick = { onProviderClick(provider) }
            )
        }
    }
}

@Composable
fun PaymentCard(
    title: String,
    index: Int,
    subtitle: String,
    logoUrl: String?,
    onClick: () -> Unit
) {
    val fallBackPainter = painterResource(remember(title) {
        getProviderIcon(title)
    })
    Column(
        modifier = Modifier
            .padding(start = if (index == 0)16.dp else 0.dp).width(160.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(text = subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(12.dp))
        AsyncImage(
            model = logoUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            contentScale = ContentScale.Fit,
            error = fallBackPainter,
            placeholder = fallBackPainter,
            fallback = fallBackPainter,
//            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
        )
    }
}

@Composable
fun ServicesHeader() {
    Row(
        modifier = Modifier.padding(horizontal = 24.dp)
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.services),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = stringResource(R.string.see_all),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ServicesLazyRow(
    services: List<AdditionalServiceItemData>,
    onServiceClick: (AdditionalServiceItemData) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(services) { index,service ->
            ServiceCard(
                service = service,
                index = index,
                onClick = { onServiceClick(service) }
            )
        }
    }
}

@Composable
fun ServiceCard(service: AdditionalServiceItemData, index: Int, onClick: () -> Unit) {
    val title = stringResource(service.titleRes)
    val iconRes = remember(service.titleRes) { getServiceIcon(titleRes = service.titleRes) }
    Column(
        modifier = Modifier
            .padding(start = if (index == 0) 16.dp else 0.dp)
            .width(140.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            minLines = 2,
            lineHeight = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Image(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
                .size(60.dp)
                .padding(6.dp),
            colorFilter = ColorFilter.tint(Color.White),
        )
    }
}

@Composable
fun GeolocationBanner(onTurnOnCLick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.turn_on_geolocation),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.geolocation_needed),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { onTurnOnCLick() },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text(stringResource(R.string.turn_on), color = Color.White, fontSize = 13.sp)
                }
            }
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(70.dp)
            )
        }
    }
}

fun getIconResource(title: String): Int {
    return when (title) {
        "USS" -> R.drawable.flag_uzb
        "USD" -> R.drawable.flag_usd
        "EUR" -> R.drawable.flag_eur
        "RUB" -> R.drawable.flag_russia
        "GBP" -> R.drawable.flag_gbp
        "CNY" -> R.drawable.flag_cny
        else -> {
            R.drawable.ic_payment
        }
    }
}

@Composable
fun CurrencyRatesSection(rates: List<com.example.entity.model.exchange.ExchangeRate>) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.currency_rates),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.see_all),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                stringResource(R.string.currency),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                stringResource(R.string.buy),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.width(24.dp))
            Text(stringResource(R.string.sell), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.outline,
            thickness = 0.5.dp
        )

        rates.forEach { rate ->
            CurrencyItem(
                code = rate.currency,
                buy = formatMoney(rate.buy),
                sell = formatMoney(rate.sell),
                flagRes = getIconResource(rate.currency)
            )
        }

        if (rates.isEmpty()) {
            Text(
                text = stringResource(R.string.no_data),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        val lastUpdate = rates.firstOrNull()?.updatedAt?.let {
            it.take(10).replace("-", ".")
        } ?: stringResource(R.string.no_data)

        Text(
            text = stringResource(R.string.last_update) + lastUpdate,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 11.sp
        )
    }
}

@Composable
fun CurrencyItem(
    code: String,
    buy: String,
    sell: String,
    flagRes: Int,
    buyDiff: String? = null,
    sellDiff: String? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(flagRes), null, modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = code,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface
        )

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = buy,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (buyDiff != null) Text(text = buyDiff, color = Color.Red, fontSize = 10.sp)
        }
        Spacer(modifier = Modifier.width(24.dp))
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = sell,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (sellDiff != null) Text(text = sellDiff, color = Color.Red, fontSize = 10.sp)
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    val navController = rememberNavController()
//    HomeScreen(navController) {
//
//    }
//}
