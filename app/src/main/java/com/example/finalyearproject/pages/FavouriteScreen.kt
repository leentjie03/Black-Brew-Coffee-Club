package com.example.finalyearproject.pages

import MenuItem
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember


import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(navController: NavController, favoriteItems: MutableList<MenuItem>) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val userUid = currentUser?.uid
    val database = FirebaseDatabase.getInstance().reference
    val favoriteItems = remember { mutableStateListOf<MenuItem>() }

    // Fetch favorites from Firebase Realtime Database
    LaunchedEffect(userUid) {
        if (userUid != null) {
            database.child("favorites").child(userUid).get().addOnSuccessListener { snapshot ->
                val items = snapshot.getValue<List<MenuItem>>() ?: emptyList()
                favoriteItems.clear()
                favoriteItems.addAll(items)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Favourites", color = Color.White) },
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
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                if (favoriteItems.isEmpty()) {
                    Text(
                        text = "There's nothing in your favourites",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start
                    ) {
                        items(favoriteItems) { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable { navController.navigate("product_page/${item.id}") }
                            ) {
                                AsyncImage(
                                    model = item.image_path,
                                    contentDescription = item.name,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = item.name,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "ZAR ${item.basePrice.format(2)}",
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )
                                }

                                IconButton(onClick = {
                                    favoriteItems.remove(item)
                                    removeFavoriteFromDatabase(userUid, item)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Remove",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}


fun addFavoriteToDatabase(userUid: String?, menuItem: MenuItem) {
    if (userUid == null) return
    val database = FirebaseDatabase.getInstance().reference
    val userFavoritesRef = database.child("favorites").child(userUid)

    userFavoritesRef.get().addOnSuccessListener { snapshot ->
        val favorites = snapshot.getValue<List<MenuItem>>()?.toMutableList() ?: mutableListOf()
        favorites.add(menuItem)
        userFavoritesRef.setValue(favorites)
    }
}



fun removeFavoriteFromDatabase(userUid: String?, menuItem: MenuItem) {
    if (userUid == null) return
    val database = FirebaseDatabase.getInstance().reference
    val userFavoritesRef = database.child("favorites").child(userUid)

    userFavoritesRef.get().addOnSuccessListener { snapshot ->
        val favorites = snapshot.getValue<List<MenuItem>>()?.toMutableList() ?: mutableListOf()
        favorites.removeIf { it.id == menuItem.id }
        userFavoritesRef.setValue(favorites)
    }
}