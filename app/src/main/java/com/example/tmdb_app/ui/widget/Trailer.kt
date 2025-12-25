package com.example.tmdb_app.ui.widget

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.airbnb.lottie.Lottie.initialize
import com.example.tmdb_app.model.Trailer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.example.tmdb_app.R
import com.example.tmdb_app.ui.helper.TimeManager
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions


@Composable

fun TrailerItem(
    trailer: Trailer,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isDate: Boolean = false, // Giữ nguyên ngày theo yêu cầu của bạn
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    val timeManager = TimeManager()
    var date  = timeManager.formatFullDate(trailer.published_at)

    Log.d("message111111", "TrailerItem: ${trailer.key}")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- PHẦN VIDEO ---
        AndroidView(
            modifier = Modifier
                .weight(0.4f) // Sử dụng weight để linh hoạt hơn
                .aspectRatio(16/9f) // Cố định tỷ lệ khung hình video
                .clip(RoundedCornerShape(12.dp)),
            factory = { ctx ->


                YouTubePlayerView(ctx).apply {
                    enableAutomaticInitialization = false // Tắt tự động khởi tạo để cấu hình tay
                    initialize(object : AbstractYouTubePlayerListener() {
                        override fun onReady(youTubePlayer: YouTubePlayer) {
                            youTubePlayer.loadVideo(trailer.key, 0f)
                        }
                    })
                }
            }
        )

        Spacer(modifier = Modifier.width(12.dp))

        // --- PHẦN THÔNG TIN ---
        Column(
            modifier = Modifier.weight(0.6f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Tên trailer với hiệu ứng chữ chạy (Marquee)
            Text(
                text = trailer.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                maxLines = 1,
                modifier = Modifier.basicMarquee() // Tên dài sẽ tự động chạy
            )

            // Hiển thị ngày (hoặc thời lượng nếu bạn có dữ liệu)
            if (isDate) {
                Text(
                    text = date, // Cắt bớt chỉ lấy YYYY-MM-DD cho gọn
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            // Nút "Update" (Tag màu hồng)
            Surface(
                color = Color(0xFFFFE4E6), // Màu hồng nhạt giống ảnh
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.update),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    color = Color(0xFFE11D48), // Màu đỏ đậm của chữ
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}