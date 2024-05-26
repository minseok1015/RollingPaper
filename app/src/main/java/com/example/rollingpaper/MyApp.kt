package com.example.rollingpaper

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rollingpaper.homePage.homeScreen
import com.example.rollingpaper.mainPage.mainPageScreen

@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "homeScreen") {
        composable("homeScreen") { homeScreen(navController) }
        composable("mainPageScreen") { mainPageScreen() }
        // Add other composable destinations here
    }
}