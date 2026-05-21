package com.example.zoomrad

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.content.Context
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zoomrad.util.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.entity.local.PrefsManager
import com.example.presenter.vm.auth.AuthViewModel
import com.example.presenter.vm.profile.ProfileViewModel
import com.example.presenter.vm.transfer.TransferViewModel
import com.example.zoomrad.presentation.navigation.AppNavHost
import com.example.zoomrad.presentation.navigation.BottomNavItem
import com.example.zoomrad.presentation.screens.tabs.home.DrawerContent
import com.example.zoomrad.util.LocaleHelper
import com.example.zoomrad.ui.theme.AppTheme
import com.example.zoomrad.ui.theme.ZoomradTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var prefsManager: PrefsManager

    override fun attachBaseContext(newBase: Context) {
        val lang = newBase.getSharedPreferences("zoomrad_prefs", Context.MODE_PRIVATE)
            .getString("app_language", "uz") ?: "uz"
        super.attachBaseContext(LocaleHelper.wrapContext(newBase, lang))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = viewModel()
            val themeMode by viewModel.themeMode
            val appLanguage by viewModel.appLanguage
            val context = LocalContext.current

            LaunchedEffect(appLanguage) {
                if (prefsManager.appLanguage != appLanguage) {
                }

            }
            LaunchedEffect(Unit) {
                viewModel.events.collect{
                    event ->
                    when(event){
                        is MainEvent.Recreate ->{
                            (context as? Activity)?.recreate()
                        }
                    }
                }
            }

            ZoomradTheme(appTheme = themeMode) {
                MainScreen(viewModel, prefsManager)
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel, prefsManager: PrefsManager) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it)
            Toast.makeText(context, "rahmat", Toast.LENGTH_SHORT).show()
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val isPermitted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!isPermitted) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    val navItems = listOf(
        BottomNavItem.Asosiy,
        BottomNavItem.Tolovlar,
        BottomNavItem.Otkazma,
        BottomNavItem.Monitoring,
        BottomNavItem.Xizmatlar
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar = navItems.any {
        it.route == currentDestination?.route
    }

    val startDestination = remember {
        if (prefsManager.accessToken != null) "lock" else "splash"
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentRoute = currentDestination?.route
    LaunchedEffect(currentRoute) {
        if (drawerState.isOpen)
            drawerState.close()
    }

    val connectivityStatus by viewModel.connectivityStatus.collectAsState(ConnectivityObserver.Status.Available)
    val showOfflineDialog = (connectivityStatus != ConnectivityObserver.Status.Available) &&
            (currentRoute != null) &&
            (currentRoute != "lock") &&
            (currentRoute != "splash") &&
            (currentRoute != "login") &&
            (currentRoute != "otp")

    if (showOfflineDialog) {
        com.example.zoomrad.presentation.components.OfflineDialog {

        }
    }

    val authViewModel: AuthViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
    val transferViewModel: TransferViewModel = viewModel()
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = currentRoute == BottomNavItem.Asosiy.route,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(320.dp)
                    .safeDrawingPadding(),
                drawerShape = RoundedCornerShape(0.dp),
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                windowInsets = WindowInsets(0)
            ) {
//                val menuItems = listOf(
//                    Triple("profile", R.string.menu_profile, Icons.Default.Person),
//                    Triple("kyc", R.string.menu_id, Icons.Default.Person),
//                    Triple("settings", R.string.menu_settings, Icons.Default.Settings),
//                    Triple("security", R.string.menu_security, Icons.Default.Lock),
//                    Triple("applications", R.string.menu_applications, Icons.Default.Email),
//                    Triple("help", R.string.menu_help, Icons.Default.Info),
//                    Triple("share", R.string.menu_share, Icons.Default.Share),
//                    Triple("about", R.string.menu_about, Icons.Default.Info)
//                )
                DrawerContent(
                    profileViewModel = profileViewModel,
                    onItemClick = { item ->
                        scope.launch { drawerState.close() }
                        when (item) {
                            "settings" -> navController.navigate("settings")
                            "profile" -> navController.navigate("profile")
                            "kyc" -> navController.navigate("kyc")
                            "security" -> navController.navigate("reset_pin")
                            "applications" -> navController.navigate("applications")
                            "help" -> navController.navigate("help")
//                            "share" -> navController.navigate("share")
                            "about" -> navController.navigate("about")
                        }
                    },
                    onLogoutClick = {
                        scope.launch { drawerState.close() }
                        prefsManager.clear()
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp
                    ) {
                        navItems.forEach { item ->
                            val selected =
                                currentDestination?.hierarchy?.any { it.route == item.route } == true
                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        modifier = Modifier.size(30.dp),
                                        painter = painterResource(id = item.icon),
                                        contentDescription = stringResource(id = item.titleRes)
                                    )
                                },
                                label = {
                                    Text(
                                        text = stringResource(id = item.titleRes),
                                        fontSize = 8.sp
                                    )
                                },
                                selected = selected,
                                onClick = {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Color(0xFF00A67E),
                                    selectedTextColor = Color(0xFF00A67E),
                                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    indicatorColor = Color.Transparent
                                )
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                AppNavHost(
                    navController = navController,
                    startDestination = startDestination,
                    prefsManager = prefsManager,
                    mainViewModel = viewModel,
                    authViewModel = authViewModel,
                    profileViewModel = profileViewModel,
                    transferViewModel = transferViewModel,
                    onOpenDrawer = { scope.launch { drawerState.open() } }
                )
            }
        }
    }
}

sealed class MainEvent {
    object Recreate : MainEvent()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    val prefsManager: PrefsManager,
    connectivityObserver: ConnectivityObserver
) : ViewModel() {
    private val _appLanguage = mutableStateOf(prefsManager.appLanguage)
    val appLanguage: State<String> get() = _appLanguage
    private val _events = MutableSharedFlow<MainEvent>()
    val events = _events.asSharedFlow()
    private val _themeMode = mutableStateOf<AppTheme>(AppTheme.System)
    val themeMode: State<AppTheme> get() = _themeMode


    val connectivityStatus = connectivityObserver.observe()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ConnectivityObserver.Status.Available
        )

    fun onThemeChange(newTheme: AppTheme) {
        _themeMode.value = newTheme
    }

    fun onLanguageChange(langCode: String) {
        prefsManager.appLanguage = langCode
        _appLanguage.value = langCode
        viewModelScope.launch {
            _events.emit(MainEvent.Recreate)
        }
    }
}
