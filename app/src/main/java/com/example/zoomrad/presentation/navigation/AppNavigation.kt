package com.example.zoomrad.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.entity.local.PrefsManager
import com.example.presenter.vm.auth.AuthViewModel
import com.example.presenter.vm.profile.ProfileViewModel
import com.example.zoomrad.MainViewModel
import com.example.zoomrad.presentation.screens.about.AboutScreen
import com.example.zoomrad.presentation.screens.applications.ApplicationScreen
import com.example.zoomrad.presentation.screens.auth.LoginScreen
import com.example.zoomrad.presentation.screens.auth.VerifyOtpScreen
import com.example.zoomrad.presentation.screens.cards.CardsScreen
import com.example.zoomrad.presentation.screens.help.HelpScreen
import com.example.zoomrad.presentation.screens.kyc.KycScreen
import com.example.zoomrad.presentation.screens.lock.AppLockScreen
import com.example.zoomrad.presentation.screens.profile.ProfileScreen
import com.example.zoomrad.presentation.screens.settings.NotificationScreen
import com.example.zoomrad.presentation.screens.settings.SettingsScreen
import com.example.zoomrad.presentation.screens.splash.SplashScreen
import com.example.zoomrad.presentation.screens.tabs.home.HomeScreen
import com.example.zoomrad.presentation.screens.tabs.loan.ApplyLoanScreen
import com.example.zoomrad.presentation.screens.tabs.loan.RepayLoanScreen
import com.example.zoomrad.presentation.screens.tabs.monitor.MonitoringScreen
import com.example.zoomrad.presentation.screens.tabs.payment.MakePaymentScreen
import com.example.zoomrad.presentation.screens.tabs.payment.PaymentReceiptScreen
import com.example.zoomrad.presentation.screens.tabs.payment.PaymentsScreen
import com.example.zoomrad.presentation.screens.tabs.service.AdditionalServicesScreen
import com.example.zoomrad.presentation.screens.tabs.transfer.CheckTransferScreen
import com.example.zoomrad.presentation.screens.tabs.transfer.TransferOtpScreen
import com.example.zoomrad.presentation.screens.tabs.transfer.TransferPinScreen
import com.example.zoomrad.presentation.screens.tabs.transfer.TransferScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
        builder.composable("reset_pin") {
            AppLockScreen(
                prefsManager = prefsManager,
                isResetMode = true,
                onSuccess = {
                    navController.popBackStack()
                }
            )
        }
    }
}

