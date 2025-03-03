package com.example.finalyearproject.pages


import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun RewardsScreen(navController: NavHostController) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

    var points by remember { mutableStateOf(0) }
    var redeemAmount by remember { mutableStateOf(0) }
    var errorMessage by remember { mutableStateOf("") }

    // Fetch user points from Firebase when the screen is loaded
    LaunchedEffect(userId) {
        getUserPoints(userId) { userPoints ->
            points = userPoints
        }
    }

    val context = LocalContext.current

    // Redeem points logic
    fun redeemPoints() {
        if (redeemAmount <= points) {
            points -= redeemAmount
            updateUserPoints(userId, points)
            Toast.makeText(context, "Redeemed $redeemAmount points!", Toast.LENGTH_SHORT).show()

            // Navigate to checkout with redeemed points
            navController.navigate("checkout_screen/$redeemAmount")
        } else {
            Toast.makeText(context, "Not enough points!", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Rewards Screen", color = Color.White)  // Custom title text
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("dashboard_screen") }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },

                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF3E4636))
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) { Spacer(modifier = Modifier.height(80.dp))
            Text("Your Points: $points", style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(80.dp))

            // Redeem points input
            Text("Enter Points to Redeem:")
            BasicTextField(
                value = redeemAmount.toString(),
                onValueChange = {
                    redeemAmount = it.toIntOrNull() ?: 0
                    // Optionally add validation logic for redeemAmount
                    errorMessage = if (redeemAmount <= 0) "Invalid amount" else ""
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )

            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { redeemPoints() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Redeem Points", color = Color.White)
            }
        }
    }
}

// Function to fetch user's points from Firebase
fun getUserPoints(userId: String, onComplete: (Int) -> Unit) {
    val db = FirebaseDatabase.getInstance().reference
    db.child("users").child(userId).child("points").get().addOnSuccessListener { snapshot ->
        val points = snapshot.getValue(Int::class.java) ?: 0
        onComplete(points)
    }.addOnFailureListener {
        onComplete(0)
        // Optionally show a message to the user about the error
    }
}

// Function to update the user's points in Firebase
fun updateUserPoints(userId: String, points: Int) {
    val db = FirebaseDatabase.getInstance().reference
    db.child("users").child(userId).child("points").setValue(points).addOnFailureListener {
        // Optionally show a message to the user about the error
    }
}