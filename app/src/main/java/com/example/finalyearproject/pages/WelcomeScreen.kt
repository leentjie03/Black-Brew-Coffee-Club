package com.example.finalyearproject.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalyearproject.R
import androidx.navigation.NavHostController


@Composable
fun WelcomeScreen(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.intro_pic),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Column for the Text and Spacer
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome to Coffee App",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Row for Buttons at the Bottom
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp), // Adjust padding if necessary
            horizontalArrangement = Arrangement.spacedBy(16.dp) // Spacing between buttons
        ) {
            Button(
                onClick = { navController.navigate("login_screen") },
                modifier = Modifier.weight(1f), // Makes button take equal width
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Login", fontSize = 16.sp)
            }

            Button(
                onClick = { navController.navigate("signup_screen") },
                modifier = Modifier.weight(1f), // Makes button take equal width
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
            ) {
                Text(text = "Sign Up", fontSize = 16.sp)
            }
        }
    }
}
