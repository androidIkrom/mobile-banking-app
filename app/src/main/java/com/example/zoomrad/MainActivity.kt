package com.example.zoomrad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.zoomrad.presentation.navigation.BottomNavItem
import com.example.zoomrad.presentation.screens.settings.SettingsScreen
import com.example.zoomrad.presentation.screens.tabs.home.DrawerContent
import com.example.zoomrad.presentation.screens.tabs.home.HomeScreen
import com.example.zoomrad.presentation.screens.tabs.monitor.MonitoringScreen
import com.example.zoomrad.presentation.screens.tabs.payment.PaymentsScreen
import com.example.zoomrad.presentation.screens.tabs.service.AdditionalServicesScreen
import com.example.zoomrad.presentation.screens.tabs.transfer.TransferScreen
import com.example.zoomrad.ui.theme.AppTheme
import com.example.zoomrad.ui.theme.QuoteReminderTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel : MainViewModel = viewModel()
            val themeMode by viewModel.themeMode
            
            QuoteReminderTheme(appTheme = themeMode) {
                MainScreen(viewModel)
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val navController = rememberNavController()
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
    
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentRoute = currentDestination?.route
    LaunchedEffect(currentRoute) {
        if (drawerState.isOpen)
        drawerState.close()
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = currentRoute == BottomNavItem.Asosiy.route,
        drawerContent = {
//            if (currentRoute == BottomNavItem.Asosiy.route) {
                ModalDrawerSheet(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(320.dp).safeDrawingPadding(),
                    drawerShape = RoundedCornerShape(0.dp),
                    drawerContainerColor = MaterialTheme.colorScheme.surface,
                    windowInsets = WindowInsets(0)
                ) {
                    DrawerContent(
                        onItemClick = { item ->
                            scope.launch { drawerState.close() }
                            if (item == "Sozlamalar") {
                                navController.navigate("settings")
                            }
                        },
                        onLogoutClick = {
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            }
//        }
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
                NavHost(
                    navController = navController,
                    startDestination = BottomNavItem.Asosiy.route,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable(BottomNavItem.Asosiy.route) { 
                        HomeScreen(navController) {
                            scope.launch { drawerState.open() }
                        } 
                    }
                    composable(BottomNavItem.Tolovlar.route) { PaymentsScreen() }
                    composable(BottomNavItem.Otkazma.route) { TransferScreen() }
                    composable(BottomNavItem.Monitoring.route) { MonitoringScreen() }
                    composable(BottomNavItem.Xizmatlar.route) { AdditionalServicesScreen() }
                    composable("settings") { SettingsScreen(viewModel){
                        navController.popBackStack()
                    } }
                }
            }
        }
    }
}

class MainViewModel : ViewModel(){
    private val _themeMode = mutableStateOf<AppTheme>(AppTheme.System)
    val themeMode : State<AppTheme> get() =  _themeMode

    fun onThemeChange(newTheme : AppTheme){
        _themeMode.value = newTheme
    }
}