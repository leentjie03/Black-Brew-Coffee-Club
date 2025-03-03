@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.finalyearproject.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.finalyearproject.R

@Composable
fun ConfirmationScreen(orderId: String, navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Confirmation", style = typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF3E4636))
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .background(Color(0xFF3E4636))

                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(id = R.drawable.complete_icon),
                    contentDescription = "EmptyCart ",
                    modifier = Modifier.size(120.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(16.dp))
                // Display success message
                Text(
                    text = "Your order has been placed successfully! Pick up in 10-20 minutes",
                    style = typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color.Black),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Order Number: $orderId",
                    style = typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = Color.Black),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { navController.navigate("dashboard_screen") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("Back to Home", color = Color.Black)
                }
            }
        }
    )
}
