package com.example.finalyearproject.pages

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.finalyearproject.R
import com.example.finalyearproject.WhiteRoundedRectangle

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest


@Composable
fun SignUpScreen(navController: NavHostController) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    // Firebase Auth instance
    val auth = FirebaseAuth.getInstance()

    // Input validation error states
    var fullNameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFEFEFEF)) // Background color
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.rock_arch), // Replace with your actual image
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )

        // Sign Up Card
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 190.dp), // Align below the background image
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }

            WhiteRoundedRectangle {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Sign Up Header
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Sign Up",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = "Join the Sorcery Family!",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))





                    // Full Name TextField
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = {
                            fullName = it
                            fullNameError = it.isEmpty()
                        },
                        label = { Text("Name", color = Color.Black) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "User Icon",
                                tint = Color.Black
                            )
                        },
                        isError = fullNameError,
                        modifier = Modifier
                            .width(300.dp)
                            .padding(vertical = 8.dp),
                        singleLine = true,
                        shape = RoundedCornerShape(30.dp),
                        textStyle = LocalTextStyle.current.copy(color = Color.Black),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF00796B),
                            unfocusedBorderColor = Color.Gray,
                            errorBorderColor = Color.Red,
                        )
                    )
                    if (fullNameError) {
                        Text(
                            "Please enter your full name",
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Email TextField
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = !Patterns.EMAIL_ADDRESS.matcher(it).matches()
                        },
                        label = { Text("Email", color = Color.Black) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email Icon",
                                tint = Color.Black
                            )
                        },
                        isError = emailError,
                        modifier = Modifier
                            .width(300.dp)
                            .padding(vertical = 8.dp),
                        singleLine = true,
                        shape = RoundedCornerShape(30.dp),
                        textStyle = LocalTextStyle.current.copy(color = Color.Black),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF00796B),
                            unfocusedBorderColor = Color.Gray,
                            errorBorderColor = Color.Red,
                        )
                    )
                    if (emailError) {
                        Text(
                            "Invalid email format",
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password TextField
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it
                            passwordError = it.length < 8 // Example: Password must be at least 8 characters long
                        },
                        isError = passwordError, // Set isError directly
                        placeholder = { Text("Password", color = Color.Black) },
                        modifier = Modifier
                            .width(300.dp)
                            .padding(vertical = 8.dp)
                            .background(Color(0xFFF7F7F7)),
                        singleLine = true,
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    painter = painterResource(
                                        id = if (isPasswordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility
                                    ),
                                    contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                                )
                            }
                        },
                        shape = RoundedCornerShape(80.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF00796B),
                            unfocusedBorderColor = Color.Gray,
                            errorBorderColor = Color.Red,
                        )
                    )

                    if (passwordError) {
                        Text("Password must be at least 8 characters long", color = Color.Red)
                    }
                    Spacer(modifier = Modifier.height(18.dp))

                    // Sign Up Button
                    Button(
                        onClick = {
                            if (fullName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                                auth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            // User creation successful, navigate to sign in screen
                                            val user = auth.currentUser
                                            user?.let {
                                                // You can add user's full name to Firebase database if needed
                                                user.updateProfile(
                                                    UserProfileChangeRequest.Builder()
                                                        .setDisplayName(fullName)
                                                        .build()
                                                )
                                                // Navigate to sign in screen
                                                navController.navigate("login_screen")
                                            }
                                        } else {
                                            errorMessage = task.exception?.message ?: "Sign Up Failed."
                                        }
                                    }
                            } else {
                                errorMessage = "Please fill in all fields."
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3E4636)),
                        shape = RoundedCornerShape(30.dp)
                    ) {
                        Text("Sign Up", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Already have an account?
                    Row {
                        Text(text = "Already have an account?", color = Color.Gray, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Sign In",
                            color = Color(0xFF3E4636),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.clickable {
                                navController.navigate("login_screen") // Navigate to sign-in screen
                            }
                        )
                    }
                }
            }
        }
    }
}