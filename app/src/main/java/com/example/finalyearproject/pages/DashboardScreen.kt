package com.example.finalyearproject.pages

import MenuItem
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase

@Composable
fun DashboardScreen(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    if (user == null) {
        navController.navigate("login_screen")
        return
    }

    val userName = user.displayName ?: user.email?.substringBefore('@') ?: "User"
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<String?>(null) }

    var menuItems by remember { mutableStateOf<List<MenuItem>>(emptyList()) }

    LaunchedEffect(Unit) {
        menuItems = fetchMenuItems()
    }

    // Filtered menu items based on search and category
    val filteredItems = remember(menuItems, selectedCategory, searchQuery) {
        menuItems.filter { item ->
            (selectedCategory == null || item.category == selectedCategory) &&
                    (item.name.contains(searchQuery, ignoreCase = true) ||
                            item.description.contains(searchQuery, ignoreCase = true))
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF3E4636)){
                var selectedTab = remember{mutableStateOf("home_screen")}

                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("dashboard_screen") },
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
                    onClick = {navController.navigate("rewards_screen") },
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.LightGray)
        ) {
            Text(
                text = "Welcome, $userName",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

//
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { androidx.compose.material.Text("Search Menu") },
                leadingIcon = {
                    androidx.compose.material.Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                singleLine = true
            )

            // Categories
            androidx.compose.material.Text(
                text = "Categories",
                style = androidx.compose.material.MaterialTheme.typography.h6,
                modifier = Modifier.padding(16.dp)
            )
            LazyRow {
                val categories = listOf("Coffee", "Iced Coffee", "Light Meals","Pizza", "Drinks","Freshly Baked", "Coffee Beans")
                items(categories) { category ->
                    CategoryButton(
                        category = category,
                        isSelected = selectedCategory == category,
                        onClick = {
                            selectedCategory = if (selectedCategory == category) null else category
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Menu Items
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(filteredItems) { item ->
                    MenuItemCard(item, onClick = {
                        navController.navigate("product_page/${item.id}")
                    })
                }}}}}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    var showLogoutDialog by remember { mutableStateOf(false) }
    val database = FirebaseDatabase.getInstance().reference // Firebase Realtime Database reference
    var showDeleteDialog by remember { mutableStateOf(false) }
    val user = auth.currentUser


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Settings", color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("dashboard_screen") }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.LightGray
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.LightGray)
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = { /* Handle search input */ },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.Black
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00796B),
                    unfocusedBorderColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            AccountItem(
                icon = Icons.Default.Person,
                text = "Personal Details",
                onClick = { navController.navigate("personal_details_screen") }
            )

            AccountItem(
                icon = Icons.Default.Lock,
                text = "Privacy & Security",
                onClick = { navController.navigate("privacy_security_screen") }
            )

            AccountItem(
                icon = Icons.AutoMirrored.Filled.List,
                text = "Order History",
                onClick = { navController.navigate("order_history_screen") }
            )

            // Option to delete the account
            AccountItem(
                icon = Icons.Filled.Delete,
                text = "Delete Account",

                onClick = { showDeleteDialog = true }
            )

            Spacer(modifier = Modifier.weight(1f))


            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = "Log Out",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showLogoutDialog = true }
                        .padding(170.dp, 16.dp)
                )
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Log Out") },
            text = { Text("Are you sure you want to log out?") },
            confirmButton = {
                TextButton(onClick = {
                    auth.signOut()
                    navController.navigate("login_screen") {
                        popUpTo("account_screen") { inclusive = true }
                    }
                    showLogoutDialog = false
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("No")
                }
            }
        )
    }

