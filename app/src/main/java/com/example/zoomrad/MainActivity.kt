package com.example.zoomrad

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.entity.local.PrefsManager
import com.example.presenter.vm.auth.AuthViewModel
import com.example.presenter.vm.profile.ProfileViewModel
import com.example.zoomrad.presentation.navigation.AppNavHost
import com.example.zoomrad.presentation.navigation.BottomNavItem
import com.example.zoomrad.presentation.screens.tabs.home.DrawerContent
import com.example.zoomrad.ui.theme.AppTheme
import com.example.zoomrad.ui.theme.QuoteReminderTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var prefsManager: PrefsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = viewModel()
            val themeMode by viewModel.themeMode

            QuoteReminderTheme(appTheme = themeMode) {
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
            val isPermitted = ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            if (!isPermitted){
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
    val authViewModel: AuthViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
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
                DrawerContent(
                    onItemClick = { item ->
                        scope.launch { drawerState.close() }
                        when (item) {
                            "Sozlamalar" -> navController.navigate("settings")
                            "Profil" -> navController.navigate("profile")
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
                                    Icon(modifier = Modifier.size(30.dp),
                                        painter = painterResource(id = item.icon),
                                        contentDescription = item.title
                                    )
                                },
                                label = { Text(text = item.title, fontSize = 8.sp) },
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
                    onOpenDrawer = { scope.launch { drawerState.open() } }
                )
            }
        }
    }
}

class MainViewModel : ViewModel() {
    private val _themeMode = mutableStateOf<AppTheme>(AppTheme.System)
    val themeMode: State<AppTheme> get() = _themeMode

    fun onThemeChange(newTheme: AppTheme) {
        _themeMode.value = newTheme
    }
}
