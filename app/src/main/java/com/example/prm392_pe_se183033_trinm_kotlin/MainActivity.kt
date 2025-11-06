package com.example.prm392_pe_se183033_trinm_kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.prm392_pe_se183033_trinm_kotlin.ui.screen.FavoritesScreen
import com.example.prm392_pe_se183033_trinm_kotlin.ui.screen.NewsDetailScreen
import com.example.prm392_pe_se183033_trinm_kotlin.ui.screen.NewsListScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                NewsApp()
            }
        }
    }
}

@Composable
fun NewsApp() {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Danh sách") },
                    selected = currentRoute == "list",
                    onClick = {
                        navController.navigate("list") {
                            popUpTo("list") { inclusive = true }
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
                    label = { Text("Yêu thích") },
                    selected = currentRoute == "favorites",
                    onClick = {
                        navController.navigate("favorites") {
                            popUpTo("list") { inclusive = false }
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "list",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("list") {
                NewsListScreen(
                    onNewsClick = { newsId ->
                        navController.navigate("detail/$newsId")
                    }
                )
            }
            composable("favorites") {
                FavoritesScreen(
                    onNewsClick = { newsId ->
                        navController.navigate("detail/$newsId")
                    }
                )
            }
            composable("detail/{newsId}") { backStackEntry ->
                val newsId = backStackEntry.arguments?.getString("newsId")?.toIntOrNull() ?: 0
                NewsDetailScreen(
                    newsId = newsId,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}