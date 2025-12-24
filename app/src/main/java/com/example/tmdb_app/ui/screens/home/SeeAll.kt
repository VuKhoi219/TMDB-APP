package com.example.tmdb_app.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.tmdb_app.model.Movie
import com.example.tmdb_app.service.ApiService
import com.example.tmdb_app.ui.widget.ColumnMovie

@ExperimentalMaterial3Api
@Composable
fun SeeAll(title: String,api : Int,onClick: () -> Unit, modifier: Modifier = Modifier, ) {
    val apiService = ApiService()

    var items by remember { mutableStateOf<List<Movie>>(emptyList()) }
    
    LaunchedEffect(api) {
        when (api) {
            1 -> apiService.getTopMovies(
                onSuccess = { items = it?.results ?: emptyList() },
                onError = { println(it) }
            )
            2 -> apiService.getTopMovies(
                onSuccess = { items = it?.results ?: emptyList() },
                onError = { println(it) }
            )
            3 -> apiService.getNewMovies(
                onSuccess = { items = it?.results ?: emptyList() },
                onError = { println(it) }
            )
            else -> apiService.getPopularMovies(
                onSuccess = { items = it?.results ?: emptyList() },
                onError = { println(it) }
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = title) },
                navigationIcon = {
                    IconButton(onClick = onClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                }
            )
        }
    ) { innerPadding ->
        // 2. QUAN TRỌNG: Truyền innerPadding vào modifier của nội dung
        Box(modifier = Modifier.padding(innerPadding)) {
            ColumnMovie(
                items = items,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
