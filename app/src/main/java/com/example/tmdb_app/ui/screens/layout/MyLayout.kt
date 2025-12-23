package com.example.tmdb_app.ui.screens.layout

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlaylistAddCheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tmdb_app.R
import com.example.tmdb_app.ui.screens.download.DownloadScreen
import com.example.tmdb_app.ui.screens.explore.ExploreScreen
import com.example.tmdb_app.ui.screens.home.HomeScreen
import com.example.tmdb_app.ui.screens.home.SeeAll
import com.example.tmdb_app.ui.screens.myList.MyListScreen
import com.example.tmdb_app.ui.screens.profile.ProfileScreen
import com.example.tmdb_app.ui.theme.Red20

// 1. Định nghĩa các Route (đường dẫn) cho màn hình
sealed class Screen(
    val route: String,
    @StringRes val labelResId: Int, // Lưu ID của string thay vì string trực tiếp
    val icon: ImageVector
) {
    object Home : Screen("home", R.string.home, Icons.Default.Home)
    object Explore : Screen("explore", R.string.explore, Icons.Default.Explore)
    object MyList : Screen("mylist", R.string.my_list, Icons.Default.PlaylistAddCheckCircle)
    object Download : Screen("download", R.string.download, Icons.Default.Download)
    object Profile : Screen("profile", R.string.profile, Icons.Default.Person)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController() // Bộ điều khiển chuyển cảnh

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val items = listOf(Screen.Home, Screen.Explore, Screen.MyList, Screen.Download, Screen.Profile)

                items.forEach { screen ->
                    // Lấy String thực tế từ Resource ID
                    val labelString = stringResource(id = screen.labelResId)

                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = screen.icon,
                                contentDescription = labelString // Dùng chuỗi đã lấy
                            )
                        },
                        label = {
                            Text(text = labelString) // Dùng chuỗi đã lấy
                        },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Red20,
                            selectedTextColor = Red20,
                            unselectedIconColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        // 2. Khu vực hiển thị nội dung (NavHost)
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(
                navController = navController
            ) }
            composable(Screen.Explore.route) { ExploreScreen() }
            composable(Screen.MyList.route) { MyListScreen() }
            composable(Screen.Download.route) { DownloadScreen() }
            composable(Screen.Profile.route) { ProfileScreen() }
            composable(
                route = "see_all/{title}/{api}",
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
}
