package com.example.finalyearproject.pages

import CartItem
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.finalyearproject.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavHostController,
    cartItems: List<CartItem>,
    onRemoveItem: (CartItem) -> Unit,
    onUpdateQuantity: (CartItem, Int) -> Unit,
    onOrder: () -> Unit
) {
    val subtotal = remember { derivedStateOf { cartItems.sumOf { it.totalPrice * it.quantity } } }
    val tax = remember { derivedStateOf { subtotal.value * 0.10 } }
    val totalPrice = remember { derivedStateOf { subtotal.value + tax.value } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Cart List", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF3E4636))
            )
        },
        bottomBar = {

            NavigationBar(
                containerColor = Color(0xFF3E4636)){
                var selectedTab = remember{ mutableStateOf("Home_screen") }

                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("home_screen") },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White) },
                    label = { Text("Home", color = Color.White) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("favorite_screen") },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites", tint = Color.White) },
                    label = { Text("Favorites", color = Color.White) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("rewards_screen") },
                    icon = { Icon(Icons.Default.CardGiftcard, contentDescription = "Vouchers", tint = Color.White) },
                    label = { Text("Vouchers", color = Color.White) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("cart_screen") },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart", tint = Color.White) },
                    label = { Text("Cart", color = Color.White) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("account_screen") },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Account", tint = Color.White) },
                    label = { Text("Account", color = Color.White) }
                )
            }

            if (cartItems.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(30.dp)
                        )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Price Summary
                            PriceSummaryRow("Subtotal:", subtotal.value)
                            PriceSummaryRow("Tax:", tax.value)
                            PriceSummaryRow("Total:", totalPrice.value, isBold = true)
                        }
                        }
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total", fontWeight = FontWeight.Bold)
                        Text("R${String.format("%.2f", totalPrice.value)}", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            // Navigate to the Checkout Screen
                            val redeemedPoints = 0  // Use the actual logic to determine redeemed points
                            navController.navigate("checkout_screen/$redeemedPoints")

                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B)),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("Checkout", color = Color.White)
                    }
                }
            }
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier.fillMaxSize()



        ) {
            if (cartItems.isEmpty()) {
                // Display "Cart is empty" message with an image using Material3
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Use an Image with a drawable resource for empty cart
                    Image(
                        painter = painterResource(id = R.drawable.cart),  // Replace with your image resource
                        contentDescription = "EmptyCart ",
                        modifier = Modifier.size(120.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Cart Empty",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.Black
                    )
                    Text(
                        text = "Good food is always cooking! Go ahead and order some yummy items from the menu",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(45.dp)
                    )
                }
            } else {
                // Display cart items in a LazyColumn
                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    items(cartItems) { item ->
                        CartItemRow(
                            cartItem = item,
                            onRemoveItem = { onRemoveItem(item) },
                            onUpdateQuantity = { newQuantity -> onUpdateQuantity(item, newQuantity) }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}




@Composable
fun CartItemRow(
    cartItem: CartItem,
    onRemoveItem: () -> Unit,
    onUpdateQuantity: (Int) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 9.dp),
        modifier = Modifier.fillMaxWidth().height(130.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            AsyncImage(
                model = cartItem.image_path,
                contentDescription = null,
                modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(cartItem.productName, fontWeight = FontWeight.Bold)

                // Options (Size, Milk, Sugar, etc.)
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    if (!cartItem.size.isNullOrEmpty()) {
                        Text("Size: ${cartItem.size}", style = MaterialTheme.typography.bodySmall)
                    }
                    if (!cartItem.milk.isNullOrEmpty()) {
                        Text("Milk: ${cartItem.milk}", style = MaterialTheme.typography.bodySmall)
                    }
                    if (!cartItem.flavor.isNullOrEmpty()) {
                        Text("Ice: ${cartItem.flavor}", style = MaterialTheme.typography.bodySmall)
                    }
                    if (!cartItem.sugar.isNullOrEmpty()) {
                        Text("Sugar: ${cartItem.sugar}", style = MaterialTheme.typography.bodySmall)
                    }
                    if (cartItem.espressoShots > 0) {
                        Text("Espresso Shots: ${cartItem.espressoShots}", style = MaterialTheme.typography.bodySmall)
                    }
                    if (!cartItem.base.isNullOrEmpty()) {
                        Text("Base: ${cartItem.base}", style = MaterialTheme.typography.bodySmall)
                    }
                    if (cartItem.toppings.isNotEmpty()) {
                        Text(
                            "Toppings: ${cartItem.toppings.joinToString(", ")}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    if (cartItem.complimentaries.isNotEmpty()) {
                        Text(
                            "Complimentary Add-ons: ${cartItem.complimentaries.joinToString(", ")}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }


                Text("R${String.format("%.2f", cartItem.totalPrice)} ", color = Color(0xFF3E4636))
                Spacer(modifier = Modifier.height(10.dp))

            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { if (cartItem.quantity > 1) onUpdateQuantity(cartItem.quantity - 1) }) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease")
                }
                Text("${cartItem.quantity}")
                IconButton(onClick = { onUpdateQuantity(cartItem.quantity + 1) }) {
                    Icon(Icons.Default.Add, contentDescription = "Increase")
                }
            }
            IconButton(onClick = onRemoveItem) {
                Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color.Red)
            }
        }
    }
}

@Composable
fun PriceSummaryRow(label: String, amount: Double, isNegative: Boolean = false, isBold: Boolean = false) {
    val style = if (isBold) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = style)
        Text(
            text = if (isNegative) "-R${amount.format(2)}" else "R${amount.format(2)}",
            style = style,
            color = if (isNegative) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        )
    }
}