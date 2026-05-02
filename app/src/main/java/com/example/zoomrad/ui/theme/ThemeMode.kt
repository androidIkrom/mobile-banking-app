package com.example.zoomrad.ui.theme

sealed class AppTheme(val title : String) {
    object Light : AppTheme("light")
    object Dark : AppTheme("Dark")
    object System : AppTheme("System")
}