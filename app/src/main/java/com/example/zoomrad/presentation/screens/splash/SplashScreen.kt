package com.example.zoomrad.presentation.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.entity.local.PrefsManager
import com.example.zoomrad.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    prefsManager: PrefsManager,
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit
) {

    val scale = remember { Animatable(0.2f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 3f,
            animationSpec = tween(
                durationMillis = 1600,
                easing = FastOutSlowInEasing
            )
        )

        delay(300)

        if (prefsManager.accessToken != null) {
            onNavigateToHome()
        } else {
            onNavigateToLogin()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    scaleX = scale.value
                    scaleY = scale.value
                }
        )
    }
}