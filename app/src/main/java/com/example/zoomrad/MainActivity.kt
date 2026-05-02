package com.example.zoomrad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
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
import com.example.zoomrad.presentation.screens.*
import com.example.zoomrad.ui.theme.AppTheme
import com.example.zoomrad.ui.theme.QuoteReminderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuoteReminderTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navItems = listOf(
        BottomNavItem.Asosiy,
        BottomNavItem.Tolovlar,
        BottomNavItem.Otkazma,
        BottomNavItem.Monitoring,
        BottomNavItem.Xizmatlar
    )
    val viewModel : MainViewModel = viewModel()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                navItems.forEach { item ->
                    val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = item.title
                            )
                        },
                        label = { Text(text = item.title, fontSize = 10.sp) },
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
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Asosiy.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Asosiy.route) { HomeScreen() }
            composable(BottomNavItem.Tolovlar.route) { PaymentsScreen() }
            composable(BottomNavItem.Otkazma.route) { TransferScreen() }
            composable(BottomNavItem.Monitoring.route) { MonitoringScreen() }
            composable(BottomNavItem.Xizmatlar.route) { AdditionalServicesScreen() }
            composable("settings") { SettingsScreen(viewModel) }
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