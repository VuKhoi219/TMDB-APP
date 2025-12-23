package com.example.tmdb_app.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tmdb_app.ui.screens.home.HomeScreen
import com.example.tmdb_app.ui.screens.home.SeeAll
import com.example.tmdb_app.ui.screens.login.LoginScreen
import com.example.tmdb_app.ui.screens.splash.SplashScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Home : Screen("home")
    object SeeAll : Screen("see_all/{title}/{api}") {
        fun createRoute(title: String, api: Int) = "see_all/$title/$api"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(
            route = Screen.SeeAll.route,
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("api") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val api = backStackEntry.arguments?.getInt("api") ?: 0
            SeeAll(
                title = title,
                api = api,
                onClick = { navController.popBackStack() }
            )
        }
    }
}