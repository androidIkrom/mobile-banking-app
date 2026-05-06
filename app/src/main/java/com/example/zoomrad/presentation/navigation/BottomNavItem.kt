package com.example.zoomrad.presentation.navigation

import com.example.zoomrad.R

sealed class BottomNavItem(
    val title: String,
    val icon: Int,
    val route: String
) {
    object Asosiy : BottomNavItem("Asosiy", R.drawable.ic_home, "home")
    object Tolovlar : BottomNavItem("To'lovlar", R.drawable.ic_payment, "payments")
    object Otkazma : BottomNavItem("O'tkazma", R.drawable.ic_transfer, "transfer")
    object Monitoring : BottomNavItem("Monitoring", R.drawable.ic_history, "monitoring")
    object Xizmatlar : BottomNavItem("Xizmatlar", R.drawable.ic_profile, "services")
}
