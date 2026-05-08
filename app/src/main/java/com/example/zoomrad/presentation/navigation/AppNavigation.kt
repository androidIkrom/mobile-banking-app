package com.example.zoomrad.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.entity.local.PrefsManager
import com.example.presenter.vm.auth.AuthViewModel
import com.example.presenter.vm.profile.ProfileViewModel
import com.example.zoomrad.MainViewModel
import com.example.zoomrad.presentation.screens.auth.LoginScreen
import com.example.zoomrad.presentation.screens.auth.VerifyOtpScreen
import com.example.zoomrad.presentation.screens.cards.CardsScreen
import com.example.zoomrad.presentation.screens.lock.AppLockScreen
import com.example.zoomrad.presentation.screens.profile.ProfileScreen
import com.example.zoomrad.presentation.screens.settings.SettingsScreen
import com.example.zoomrad.presentation.screens.splash.SplashScreen
import com.example.zoomrad.presentation.screens.tabs.home.HomeScreen
import com.example.zoomrad.presentation.screens.tabs.monitor.MonitoringScreen
import com.example.zoomrad.presentation.screens.tabs.payment.PaymentsScreen
import com.example.zoomrad.presentation.screens.tabs.service.AdditionalServicesScreen
import com.example.zoomrad.presentation.screens.tabs.transfer.TransferScreen

class AuthNavigation(
    private val navController: NavHostController,
    private val authViewModel: AuthViewModel,
    private val prefsManager: PrefsManager
) {
    fun register(builder: NavGraphBuilder) {
        builder.composable("splash") {
            SplashScreen(
                prefsManager = prefsManager,
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate("lock") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }
        builder.composable("login") {
            LoginScreen(navController, authViewModel) {
                navController.navigate("otp")
            }
        }
        builder.composable("otp") {
            VerifyOtpScreen(navController, viewModel = authViewModel, onBackToLogin = {
                navController.popBackStack()
            }, onVerifySuccess = {
                navController.navigate("lock") {
                    popUpTo("login") {
                        inclusive = true
                    }
                }
            })
        }
    }
}

class LockNavigation(
    private val navController: NavHostController,
    private val prefsManager: PrefsManager
) {
    fun register(builder: NavGraphBuilder) {
        builder.composable("lock") {
            AppLockScreen(
                prefsManager = prefsManager,
                onSuccess = {
                    navController.navigate(BottomNavItem.Asosiy.route) {
                        popUpTo("lock") { inclusive = true }
                    }
                }
            )
        }
    }
}

class HomeNavigation(
    private val navController: NavHostController,
    private val onOpenDrawer: () -> Unit
) {
    fun register(builder: NavGraphBuilder) {
        builder.composable(BottomNavItem.Asosiy.route) {
            HomeScreen(navController) {
                onOpenDrawer()
            }
        }
        builder.composable(BottomNavItem.Tolovlar.route) { PaymentsScreen() }
        builder.composable(BottomNavItem.Otkazma.route) { TransferScreen() }
        builder.composable(BottomNavItem.Monitoring.route) { MonitoringScreen() }
        builder.composable(BottomNavItem.Xizmatlar.route) { AdditionalServicesScreen() }
        builder.composable("cards") { CardsScreen(navController) }
    }
}

class SettingsNavigation(
    private val navController: NavHostController,
    private val mainViewModel: MainViewModel,
    private val profileViewModel: ProfileViewModel
) {
    fun register(builder: NavGraphBuilder) {
        builder.composable("settings") {
            SettingsScreen(mainViewModel) {
                navController.popBackStack()
            }
        }
        builder.composable("profile") {
            ProfileScreen(viewModel = profileViewModel, onBack = {
                navController.popBackStack()
            })
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    prefsManager: PrefsManager,
    mainViewModel: MainViewModel,
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel,
    onOpenDrawer: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.fillMaxSize()
    ) {
        AuthNavigation(navController, authViewModel, prefsManager).register(this)
        LockNavigation(navController, prefsManager).register(this)
        HomeNavigation(navController, onOpenDrawer).register(this)
        SettingsNavigation(navController, mainViewModel, profileViewModel).register(this)
    }
}
