package com.example.tmdb_app.ui.helper

import java.time.Duration
import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class TimeManager {
    fun formatFullDate(isoString: String): String {
        return try {
            // Parse chuỗi ISO 8601
            val parsedDate = ZonedDateTime.parse(isoString)
            // Định dạng theo ý muốn (Ví dụ: ngày/tháng/năm)
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            parsedDate.format(formatter)
        } catch (e: Exception) {
            isoString // Trả về chuỗi gốc nếu lỗi
        }
    }
    fun getTimeAgo(isoString: String): String {
        return try {
            val past = Instant.parse(isoString)
            val now = Instant.now()
            val duration = Duration.between(past, now)

            val seconds = duration.seconds
            when {
                seconds < 60 -> "Vừa xong"
                seconds < 3600 -> "${seconds / 60} phút trước"
                seconds < 86400 -> "${seconds / 3600} giờ trước"
                seconds < 2592000 -> "${seconds / 86400} ngày trước"
                seconds < 31104000 -> "${seconds / 2592000} tháng trước"
                else -> "${seconds / 31104000} năm trước"
            }
        } catch (e: Exception) {
            ""
        }
    }
}