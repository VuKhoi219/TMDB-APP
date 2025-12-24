package com.example.tmdb_app.ui.screens.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tmdb_app.R
import com.example.tmdb_app.model.Movie
import com.example.tmdb_app.service.ApiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.tmdb_app.model.MovieDetail
import com.example.tmdb_app.navigation.Screen
import com.example.tmdb_app.ui.theme.Red20
import com.example.tmdb_app.ui.widget.MovieItem

@Composable
fun HomeScreen(navController: NavController) {
    var popular by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var topMovies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var newReleases by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var notification by remember { mutableStateOf<List<Movie>>(emptyList()) }

    val apiService = remember { ApiService() }

    // 1. Chỉ khởi tạo PagerState khi cần thiết
    val pagerState = rememberPagerState(pageCount = { popular.size })
    val scope = rememberCoroutineScope()

    // 2. Logic Auto-play (Chỉ chạy khi có phim)
    if (popular.isNotEmpty()) {
        LaunchedEffect(Unit) {
            while (true) {
                // Thêm một khoảng delay nhỏ ở đầu để chờ Pager ổn định
                delay(3000)
                // Chỉ tự động cuộn khi người dùng không tương tác
                if (!pagerState.isScrollInProgress) {
                    val nextPage = (pagerState.currentPage + 1) % popular.size
                    pagerState.animateScrollToPage(nextPage)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        apiService.getPopularMovies(
            onSuccess = { response -> response?.let { popular = it.results } },
            onError = {
                // Xử lý lỗi
                println("Lỗi getPopularMovies: $it")
            }
        )
        apiService.getTopMovies(
            onSuccess = { response -> response?.let { topMovies = it.results } },
            onError = {
                // Xử lý lỗi
                println("Lỗi getTopMovies: $it")
            }
        )
        // giả định newReleases và notification
        apiService.getPopularMovies(
            onSuccess = { response -> response?.let { newReleases = it.results } },
            onError = {
                // Xử lý lỗi
                println("Lỗi getPopularMovies: $it")
            }
        )
        apiService.getTopMovies(
            onSuccess = { response -> response?.let { notification = it.results } },
            onError = {
                // Xử lý lỗi
                println("Lỗi getTopMovies: $it")
            }
        )
    }

    val topMoviesTitle = stringResource(id = R.string.top_10_movies_this_week)
    val newReleasesTitle = stringResource(id = R.string.new_releases)
    val notificationsTitle = stringResource(id = R.string.notifications)

    // 3. Sử dụng LazyColumn để chứa toàn bộ màn hình
    LazyColumn(modifier = Modifier.fillMaxSize()) {

        // MỤC 1: SLIDER TỰ ĐỘNG CHẠY (Đây là phần Header)
        item {
            if (popular.isNotEmpty()) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)) {
                    HorizontalPager(state = pagerState) { page ->
                        // Truyền phim tại trang hiện tại vào MovieItem
                        MoviePopularItem(
                            movie = popular[page],
                            onNext = {
                                scope.launch {
                                    pagerState.animateScrollToPage((pagerState.currentPage + 1) % popular.size)
                                }
                            },
                            onPrev = {
                                scope.launch {
                                    val prevPage = if (pagerState.currentPage > 0) pagerState.currentPage - 1 else popular.size - 1
                                    pagerState.animateScrollToPage(prevPage)
                                }
                            }
                        )
                    }
                }
            }
        }

        // MỤC 2: CÁC DANH SÁCH PHIM trong tuần// 2. Các Section phim
            movieSection(
                title = topMoviesTitle,
                movieList = topMovies,
                onSeeAllClick = { navController.navigate(Screen.SeeAll.createRoute(topMoviesTitle, 1)) },
                navController = navController
            )

            movieSection(
                title = newReleasesTitle,
                movieList = newReleases,
                onSeeAllClick = { navController.navigate(Screen.SeeAll.createRoute(newReleasesTitle, 2)) },
                navController = navController
            )

            movieSection(
                title = notificationsTitle,
                movieList = notification,
                onSeeAllClick = { navController.navigate(Screen.SeeAll.createRoute(notificationsTitle, 3))},
                navController = navController

            )
    }
}
fun LazyListScope.movieSection(
    title: String,
    movieList: List<Movie>,
    onSeeAllClick: () -> Unit,
    navController: NavController
) {

    item {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = title,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium
            )
            VerticalDivider(
                modifier = Modifier.padding(horizontal = 8.dp),
                thickness = 1.dp,
            )
            Text(
                text = stringResource(id = R.string.see_all),
                color = Red20,
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                modifier = Modifier
                    .padding(16.dp) // Giữ lại padding để vùng bấm rộng hơn (dễ bấm trúng hơn)
                    .clickable { onSeeAllClick() } // Thêm sự kiện click tại đây
            )

        }

    }

    item {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(movieList) { movie ->
                MovieItem(
                    movie = movie, 
                    modifier = Modifier.size(width = 150.dp, height = 225.dp),
                    onClick = {
                    Log.d("error", "movieSection: ${movie.id}")
                    navController.navigate(Screen.DetailMovie.createRoute(movie.id))
                })
            }
        }
    }
    item {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
//            HorizontalPager(
//                pageCount = 3,
//                state = pagerState
//            ) { page ->
//                Text(text = "Page $page")
//            }
        }
    }
}