// Delete account confirmation dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Account") },
            text = { Text("Are you sure you want to delete your account? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    val userId = auth.currentUser?.uid
                    if (userId == null) {
                        Toast.makeText(context, "User is not authenticated", Toast.LENGTH_SHORT)
                            .show()
                        return@TextButton
                    }

                    // Delete user data from Firebase Realtime Database
                    database.child("users").child(userId).removeValue()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("AccountScreen", "User data deleted successfully.")

                                // Now delete the Firebase Authentication account
                                auth.currentUser?.delete()?.addOnCompleteListener { deleteTask ->
                                    if (deleteTask.isSuccessful) {
                                        Log.d(
                                            "AccountScreen",
                                            "Firebase Authentication account deleted."
                                        )

                                        // Successfully deleted, navigate to the welcome screen
                                        navController.navigate("login_screen") {
                                            popUpTo("account_screen") { inclusive = true }
                                        }

                                        // Optionally, also show a confirmation Toast
                                        Toast.makeText(
                                            context,
                                            "Account deleted successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    } else {
                                        Log.e(
                                            "AccountScreen",
                                            "Error deleting Firebase authentication account",
                                            deleteTask.exception
                                        )
                                        Toast.makeText(
                                            context,
                                            "Error deleting account from Firebase Authentication",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            } else {
                                Log.e(
                                    "AccountScreen",
                                    "Error deleting user data from Firebase Database",
                                    task.exception
                                )
                                Toast.makeText(
                                    context,
                                    "Error deleting account from Firebase Database",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    showDeleteDialog = false // Close the dialog
                }) {
                    Text("Delete Account", color = Color(0xFF3E4636)) // Set the text color here
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("No thanks", color = Color(0xFF3E4636)) // Set the text color here
                }
            }
        )
    }
}


@Composable
fun PersonalDetailsScreen(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    var name by remember { mutableStateOf(user?.displayName ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var isSaving by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.clickable { navController.navigate("account_screen") }
            )
            Text("Edit Details", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(
                text = if (isSaving) "Saving..." else "Save",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    isSaving = true
                    // Update user details in Firebase
                    user?.let {
                        val profileUpdates = userProfileChangeRequest {
                            displayName = name
                        }
                        it.updateProfile(profileUpdates)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    it.updateEmail(email).addOnCompleteListener {
                                        isSaving = false
                                    }
                                }
                            }
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Account Details
        Text("Account Details", fontWeight = FontWeight.Bold, fontSize = 16.sp)

        Spacer(modifier = Modifier.height(8.dp))

        // Name Field
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Edit Password Button
        Text(
            text = "Edit Password",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clickable { navController.navigate("privacy_security_screen") }
                .padding(vertical = 8.dp)
        )
    }
}


@Composable
fun PrivacySecurityScreen(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isUpdating by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.clickable { navController.popBackStack() }
            )
            Text("Update Password", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Current Password Field
        OutlinedTextField(
            value = currentPassword,
            onValueChange = { currentPassword = it },
            label = { Text("Current Password") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // New Password Field
        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("New Password") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Confirm New Password Field
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm New Password") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Error Message
        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
        }

        // Update Password Button
        Button(
            onClick = {

                if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    errorMessage = "Please fill in all fields."
                }
                else if (newPassword == confirmPassword) {
                    isUpdating = true


                    // Re-authenticate user with current password
                    val credential = EmailAuthProvider.getCredential(user?.email ?: "", currentPassword)
                    user?.reauthenticate(credential)?.addOnCompleteListener { reauthTask ->
                        if (reauthTask.isSuccessful) {
                            user.updatePassword(newPassword).addOnCompleteListener { updateTask ->
                                isUpdating = false
                                if (updateTask.isSuccessful) {
                                    navController.popBackStack()
                                } else {
                                    errorMessage = "Failed to update password."
                                }
                            }
                        } else {
                            isUpdating = false
                            errorMessage = "Current password is incorrect."
                        }
                    }
                } else {
                    errorMessage = "Passwords do not match."
                }
            },
            modifier = Modifier.align(Alignment.End),
            enabled = !isUpdating
        ) {
            Text(if (isUpdating) "Updating..." else "Update Password")
        }
    }
}



@Composable
fun OrderHistoryScreen() {
    val orderHistory = remember { mutableStateOf(listOf<String>()) } // Replace with actual data

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(35.dp)
    ) {
        Text(
            text = "Order History",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (orderHistory.value.isEmpty()) {
            Text(
                text = "You don't have an order history yet, shop with us!",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            // Display order history
            LazyColumn {
                items(orderHistory.value) { order ->
                    Text(text = order, modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}


@Composable
fun AccountItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, color = Color.Black, fontSize = 18.sp)
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Go to $text",
            tint = Color.Black
        )
    }
}

@Composable
fun CategoryButton(category: String, isSelected: Boolean, onClick: () -> Unit) {
    androidx.compose.material.Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (isSelected) Color(0xFF3E4636) else Color.White
        ),
        modifier = Modifier.padding(end = 8.dp)
    ) {
        androidx.compose.material.Text(
            text = category,
            color = if (isSelected) Color.White else Color.Black
        )
    }
}