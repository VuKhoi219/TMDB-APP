package com.example.tmdb_app.ui.screens.detailMovie

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CastConnected
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.tmdb_app.R
import com.example.tmdb_app.model.Cast
import com.example.tmdb_app.model.MovieDetail
import com.example.tmdb_app.service.ApiService
import com.example.tmdb_app.ui.theme.Red20


@Composable
fun DetailMovie(
    movieId: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onBackClick: () -> Unit,
){
    var movieDetail by remember { mutableStateOf<MovieDetail?>(null) }
    val apiService = remember { ApiService() }
    var stringGenre by remember { mutableStateOf("") }
    var rounded by remember { mutableStateOf("0.0") }
    var ageText by remember { mutableStateOf("13+") }
    var isExpanded by remember { mutableStateOf(false) }
    var casts by remember { mutableStateOf(emptyList<Cast>()) }


    LaunchedEffect(Unit) {
        apiService.getDetailMovie(
            movieId = movieId,
            onSuccess = { response -> response?.let { movieDetail = it } },
            onError = {print("$it")}
        )
        stringGenre = movieDetail?.genres?.joinToString(", ") { it.name } ?: ""
        rounded = String.format("%.1f", movieDetail?.vote_average)
        if (movieDetail?.adult == true) {
            ageText = "18+"
        }
        apiService.getCredit(movieId = movieId,
            onSuccess = { response -> response?.let { casts = it.cast } },
            onError = {print("$it")}
        )
    }
    LazyColumn(
        modifier = modifier.fillMaxSize()

    ) {
        item {
            // Chiều cao toàn bộ Slider nên lớn (ví dụ 550dp hoặc fillMaxHeight)
            Box(modifier = Modifier.fillMaxSize()) {

                // 1. LỚP NỀN: Ảnh Poster phim chiếm toàn bộ diện tích
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w500${movieDetail?.poster_path}",
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // 2. LỚP PHỦ: Gradient đen từ dưới lên để chữ nổi bật
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                                startY = 500f // Bắt đầu tối dần từ giữa ảnh
                            )
                        )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                        }
                        IconButton(onClick = { /* Notify */ }) {
                            Icon(Icons.Default.CastConnected, null, tint = Color.White)
                        }

                }
            }
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically // Giúp text và icon thẳng hàng
                ) {
                    Text(
                        text = movieDetail?.title ?: "",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        maxLines = 1, // Bắt buộc phải là 1 dòng để hiệu ứng chạy hoạt động
                        modifier = Modifier
                            .weight(1f) // 1. Chiếm hết không gian trống còn lại
                            .padding(end = 8.dp) // Khoảng cách nhỏ với cụm icon
                            .basicMarquee(
                                iterations = Int.MAX_VALUE, // Chạy vô hạn lần
                                initialDelayMillis = 2000, // Đợi 2 giây sau khi quay lại đầu
                                velocity = 50.dp // Tốc độ chạy (50dp mỗi giây)
                            )
                    )
                    // Cụm Icon bên phải
                    Row(
                        modifier = Modifier
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(4.dp)
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { /* Save */ }) {
                            Icon(Icons.Filled.BookmarkBorder, null, tint = Color.Black)
                        }
                        IconButton(onClick = onClick) {
                            Icon(Icons.Filled.Send, null, tint = Color.Black)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp), // Cách đều các phần tử
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color.Yellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(rounded, style = MaterialTheme.typography.bodyMedium)
                    Icon(
                        Icons.Filled.NavigateNext,
                        contentDescription = null,
                        tint = Red20,
                        modifier = Modifier
                    )
                    Surface(
                        modifier = Modifier
                            .size(4.dp)
                            .background(color = Red20, shape = RoundedCornerShape(2.dp))
                    ){
                        Text(ageText, style = MaterialTheme.typography.bodyMedium, color = Red20)

                    }

                    Surface(
                        modifier = Modifier
                            .size(4.dp)
                            .background(color = Red20, shape = RoundedCornerShape(2.dp))
                    ){
                        Text(ageText, style = MaterialTheme.typography.bodyMedium, color = Red20)
                    }
                    Surface(
                        modifier = Modifier
                            .size(4.dp)
                            .background(color = Red20, shape = RoundedCornerShape(2.dp))
                    ){
                        Text(text = movieDetail?.production_countries?.firstOrNull()?.name ?: "United States", style = MaterialTheme.typography.bodyMedium, color = Red20)
                    }
                    Surface(
                        modifier = Modifier
                            .size(4.dp)
                            .background(color = Red20, shape = RoundedCornerShape(2.dp))
                    ){
                        Text(text = stringResource(id = R.string.subtitle), style = MaterialTheme.typography.bodyMedium, color = Red20)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),){
                    Button(
                        onClick = { /* Play */ },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        // THAY ĐỔI: Giảm bo góc để đồng bộ với nút Play
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        // THAY ĐỔI: Giảm bo góc để nút vuông vức hơn
                    ) {
                        Icon(Icons.Default.PlayArrow, null, tint = Color.White, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(stringResource(
                            id = R.string.play
                        ), color = Color.White, fontSize = MaterialTheme.typography.bodyMedium.fontSize)
                    }
                    // THAY ĐỔI: Chuyển từ OutlinedButton sang Button và tùy chỉnh màu nền
                    Button(
                        onClick = { /* My List */ },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        // THAY ĐỔI: Giảm bo góc để đồng bộ với nút Play
                        shape = RoundedCornerShape(50.dp),
                        // THAY ĐỔI: Đặt màu nền xám bán trong suốt, không có viền
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray.copy(alpha = 0.5f),
                            contentColor = Color.White
                        ),
                        border = null // Đảm bảo không có viền
                    ) {
                        Icon(Icons.Default.SaveAlt, null, tint = Color.White, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(4.dp))
                        Text( stringResource(
                            id = R.string.my_list
                        )  , color = Color.White, fontSize = MaterialTheme.typography.bodyMedium.fontSize)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(id = R.string.genre))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringGenre,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .basicMarquee(
                                iterations = Int.MAX_VALUE,
                                velocity = 40.dp // Chạy chậm hơn tên phim một chút để dễ đọc
                            )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = movieDetail?.overview ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        // 2. Nếu isExpanded là true thì hiện hết, ngược lại giới hạn 3 dòng
                        maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .animateContentSize() // Hiệu ứng mượt mà khi mở rộng
                            .clickable { isExpanded = !isExpanded } // Nhấn vào để chuyển đổi
                    )
                    // 3. Hiển thị chữ "more" hoặc "less" để gợi ý người dùng
                    if (!isExpanded) {
                        Text(
                            text = stringResource(id = R.string.more),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable { isExpanded = true }
                        )
                    }
                }
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(casts) { item ->
                        AsyncImage(
                            model = "https://imag