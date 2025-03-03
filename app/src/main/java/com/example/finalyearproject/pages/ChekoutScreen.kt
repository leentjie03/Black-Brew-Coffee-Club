package com.example.finalyearproject.pages

import CartItem
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavHostController,
    cartItems: List<CartItem>,
    redeemedPoints: Int,
    onPlaceOrder: (String) -> Unit // Callback for orderId after successful order
) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val userUid = currentUser?.uid
    val database = Firebase.database.reference

    val subtotal = remember { cartItems.sumOf { it.totalPrice * it.quantity } }
    val tax = 5.00
    val totalPrice = subtotal + tax - redeemedPoints

    // Points Calculation: 5 points for every R20 spent
    val pointsEarned = ((totalPrice / 20) * 5).toInt()


    // Payment fields state
    var cardholderName by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expirationDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }


    // Validation for payment fields
    val isValidCardNumber = cardNumber.matches(Regex("\\d{16}"))
    val isValidExpirationDate = expirationDate.matches(Regex("(0[1-9]|1[0-2])/\\d{2}"))
    val isValidCVV = cvv.matches(Regex("\\d{3}"))

    // Generate random orderId between 1 and 5000
    // Disable Place Order button if payment fields are not filled
    val isPaymentComplete = cardholderName.isNotEmpty() && cardNumber.isNotEmpty() && expirationDate.isNotEmpty() && cvv.isNotEmpty()

    // Generate random orderId between 1 and 5000
    val orderId = Random.nextInt(1, 5001).toString()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF3E4636))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Enables vertical scrolling
        ) {
            // Order Summary
            Text("Order Summary", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))
            cartItems.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Product Image
                    AsyncImage(
                        model = item.image_path,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(item.productName, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))

                        // Display options (size, milk, flavor, etc.)
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            if (!item.size.isNullOrEmpty()) Text("Size: ${item.size}")
                            if (!item.milk.isNullOrEmpty()) Text("Milk: ${item.milk}")
                            if (!item.sugar.isNullOrEmpty()) Text("Sugar: ${item.sugar}")
                            if (!item.flavor.isNullOrEmpty()) Text("Flavor: ${item.flavor}")
                            if (item.espressoShots > 0) Text("Espresso Shots: ${item.espressoShots}")
                            if (item.toppings.isNotEmpty()) Text("Toppings: ${item.toppings.joinToString(", ")}")
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text("R${String.format("%.2f", item.totalPrice)} x ${item.quantity}", color = Color(0xFF3E4636))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            Divider()

            // Pickup Location
            Spacer(modifier = Modifier.height(16.dp))
            Text("Pickup", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Location: 3 Diep in Die Berg, Wapadrand, Pretoria")
            Spacer(modifier = Modifier.height(16.dp))
            Divider()

            // Price Breakdown
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Subtotal")
                Text("R${String.format("%.2f", subtotal)}")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Tax")
                Text("R${String.format("%.2f", tax)}")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total", fontWeight = FontWeight.Bold)
                Text("R${String.format("%.2f", totalPrice)}", fontWeight = FontWeight.Bold)
            }

            // Payment Information
            // Payment Information
            Spacer(modifier = Modifier.height(24.dp))
            Text("Payment Information", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = cardholderName,
                onValueChange = { cardholderName = it },
                label = { Text("Cardholder Name") },
                placeholder = { Text("John Doe") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = cardNumber,
                onValueChange = { cardNumber = it },
                label = { Text("Card Number") },
                placeholder = { Text("4111 1111 1111 1111") },
                isError = !isValidCardNumber && cardNumber.isNotEmpty(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = expirationDate,
                    onValueChange = { expirationDate = it },
                    label = { Text("Expiration Date") },
                    placeholder = { Text("MM/YY") },
                    isError = !isValidExpirationDate && expirationDate.isNotEmpty(),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = cvv,
                    onValueChange = { cvv = it },
                    label = { Text("CVV") },
                    placeholder = { Text("123") },
                    isError = !isValidCVV && cvv.isNotEmpty(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }

            // Earned Points Section
            Spacer(modifier = Modifier.height(16.dp))
            Text("Points Earned: $pointsEarned points", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)


            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    if (userUid != null && isPaymentComplete) {
                        // Generate the orderId and place the order
                        val order = mapOf(
                            "orderId" to orderId,
                            "userId" to userUid,
                            "cartItems" to cartItems.map { it.toMap() },
                            "subtotal" to subtotal,
                            "tax" to tax,
                            "totalPrice" to totalPrice,
                            "timestamp" to System.currentTimeMillis()
                        )
                        database.child("orders").child(userUid).child(orderId).setValue(order)
                            .addOnSuccessListener {
                                Toast.makeText(navController.context, "Order placed successfully!", Toast.LENGTH_LONG).show()
                                onPlaceOrder(orderId) // Passing orderId to confirmation screen
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(navController.context, "Failed to place order: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                    }
                },
                enabled = isPaymentComplete, // Button is enabled only when payment info is complete
            ) {
                Text("Place Order", color = Color.White)
            }
        }
    }
}



fun CartItem.toMap(): Map<String, Any?> {
    return mapOf(
        "productName" to productName,
        "imagePath" to image_path,
        "totalPrice" to totalPrice,
        "quantity" to quantity,
        "size" to size,
        "milk" to milk,
        "flavor" to flavor,
        "espressoShots" to espressoShots,
        "toppings" to toppings
    )
}