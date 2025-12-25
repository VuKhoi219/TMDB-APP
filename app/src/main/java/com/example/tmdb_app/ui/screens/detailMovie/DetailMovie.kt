package com.example.tmdb_app.ui.screens.detailMovie

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CastConnected
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.tmdb_app.R
import com.example.tmdb_app.model.Cast
import com.example.tmdb_app.model.Movie
import com.example.tmdb_app.model.MovieDetail
import com.example.tmdb_app.model.Review
import com.example.tmdb_app.model.Trailer
import com.example.tmdb_app.service.ApiService
import com.example.tmdb_app.ui.helper.formatCount
import com.example.tmdb_app.ui.theme.Red20
import com.example.tmdb_app.ui.widget.ColumnMovie
import com.example.tmdb_app.ui.widget.Comment
import com.example.tmdb_app.ui.widget.TrailerItem
import kotlinx.coroutines.launch


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
    var trailers by remember { mutableStateOf(emptyList<Trailer?>()) }
    var topMovies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var comments by remember { mutableStateOf<List<Review>>(emptyList()) }
    val context = LocalContext.current // Lấy context ở đầu hàm Composable

    val pages = listOf(stringResource(id = R.string.trailers),stringResource(id = R.string.more_like_this) ,stringResource(id = R.string.comments))
    val pagerState = rememberPagerState(initialPage = 0) {
        pages.size
    }
    val scope = rememberCoroutineScope()
    val formatCount = formatCount()
    fun shareMovie(context: Context, title: String, movieUrl: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            // Nội dung tin nhắn chia sẻ
            putExtra(Intent.EXTRA_TEXT, "Xem phim này hay lắm: $title\nLink: $movieUrl")
            type = "text/plain" // Định dạng là văn bản thuần túy
        }

        val shareIntent = Intent.createChooser(sendIntent, "Chia sẻ phim qua...")
        context.startActivity(shareIntent)
    }
    LaunchedEffect(Unit) {
        Log.d("message111111", "DetailMovie: $movieId")
        apiService.getDetailMovie(
            movieId = movieId,
            onSuccess = { response -> response?.let { movieDetail = it }
                Log.d("message111111", "DetailMovie title1: ${movieDetail?.title}")},
            onError = {print("$it")}
        )
        Log.d("message111111", "DetailMovie title2: ${movieDetail?.title}")
        stringGenre = movieDetail?.genres?.joinToString(", ") { it.name } ?: ""
        rounded = String.format("%.1f", movieDetail?.vote_average)
        if (movieDetail?.adult == true) {
            ageText = "18+"
        }
        Log.d("message111111", "DetailMovie title: ${movieDetail?.title}")
        apiService.getCredit(movieId = movieId,
            onSuccess = { response -> response?.let { casts = it.cast } },
            onError = {print("$it")}
        )
        apiService.getTrailers(movieId = movieId,
            onSuccess = { response -> response?.let { trailers = it.results } },
            onError = {print("$it")}
        )
        apiService.getTopMovies(
            onSuccess = { response -> response?.let { topMovies = it.results } },
            onError = {
                // Xử lý lỗi
                println("Lỗi getTopMovies: $it")
            })
        apiService.getReviews(
            movieId = movieId,
            onSuccess = { response -> response?.let { comments = it.results } },
            onError = {
                // Xử lý lỗi
                println("Lỗi getReviews: $it")
            }
        )
    }
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val bannerHeight = screenHeight * 0.5f
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        item {
            // Chiều cao toàn bộ Slider nên lớn (ví dụ 550dp hoặc fillMaxHeight)
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(bannerHeight)) {

                // 1. LỚP NỀN: Ảnh Poster phim chiếm toàn bộ diện tích
                Log.d("message", "DetailMovie: https://image.tmdb.org/t/p/w300${movieDetail?.poster_path}")
                AsyncImage(
                    model = "https://image.tmdb.org/t/p/w300${movieDetail?.poster_path}",
                    contentDescription = null,
                    modifier = modifier.fillMaxSize(),
                    contentScale = ContentScale.FillWidth
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
                        IconButton(onClick = { onBackClick() }) {
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
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp), // Thêm padding tổng thể
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = movieDetail?.title ?: "Avatar: The Way of Water",
                        style = MaterialTheme.typography.headlineSmall.copy( // Dùng font to và đậm hơn
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.5).sp // Chữ hơi khít lại một chút cho giống mẫu
                        ),
                        maxLines = 1,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 16.dp)
                            .basicMarquee(
                                iterations = Int.MAX_VALUE,
                                velocity = 50.dp
                            )
                    )

                    // Cụm Icon bên phải (Không dùng background để giống hình mẫu)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp) // Khoảng cách giữa 2 icon
                    ) {
                        IconButton(
                            onClick = { /* Save */ },
                            modifier = Modifier.size(32.dp) // Thu nhỏ vùng click để icon sát nhau hơn
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.BookmarkBorder, // Dùng Outlined cho thanh mảnh
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                shareMovie(
                                    context = context,
                                    title = movieDetail?.title ?: "Phim hay",
                                    movieUrl = "https://www.themoviedb.org/movie/${movieDetail?.id}"
                                )
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Send, // Icon máy bay giấy dạng viền
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.rotate(-30f), // Xoay nhẹ icon để giống hình mẫu hơn
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), // Thêm padding tổng thể

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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), // Thêm padding cho đẹp bố cục
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // --- NÚT PLAY (Nền đỏ đặc) ---
                    Button(
                        onClick = { /* Play */ },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = CircleShape, // Bo tròn hoàn toàn giống hình
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE50914), // Màu đỏ chuẩn Netflix/YouTube
                            contentColor = Color.White
                        ),
                        elevation = null // Thường các nút này phẳng (flat)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayCircleFilled, // Icon Play có vòng tròn sẽ giống hơn
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = stringResource(id = R.string.play),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    // --- NÚT DOWNLOAD (Nền trắng, viền đỏ) ---
                    OutlinedButton(
                        onClick = { /* Download */ },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = CircleShape, // Bo tròn hoàn toàn
                        border = BorderStroke(2.dp, Color(0xFFE50914)), // Viền đỏ đậm
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color(0xFFE50914) // Chữ màu đỏ
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.FileDownload, // Icon tải xuống
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Color(0xFFE50914)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = stringResource(id = R.string.download), // Thay bằng R.string.download
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), // Thêm padding tổng thể

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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    pages.forEachIndexed { index, title ->
                        // Kiểm tra xem trang này có đang được chọn hay không
                        val isSelected = pagerState.currentPage == index

                        // Màu sắc thay đổi dựa trên trạng thái chọn
                        val textColor = if (isSelected) Color.Red else Color.Gray

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null // Tắt hiệu ứng ripple mặc định nếu muốn sạch hơn
                                ) {
                                    scope.launch { pagerState.animateScrollToPage(index) }
                                }
                                .padding(top = 12.dp) // Chỉ padding top để gạch dưới sát đáy
                        ) {
                            Text(
                                text = title,
                                color = textColor,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )

                            // Thanh gạch dưới (Indicator)
                            Box(
                                modifier = Modifier
                                    .height(2.dp)
                                    .width(40.dp) // Độ rộng của gạch dưới
                                    .background(
                                        color = if (isSelected) Color.Red else Color.Transparent,
                                        shape = RoundedCornerShape(2.dp)
                                    )
                            )
                        }
                    }
                }
                // Nội dung dưới menu, vuốt ngang hoặc đổi bằng menu
                HorizontalPager(
                    state = pagerState, // pageCount đã được khai báo ở bước rememberPagerState
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) { page ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        when (page) {
                            0 -> {
                                // Sử dụng LazyRow nếu muốn vuốt ngang, LazyColumn nếu muốn vuốt dọc
                                LazyColumn(modifier = Modifier.fillMaxSize()) {
                                    items(trailers) { item -> // Duyệt qua từng trailer trong list
                                        if (item != null) {
                                            TrailerItem(
                                                trailer = item,
                                                modifier = Modifier.padding(8.dp),
                                                onClick = { /* Handle Trailer Click */ },
                                            )
                                        }
                                    }
                                }
                            }
                            1 -> {
                                ColumnMovie(modifier = Modifier.fillMaxSize(), items = topMovies)
                            }
                            2 -> {
                                LazyColumn(modifier = Modifier.fillMaxSize()) {
                                    item {
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,

                                        ) {
                                            Text(
                                                text = "${formatCount.formatCount(comments.size)} Comments",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = Color.Gray
                                            )
                                            Text(text  = stringResource( R.string.see_all), style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                                        }
                                    }

                                    items(comments) { item -> // Duyệt qua từng trailer trong list
                                        if (item != null) {
                                            Comment(
                                                comment = item,
                                            )
                                        }
                                    }
                                }
                            }
                    }
                }
            }
        }
    }
}}

