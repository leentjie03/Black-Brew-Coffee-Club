package com.example.finalyearproject.pages // Or your package name

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.finalyearproject.R
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavController) {
    val scale = remember {
        Animatable(0f)
    }

    // Animation
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 2000,
                easing = {
                    OvershootInterpolator(4f).getInterpolation(it)
                }
            )
        )
        delay(3000L) // Adjust delay as needed

        // Navigates to the next screen after the splash screen
        navController.navigate("welcome_screen")
    }

    // Splash screen content
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.background),
                contentScale = androidx.compose.ui.layout.ContentScale.FillBounds)
    ) {
        // Vector Asset (Logo)
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.outline_moon), // Replace with your vector asset name
            contentDescription = "Logo",
            modifier = Modifier
                .scale(scale.value)
                .size(100.dp) // Adjust size as needed
        )

        // Text
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 200.dp) // Position the text below the vector asset
        ) {
            Text(
                text = "Sorcery Coffee Roasters", // Updated text
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White, // White text for contrast
                textAlign = TextAlign.Center
            )
        }
    }
}
