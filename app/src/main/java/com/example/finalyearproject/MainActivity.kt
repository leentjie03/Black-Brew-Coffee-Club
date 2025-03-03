package com.example.finalyearproject

import CartItem
import MenuItem
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.finalyearproject.pages.AccountScreen
import com.example.finalyearproject.pages.CartScreen
import com.example.finalyearproject.pages.CheckoutScreen
import com.example.finalyearproject.pages.ConfirmationScreen
import com.example.finalyearproject.pages.DashboardScreen
import com.example.finalyearproject.pages.FavoriteScreen
import com.example.finalyearproject.pages.ForgotPasswordScreen
import com.example.finalyearproject.pages.LoginScreen
import com.example.finalyearproject.pages.OrderHistoryScreen
import com.example.finalyearproject.pages.PersonalDetailsScreen
import com.example.finalyearproject.pages.PrivacySecurityScreen
import com.example.finalyearproject.pages.ProductDetailsScreen
import com.example.finalyearproject.pages.RewardsScreen
import com.example.finalyearproject.pages.SignUpScreen
import com.example.finalyearproject.pages.SplashScreen
import com.example.finalyearproject.pages.WelcomeScreen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.getValue


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoffeeApp()
        }
    }
}

@Composable
fun CoffeeApp() {
    val auth = FirebaseAuth.getInstance()
    val navController = rememberNavController()
    val cartItems = remember { mutableStateListOf<CartItem>() }



    // Checks if the user is already logged in
    val currentUser = auth.currentUser
    val startDestination = if (currentUser != null) "splash_screen" else "login_screen"
    val favoriteItems = remember { mutableStateListOf<MenuItem>() } // Correct way to initialize


    NavHost(navController = navController, startDestination = "dashboard_screen") {
        composable("splash_screen") { SplashScreen(navController) }
        composable("welcome_screen") { WelcomeScreen(navController) }
        composable("login_screen") { LoginScreen(navController) }
        composable("signup_screen") { SignUpScreen(navController) }
        //DASHBOARD
        composable("forgot_password_screen") { ForgotPasswordScreen(navController) }
        composable("dashboard_screen") { DashboardScreen(navController) }
        //productScreen
        composable("product_page/{productId}") { backStackEntry ->
            ProductDetailsScreen(
                navController = navController,
                productId = backStackEntry.arguments?.getString("productId") ?: "",
                favoriteItems = favoriteItems,
                cartItems = cartItems
            )
        }
        //Account
        composable("account_screen") { AccountScreen(navController) }
        composable("personal_details_screen") { PersonalDetailsScreen(navController) }
        composable("privacy_security_screen") { PrivacySecurityScreen(navController) }
        composable("order_history_screen") { OrderHistoryScreen() }


        //favorite
        composable("favorite_screen") {
            val auth = FirebaseAuth.getInstance()
            val currentUser = auth.currentUser
            val userUid = currentUser?.uid
            val database = FirebaseDatabase.getInstance().reference
            val favoriteItems = remember { mutableStateListOf<MenuItem>() }

            // Sync favorites with Firebase when the screen is displayed
            LaunchedEffect(userUid) {
                if (userUid != null) {
                    database.child("favorites").child(userUid).get()
                        .addOnSuccessListener { snapshot ->
                            val items = snapshot.getValue<List<MenuItem>>() ?: emptyList()
                            favoriteItems.clear()
                            favoriteItems.addAll(items)
                        }
                }
            }

            FavoriteScreen(navController, favoriteItems = favoriteItems)
        }
        //Cart
        composable("cart_screen") {
            CartScreen(
                navController = navController,
                cartItems = cartItems,
                onRemoveItem = { itemToRemove ->
                    cartItems.remove(itemToRemove)
                },
                onUpdateQuantity = { cartItem, newQuantity ->
                    val index = cartItems.indexOf(cartItem)
                    if (index != -1) {
                        // Update quantity or remove item if quantity is zero
                        if (newQuantity > 0) {
                            cartItems[index] = cartItem.copy(quantity = newQuantity)
                        } else {
                            cartItems.removeAt(index)
                        }
                    }
                },
                onOrder = {
                    Log.d("CartScreen", "Navigating to checkout screen...")
                    val redeemedPoints =  0// Replace this with the actual redeemed points value, if available
                    navController.navigate("checkout_screen/$redeemedPoints") // Navigate to checkout screen
                }
            )
        }


        composable("checkout_screen/{redeemedPoints}") { backStackEntry ->
            val redeemedPoints = backStackEntry.arguments?.getString("redeemedPoints")?.toInt() ?: 0
            CheckoutScreen(
                navController = navController,
                cartItems = cartItems,
                redeemedPoints = redeemedPoints,
                onPlaceOrder = { orderId ->
                    navController.navigate("confirmation_screen/$orderId")
                }
            )
        }

        composable("confirmation_screen/{orderId}") { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            ConfirmationScreen(orderId = orderId, navController = navController)
        }

// Vouchers Screen (added this composable)
        composable("rewards_screen") {
            RewardsScreen(navController = navController)
        }
    }}


@Composable
fun WhiteRoundedRectangle(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize() // Ensures the Box fills the entire screen
            .background(
                color = Color.White, // White background
                shape = RoundedCornerShape(30.dp)
            )
    ) {
        content() // Render the content inside the rectangle
    }
}

@Composable
fun SignInWithGoogle(
    googleSignInClient: GoogleSignInClient,
    firebaseAuth: FirebaseAuth,
    navController: NavHostController
) {
    val signInIntent = googleSignInClient.signInIntent
    (LocalContext.current as Activity).startActivityForResult(signInIntent, RC_SIGN_IN)
}

const val RC_SIGN_IN = 9001


fun handleSignInResult(
    data: Intent?,
    googleSignInClient: GoogleSignInClient,
    firebaseAuth: FirebaseAuth,
    navController: NavHostController
) {
    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
    try {
        val account = task.getResult(ApiException::class.java)
        firebaseAuthWithGoogle(account.idToken!!, firebaseAuth, navController)
    } catch (e: ApiException) {
        Log.w("LoginActivity", "Google sign-in failed", e)
    }
}

fun firebaseAuthWithGoogle(
    idToken: String,
    firebaseAuth: FirebaseAuth,
    navController: NavHostController
) {
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    firebaseAuth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign-in successful, navigate to the dashboard
                navController.navigate("dashboard_screen")
            } else {
                // Handle failure
                Log.w("LoginActivity", "signInWithCredential:failure", task.exception)
            }
        }
}




@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CoffeeApp()
}