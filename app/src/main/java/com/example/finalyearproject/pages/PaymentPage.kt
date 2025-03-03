import java.util.UUID
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.runtime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentPage(
    navController: NavHostController,
    cartItems: List<CartItem>,
    onPayment: (String, String, String, String) -> Unit // Callback to handle the payment
) {
    // State for the form fields
    var cardholderName by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expirationDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    // Calculating total price and tax
    val subtotal = remember { derivedStateOf { cartItems.sumOf { it.totalPrice * it.quantity } } }
    val tax = 10.00
    val totalPrice = remember { derivedStateOf { subtotal.value + tax } }

    // Generate a unique order number when the user starts the payment process
    val generateOrderNumber = UUID.randomUUID().toString().take(8) // Generate order number (8 chars)


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payment Details", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
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
        ) {
            // Cardholder Name Field
            OutlinedTextField(
                label = { Text("Cardholder Name") },
                value = cardholderName,
                onValueChange = { cardholderName = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("Enter your name") }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Card Number Field
            OutlinedTextField(
                label = { Text("Card Number") },
                value = cardNumber,
                onValueChange = { cardNumber = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("Enter card number") }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Expiration Date Field
            OutlinedTextField(
                label = { Text("Expiration Date (MM/YY)") },
                value = expirationDate,
                onValueChange = { expirationDate = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("MM/YY") }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // CVV Field
            OutlinedTextField(
                label = { Text("CVV") },
                value = cvv,
                onValueChange = { cvv = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("Enter CVV") }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Subtotal, Tax, Total Display
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Subtotal: R${String.format("%.2f", subtotal.value)}", color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Est. Tax: R${String.format("%.2f", tax)}", color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Total: R${String.format("%.2f", totalPrice.value)}",
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Pay Now Button
            Button(
                onClick = {
                    if (cardholderName.isNotEmpty() && cardNumber.isNotEmpty() && expirationDate.isNotEmpty() && cvv.isNotEmpty()) {
                        println("Navigating to confirmation screen with order number: $generateOrderNumber")
                        navController.navigate("confirmation_screen/$generateOrderNumber")
                    } else {
                        // Handle error (empty fields)
                        println("Error: Please fill all the payment details.")
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B)),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Pay Now", color = Color.White)
            }
        }
        }
}
