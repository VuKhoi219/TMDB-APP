package com.example.tmdb_app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tmdb_app.ui.screens.home.HomeScreen
import com.example.tmdb_app.ui.screens.login.LoginScreen
import com.example.tmdb_app.ui.screens.splash.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController = navController)
        }
        composable("home") {
            HomeScreen()
        }
        composable("login"){
            LoginScreen(navController = navController)
        }
    }
}
