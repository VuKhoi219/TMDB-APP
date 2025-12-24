package com.example.tmdb_app.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.tmdb_app.model.Movie
import com.example.tmdb_app.ui.theme.Red20

@Composable
fun MovieItem(
    movie: Movie,
    modifier: Modifier = Modifier,
    onClick : () -> Unit,

){
    val rounded = String.format("%.1f", movie.vote_average)


    Box(modifier = modifier.clip(RoundedCornerShape(16.dp)).clickable(onClick = onClick)
    ) {
        // 1. Ảnh nền chiếm toàn bộ Box cha
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Nên thêm để ảnh đầy khung

        )

        // 2. Box màu đỏ (Badge) - Chỉ chiếm diện tích vừa đủ và nằm ở góc trái
        Box(
            modifier = Modifier
                .padding(8.dp) // Cách mép ảnh một chút
                .align(Alignment.TopStart) // ĐÂY LÀ CHÌA KHÓA: Đưa về góc trái trên
                .background(
                    color = Red20,
                    shape = RoundedCornerShape(4.dp) // Thường Badge chỉ nên bo nhẹ 4dp nhìn sẽ chuyên nghiệp hơn
                )
        ) {
            Text(
                text = rounded,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), // Padding cho text bên trong vùng đỏ
                color = Color.White,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}