package com.example.tmdb_app.ui.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tmdb_app.model.Movie

@Composable
fun ColumnMovie(items: List<Movie>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(), // Chiếm hết màn hình
        contentPadding = PaddingValues(16.dp), // Khoảng cách từ mép màn hình vào trong danh sách
        verticalArrangement = Arrangement.spacedBy(16.dp), // Khoảng cách giữa các hàng
        horizontalArrangement = Arrangement.spacedBy(16.dp) // Khoảng cách giữa 2 cột
                    ){
        items(items.size) { index ->
            MovieItem(items[index], onClick = {})
        }
    }
}

