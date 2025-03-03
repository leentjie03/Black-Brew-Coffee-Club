package com.example.finalyearproject.pages

import CartItem
import MenuItem
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await



@Composable
fun MenuItemCard(item: MenuItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        elevation = 4.dp
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = item.image_path,
                contentDescription = item.name,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = item.name, style = MaterialTheme.typography.h6)
                Text(text = item.description, style = MaterialTheme.typography.body2)
                Text(text = "Price: R${"%.2f".format(item.basePrice)}", style = MaterialTheme.typography.body1)
            }
        }
    }
}


// Suspend function for fetching menu items from Firebase
suspend fun fetchMenuItems(): List<MenuItem> {
    val databaseRef = FirebaseDatabase.getInstance().getReference("products")
    return try {
        val snapshot = databaseRef.get().await()
        snapshot.children.mapNotNull { childSnapshot ->
            childSnapshot.getValue(MenuItem::class.java)?.copy(id = childSnapshot.key ?: "")
        }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}






@Composable
fun ProductDetailsScreen(
    navController: NavHostController,
    productId: String,
    favoriteItems: MutableList<MenuItem>,
    cartItems: MutableList<CartItem>
) {
    val quantity = remember { mutableStateOf(1) }
    val product = remember { mutableStateOf<MenuItem?>(null) }
    val isFavorite = remember { mutableStateOf(false) }
    val selectedSize = remember { mutableStateOf("Small") }
    val selectedMilk = remember { mutableStateOf("No Milk") }
    val selectedSugar = remember { mutableStateOf("No Sugar") }
    val espressoShots = remember { mutableStateOf(0) }
    val totalPrice = remember { mutableStateOf(0.00) }
    val selectedComplimentaries = remember { mutableStateOf(mutableSetOf<String>()) }
    val selectedBase = remember { mutableStateOf("Regular Base") }
    val selectedToppings = remember { mutableStateOf(mutableSetOf<String>()) }
    val selectedFlavor = remember { mutableStateOf("") }
    val database = FirebaseDatabase.getInstance().reference
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val userUid = currentUser?.uid




    // Flavors for Ice Tea and Powerade
    val iceTeaFlavors = listOf("Cling Peach", "Hibiscus & Vanilla", "Light Lemon", "Mint & Lime")
    val poweradeFlavors = listOf("Island Blast", "Jagged Ice", "Mountain Blast", "Naartjie", "Orange")


    val milkOptions = listOf("No Milk", "Whole Milk", "Skim Milk", "Almond Milk", "Soy Milk")
    val sugarOptions = listOf("No Sugar", "White Sugar", "Brown Sugar", "Artificial Sweetener")




    // Fetch product details when the screen is loaded
    LaunchedEffect(productId) {
        product.value = fetchMenuItemById(productId)
        isFavorite.value = favoriteItems.any { it.id == productId }
        product.value?.let {
            totalPrice.value = it.basePrice
        }
    }

    // Main UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        product.value?.let { item ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Top Row: Back Button + Favorite Icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF3E4636)
                        )
                    }
                    IconButton(
                        onClick = {
                            if (isFavorite.value) {
                                favoriteItems.removeIf { it.id == productId }
                                removeFavoriteFromDatabase(userUid, item)
                                isFavorite.value = false
                            } else {
                                favoriteItems.add(item)
                                addFavoriteToDatabase(userUid, item)
                                isFavorite.value = true
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (isFavorite.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite.value) Color.Red else Color.Gray
                        )
                    }
                }








                // Product Image
                AsyncImage(
                    model = item.image_path,  // URL or path to the image
                    contentDescription = item.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Product Name and Description

                Text(
                    text = item.name,
                    style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .fillMaxWidth() // Make the Text span the entire width of its parent
                        .wrapContentWidth(Alignment.CenterHorizontally) // Center the Text horizontally
                        .padding(bottom = 8.dp) // Add padding below the Text
                )

                // Base Price (under product name)
                Text(
                    text = "ZAR ${item.basePrice.format(2)}",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier
                        .fillMaxWidth() // Make the Text span the entire width of its parent
                        .wrapContentWidth(Alignment.CenterHorizontally) // Center the Text horizontally
                        .padding(bottom = 8.dp)
                )

                Text(
                    text = item.description,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .fillMaxWidth() // Make the Text span the entire width of its parent
                        .wrapContentWidth(Alignment.CenterHorizontally) // Center the Text horizontally
                        .padding(bottom = 8.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Size Selector
                if (item.category.equals("Coffee", ignoreCase = true) || item.category.equals("Iced Coffee", ignoreCase = true)) {
                    Text(
                        text = "Choose Size:",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    item.sizes.forEach { (size, adjustment) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    selectedSize.value = size
                                    totalPrice.value = item.basePrice + adjustment + espressoShots.value * 10.90
                                }
                        ) {
                            RadioButton(
                                selected = selectedSize.value == size,
                                onClick = {
                                    selectedSize.value = size
                                    totalPrice.value = item.basePrice + adjustment + espressoShots.value * 10.90
                                },
                                modifier = Modifier.padding(end = 8.dp),
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFF3E4636),  // Color when selected
                                    unselectedColor = Color.Gray,  // Color when unselected
                                    disabledColor = Color.LightGray  // Color when disabled
                                )
                            )
                            Text("$size (+R${adjustment.format(2)})")
                        }
                    }

                }

                Spacer(modifier = Modifier.height(16.dp))

                // Conditional rendering for Milk and Sugar Options
                if (item.category.equals("Coffee", ignoreCase = true) || item.category.equals("Iced Coffee", ignoreCase = true)) {

                    // Milk Options
                    Text(
                        text = "Choose Milk Option:",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    milkOptions.forEach { milkOption ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = selectedMilk.value == milkOption,
                                onClick = {
                                    selectedMilk.value = milkOption
                                },
                                modifier = Modifier.padding(end = 8.dp),
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFF3E4636),  // Color when selected
                                    unselectedColor = Color.Gray,  // Color when unselected
                                    disabledColor = Color.LightGray  // Color when disabled
                                )

                            )
                            Text(milkOption)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Sugar Options
                    Text(
                        text = "Choose Sugar Option:",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    sugarOptions.forEach { sugarOption ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    selectedSugar.value = sugarOption
                                }
                        ) {
                            RadioButton(
                                selected = selectedSugar.value == sugarOption,
                                onClick = {
                                    selectedSugar.value = sugarOption
                                },
                                modifier = Modifier.padding(end = 8.dp),
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFF3E4636),  // Color when selected
                                    unselectedColor = Color.Gray,  // Color when unselected
                                    disabledColor = Color.LightGray  // Color when disabled
                                )
                            )
                            Text(sugarOption)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Espresso Shots (always available for Coffee)
                if (item.category.equals("Coffee", ignoreCase = true)) {
                    Text(
                        text = "Espresso Shots :",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                if (espressoShots.value > 0) {
                                    espressoShots.value -= 1
                                    totalPrice.value -= 10.90
                                }
                            },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_remove_24),
                                contentDescription = "Decrease",
                                tint = Color.Gray
                            )
                        }
                        Text(
                            text = espressoShots.value.toString(),
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        IconButton(
                            onClick = {
                                espressoShots.value += 1
                                totalPrice.value += 10.90
                            },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_add_24),
                                contentDescription = "Increase",
                                tint = Color.Black
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Pizza Base Options
                if (item.category.equals("Pizza", ignoreCase = true)) {
                    // Pizza Base Section
                    Text(
                        text = "Choose Base Option:",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Fetch pizza base options
                    val baseOptions = item.pizzaBase ?: emptyMap()
                    baseOptions.forEach { (base, adjustment) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = selectedBase.value == base, // Reflect selected state
                                onClick = {
                                    // Update selected base and total price
                                    selectedBase.value = base
                                    totalPrice.value = item.basePrice + adjustment +
                                            calculateToppingsPrice(selectedToppings.value, item.toppings)
                                },
                                modifier = Modifier.padding(end = 8.dp),
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFF3E4636),
                                    unselectedColor = Color.Gray
                                )
                            )
                            Text("$base (+R${adjustment.format(2)})")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Pizza Toppings Section
                    Text(
                        text = "Choose Toppings:",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Fetch pizza toppings
                    val toppings = item.toppings ?: emptyMap()
                    toppings.forEach { (topping, price) ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            // Checkbox for toppings
                            Checkbox(
                                checked = selectedToppings.value.contains(topping), // Reflect selected state
                                onCheckedChange = { isChecked ->
                                    val updatedToppings = selectedToppings.value.toMutableSet()
                                    if (isChecked) {
                                        // Add topping to updatedToppings
                                        updatedToppings.add(topping)
                                    } else {
                                        // Remove topping from updatedToppings
                                        updatedToppings.remove(topping)
                                    }
                                    // Update state with new set and trigger recomposition
                                    selectedToppings.value = updatedToppings
                                    // Update total price
                                    totalPrice.value = item.basePrice +
                                            (baseOptions[selectedBase.value] ?: 0.0) +
                                            calculateToppingsPrice(updatedToppings, toppings)
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color(0xFF3E4636),
                                    uncheckedColor = Color.Gray
                                )
                            )
                            Text("$topping (+R${price.format(2)})")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Complementary Items Section
                if (item.complimentaries?.isNotEmpty() == true) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Choose Complimentary:",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Initialize the selectedComplementaries state to track selected items
                    val selectedComplimentaries = remember { mutableStateOf(mutableSetOf<String>()) }

                    // Show a message if the user has already selected two items
                    val canSelectMore = selectedComplimentaries.value.size < 2

                    // Iterate through the complementary items and display each with a checkbox
                    item.complimentaries.forEach { complimentary ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            // Checkbox for complementary items
                            Checkbox(
                                checked = selectedComplimentaries.value.contains(complimentary), // Reflect selected state
                                onCheckedChange = { isChecked ->
                                    if (isChecked && selectedComplimentaries.value.size < 2) {
                                        // Add complementary to updated list
                                        val updatedComplimentaries = selectedComplimentaries.value.toMutableSet()
                                        updatedComplimentaries.add(complimentary)
                                        selectedComplimentaries.value = updatedComplimentaries
                                    } else if (!isChecked) {
                                        // Remove complementary from list if unchecked
                                        val updatedComplimentaries = selectedComplimentaries.value.toMutableSet()
                                        updatedComplimentaries.remove(complimentary)
                                        selectedComplimentaries.value = updatedComplimentaries
                                    }
                                    // Update total price (no change since complementaries are free)
                                    // You can add logic here if you want to show a message about selection limits.
                                },
                                enabled = canSelectMore || selectedComplimentaries.value.contains(complimentary), // Disable if two items are already selected
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color(0xFF3E4636), // Customize checked color
                                    uncheckedColor = Color.Gray // Customize unchecked color
                                )
                            )
                            // Display the name of the complementary item
                            Text(complimentary)

                            // Show a message if the user can't select more
                            if (!canSelectMore) {
                                Text(
                                    text = "You can select a maximum of 2 complimentary items.",
                                    style = MaterialTheme.typography.body2.copy(color = Color.Red),
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(15.dp))

                // Flavor Selection for Drinks (Ice Tea & Powerade)
                if (item.category.equals("drinks", ignoreCase = true) &&
                    (item.name.equals("Ice Tea", ignoreCase = true) || item.name.equals("Powerade", ignoreCase = true))) {
                    Text(
                        text = "Choose Flavor:",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Displays available flavors for selection based on the item name
                    val availableFlavors = when (item.name) {
                        "Ice Tea" -> iceTeaFlavors
                        "Powerade" -> poweradeFlavors
                        else -> emptyList()
                    }

                    availableFlavors.forEach { flavor ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = selectedFlavor.value == flavor,
                                onClick = {
                                    selectedFlavor.value = flavor
                                    totalPrice.value = item.basePrice // Add any flavor-related price adjustments if needed
                                },
                                modifier = Modifier.padding(end = 8.dp),
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFF3E4636),
                                    unselectedColor = Color.Gray
                                )
                            )
                            Text(flavor)
                        }
                    }
                }



                // Quantity Selector
                    Text(
                        text = "Quantity :",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) {
                    IconButton(
                        onClick = {
                            if (quantity.value > 1) {
                                quantity.value -= 1
                                totalPrice.value = item.basePrice * quantity.value
                            }
                        },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_remove_24),
                            contentDescription = "Decrease",
                            tint = Color.Gray
                        )
                    }

                    Text(
                        text = quantity.value.toString(),
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    IconButton(
                        onClick = {
                            quantity.value += 1
                            totalPrice.value = item.basePrice * quantity.value
                        },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_add_24),
                            contentDescription = "Increase",
                            tint = Color.Black
                        )
                    } }

                // Add to Cart Button
                Spacer(modifier = Modifier.height(50.dp))  // This spacer pushes the button to the bottom
            }




            // Add to Cart Button at the Bottom
            Button(
                onClick = {
                    product.value?.let { item ->
                        val cartItem = CartItem(
                            productName = item.name,
                            quantity = quantity.value,
                            flavor = selectedFlavor.value,
                            size = if (item.category == "Coffee") selectedSize.value else null, // Only for Coffee
                            milk = if (item.category == "Coffee") selectedMilk.value else null, // Only for Coffee
                            sugar = if (item.category == "Coffee") selectedSugar.value else null, // Only for Coffee
                            espressoShots = if (item.category == "Coffee") espressoShots.value else 0, // Only for Coffee
                            base = if (item.pizzaBase != null) selectedBase.value else null, // Only for items with a base
                            toppings = selectedToppings.value.toList(), // Toppings are optional
                            complimentaries = selectedComplimentaries.value.toList(), // Complimentary items are optional
                            totalPrice = totalPrice.value,
                             image_path = item.image_path
                        )

                        cartItems.add(cartItem) // Add to shared cart
                        navController.navigate("cart_screen") // Navigate to Cart
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF3E4636)),
                shape = RoundedCornerShape(30.dp)
            ) {
                Text("Add to Cart | R${totalPrice.value.format(2)}", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

        }

        }
        }





// Fetch product details by ID
suspend fun fetchMenuItemById(productId: String): MenuItem? {
    val databaseRef = FirebaseDatabase.getInstance().getReference("products").child(productId)
    return try {
        val snapshot = databaseRef.get().await()
        if (snapshot.exists()) {
            snapshot.getValue(MenuItem::class.java)?.copy(id = snapshot.key ?: "")
        } else {
            null // Product not found
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
// Helper function to calculate the total price of selected toppings
fun calculateToppingsPrice(selectedToppings: Set<String>, toppings: Map<String, Double>?): Double {
    return selectedToppings.sumOf { topping -> toppings?.get(topping) ?: 0.0 }
}

fun calculateComplimentaryPrice(selectedComplimentaries: Set<String>): Double {
    // Assuming each complementary item costs a fixed amount (e.g., R10.00)
    val pricePerComplimentary = 10.00
    return selectedComplimentaries.size * pricePerComplimentary
}


// Utility function to format a double value to two decimal places
fun Double.format(digits: Int) = "%.${digits}f".format(this)