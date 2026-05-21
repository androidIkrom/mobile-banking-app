package com.example.zoomrad.presentation.navigation

import com.example.zoomrad.R

sealed class BottomNavItem(
    val titleRes: Int,
    val icon: Int,
    val route: String
) {
    object Asosiy : BottomNavItem(R.string.nav_home, R.drawable.ic_home, "home")
    object Tolovlar : BottomNavItem(R.string.nav_payments, R.drawable.payment, "payments")
    object Otkazma : BottomNavItem(R.string.nav_transfers, R.drawable.transfer, "transfer")
    object Monitoring : BottomNavItem(R.string.nav_monitoring, R.drawable.ic_history, "monitoring")
    object Xizmatlar : BottomNavItem(R.string.nav_services, R.drawable.ic_profile, "services")
}