class HomeNavigation(
    private val navController: NavHostController,
    private val transferViewModel: com.example.presenter.vm.transfer.TransferViewModel,
    private val prefsManager: PrefsManager,
    private val onOpenDrawer: () -> Unit
) {
    fun register(builder: NavGraphBuilder) {
        builder.composable(BottomNavItem.Asosiy.route) {
            HomeScreen(navController) {
                onOpenDrawer()
            }
        }

        builder.composable(BottomNavItem.Tolovlar.route) { 
            PaymentsScreen(onProviderClick = { provider ->
                val encodedUrl = URLEncoder.encode(provider.logoUrl ?: "", StandardCharsets.UTF_8.toString())
                navController.navigate("make_payment/${provider.id}/${provider.name}/$encodedUrl")
            })
        }
        
        builder.composable(
            route = "make_payment/{providerId}/{providerName}/{logoUrl}",
            arguments = listOf(
                navArgument("providerId") { type = NavType.StringType },
                navArgument("providerName") { type = NavType.StringType },
                navArgument("logoUrl") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val providerId = backStackEntry.arguments?.getString("providerId") ?: ""
            val providerName = backStackEntry.arguments?.getString("providerName") ?: ""
            val logoUrl = URLDecoder.decode(backStackEntry.arguments?.getString("logoUrl") ?: "", StandardCharsets.UTF_8.toString())
            MakePaymentScreen(
                navController = navController,
                providerId = providerId,
                providerName = providerName,
                logoUrl = logoUrl
            )
        }

        builder.composable(
            route = "payment_receipt/{providerName}/{amount}/{account}/{logoUrl}",
            arguments = listOf(
                navArgument("providerName") { type = NavType.StringType },
                navArgument("amount") { type = NavType.StringType },
                navArgument("account") { type = NavType.StringType },
                navArgument("logoUrl") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val providerName = backStackEntry.arguments?.getString("providerName") ?: ""
            val amount = backStackEntry.arguments?.getString("amount") ?: ""
            val account = backStackEntry.arguments?.getString("account") ?: ""
            val logoUrl = URLDecoder.decode(backStackEntry.arguments?.getString("logoUrl") ?: "", StandardCharsets.UTF_8.toString())

            PaymentReceiptScreen(
                providerName = providerName,
                amount = amount,
                account = account,
                logoUrl = logoUrl,
                onClose = {
                    navController.navigate(BottomNavItem.Asosiy.route) {
                        popUpTo(BottomNavItem.Tolovlar.route) { inclusive = true }
                    }
                }
            )
        }
        builder.composable(BottomNavItem.Otkazma.route) {
            TransferScreen(navController, transferViewModel)
        }
        builder.composable("check_transfer") {
            CheckTransferScreen(
                navController = navController,
                transferViewModel = transferViewModel,
                cardViewModel = hiltViewModel(),
                prefsManager = prefsManager
            )
        }
        builder.composable(BottomNavItem.Monitoring.route) { MonitoringScreen() }
        builder.composable(BottomNavItem.Xizmatlar.route) { 
            AdditionalServicesScreen(navController) 
        }
        builder.composable("apply_loan") {
            ApplyLoanScreen(navController)
        }
        builder.composable("kyc") {
            KycScreen(navController)
        }
        builder.composable(
            route = "repay_loan/{loanId}/{monthlyPayment}",
            arguments = listOf(
                navArgument("loanId") { type = NavType.StringType },
                navArgument("monthlyPayment") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val loanId = backStackEntry.arguments?.getString("loanId") ?: ""
            val monthlyPayment = backStackEntry.arguments?.getString("monthlyPayment")?.toDoubleOrNull() ?: 0.0
            RepayLoanScreen(navController, loanId, monthlyPayment)
        }
        builder.composable("cards") { CardsScreen(navController) }
        builder.composable("transfer_otp") {
            TransferOtpScreen(
                viewModel = transferViewModel,
                onSuccess = {
                    navController.navigate(BottomNavItem.Asosiy.route) {
                        popUpTo(BottomNavItem.Otkazma.route) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        builder.composable("transfer_pin") {
            TransferPinScreen(
                viewModel = transferViewModel,
                onSuccess = {
                    navController.navigate(BottomNavItem.Asosiy.route) {
                        popUpTo(BottomNavItem.Otkazma.route) { inclusive = true }
                    }
                },
                onNavigateToOtp = {
                    navController.navigate("transfer_otp")
                }
            )
        }
    }
}

class SettingsNavigation(
    private val navController: NavHostController,
    private val mainViewModel: MainViewModel,
    private val profileViewModel: ProfileViewModel
) {
    fun register(builder: NavGraphBuilder) {
        builder.composable("settings") {
            SettingsScreen(
                viewModel = mainViewModel,
                cardViewModel = hiltViewModel(),
                onNotificationClick = {
                    navController.navigate("notifications")
                },
                onBack = {
                    navController.popBackStack()
                },
                onAddCardClick = {
                    navController.navigate("cards")
                }
            )
        }
        builder.composable("profile") {
            ProfileScreen(
                viewModel = profileViewModel,
                onBack = { navController.popBackStack() },
                onNavigateToKyc = { navController.navigate("kyc") }
            )
        }
        builder.composable("applications") {
            ApplicationScreen (onBack = { navController.popBackStack() })
        }
        builder.composable("help") {
            HelpScreen (onBack = { navController.popBackStack() })
        }
        builder.composable("about") {
            AboutScreen(onBack = { navController.popBackStack() })
        }
        builder.composable("notifications") {
            NotificationScreen (onBack = { navController.popBackStack() })
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
    transferViewModel: com.example.presenter.vm.transfer.TransferViewModel,
    onOpenDrawer: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.fillMaxSize()
    ) {
        AuthNavigation(navController, authViewModel, prefsManager).register(this)
        LockNavigation(navController, prefsManager).register(this)
        HomeNavigation(navController, transferViewModel, prefsManager, onOpenDrawer).register(this)
        SettingsNavigation(navController, mainViewModel, profileViewModel).register(this)
    }
}
