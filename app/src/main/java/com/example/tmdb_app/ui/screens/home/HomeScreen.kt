package com.example.tmdb_app.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FindReplace

import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.tmdb_app.R
import com.example.tmdb_app.model.PopularMovie
import com.example.tmdb_app.service.ApiService
import com.example.tmdb_app.ui.theme.Red20
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {
    var movies by remember { mutableStateOf<List<PopularMovie>>(emptyList()) }
    val apiService = remember { ApiService() }

    // 1. Chỉ khởi tạo PagerState khi cần thiết
    val pagerState = rememberPagerState(pageCount = { movies.size })
    val scope = rememberCoroutineScope()

    // 2. Logic Auto-play (Chỉ chạy khi có phim)
    if (movies.isNotEmpty()) {
        LaunchedEffect(Unit) {
            while (true) {
                // Thêm một khoảng delay nhỏ ở đầu để chờ Pager ổn định
                delay(3000)
                // Chỉ tự động cuộn khi người dùng không tương tác
                if (!pagerState.isScrollInProgress) {
                    val nextPage = (pagerState.currentPage + 1) % movies.size
                    pagerState.animateScrollToPage(nextPage)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        apiService.getPopularMovies(
            onSuccess = { response -> response?.let { movies = it.results } },
            onError = {
                // Xử lý lỗi
                println("Lỗi getPopularMovies: $it")
            }
        )
    }

    // 3. Sử dụng LazyColumn để chứa toàn bộ màn hình
    LazyColumn(modifier = Modifier.fillMaxSize()) {

        // MỤC 1: SLIDER TỰ ĐỘNG CHẠY (Đây là phần Header)
        item {
            if (movies.isNotEmpty()) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)) {
                    HorizontalPager(state = pagerState) { page ->
                        // Truyền phim tại trang hiện tại vào MovieItem
                        MovieItem(
                            movie = movies[page],
                            onNext = {
                                scope.launch {
                                    pagerState.animateScrollToPage((pagerState.currentPage + 1) % movies.size)
                                }
                            },
                            onPrev = {
                                scope.launch {
                                    val prevPage = if (pagerState.currentPage > 0) pagerState.currentPage - 1 else movies.size - 1
                                    pagerState.animateScrollToPage(prevPage)
                                }
                            }
                        )
                    }
                }
            }
        }

        // MỤC 2: CÁC DANH SÁCH PHIM KHÁC (Nếu bạn muốn thêm bên dưới)
        item {
            Text("Trending Now", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
        }
        // Bạn có thể dùng items(movies) ở đây để tạo danh sách dọc bình thường bên dưới Slider
    }
}

@Composable
fun MovieItem(
    movie: PopularMovie,
    onNext: () -> Unit,
    onPrev: () -> Unit
) {
    // Chiều cao toàn bộ Slider nên lớn (ví dụ 550dp hoặc fillMaxHeight)
    Box(modifier = Modifier.fillMaxSize()) {

        // 1. LỚP NỀN: Ảnh Poster phim chiếm toàn bộ diện tích
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${movie.poster_path}",
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

        // 3. LỚP UI TRÊN CÙNG: Logo, Search, Notifications (Cố định ở Top)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding() // Tránh bị đè bởi thanh trạng thái hệ thống
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier.height(35.dp) // Kích thước logo vừa phải
            )
            Row {
                IconButton(onClick = { /* Search */ }) {
                    Icon(Icons.Default.Search, null, tint = Color.White)
                }
                IconButton(onClick = { /* Notify */ }) {
                    Icon(Icons.Default.NotificationsNone, null, tint = Color.White)
                }
            }
        }

        // 4. THAY ĐỔI: Vô hiệu hóa lớp điều hướng Next/Prev để ẩn chúng đi
        // Row(
        //     modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp),
        //     horizontalArrangement = Arrangement.SpaceBetween,
        //     verticalAlignment = Alignment.CenterVertically
        // ) {
        //     IconButton(onClick = onPrev) {
        //         Icon(Icons.Default.NavigateBefore, null, tint = Color.White.copy(alpha = 0.5f), modifier = Modifier.size(40.dp))
        //     }
        //     IconButton(onClick = onNext) {
        //         Icon(Icons.Default.NavigateNext, null, tint = Color.White.copy(alpha = 0.5f), modifier = Modifier.size(40.dp))
        //     }
        // }

        // 5. LỚP THÔNG TIN PHIM & NÚT BẤM (Ghim ở Bottom)
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 32.dp, start = 20.dp, end = 20.dp),
        ) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            // THAY ĐỔI: Thay overview bằng text thể loại phim tĩnh như trong ảnh
            Text(
                text = "Action, Superhero, Science Fiction",
                color = Color.LightGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Hàng nút Play và My List
            Row(
                // THAY ĐỔI: Thu hẹp Row lại một chút và tăng khoảng cách giữa các nút
                modifier = Modifier.fillMaxWidth(0.6f),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Nút Play Đỏ
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
                    Text("Play", color = Color.White, fontSize = MaterialTheme.typography.bodyMedium.fontSize)
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
                    Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("My List", color = Color.White, fontSize = MaterialTheme.typography.bodyMedium.fontSize)
                }
            }
        }
    }
}
