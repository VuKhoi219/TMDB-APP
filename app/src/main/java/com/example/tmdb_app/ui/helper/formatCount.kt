package com.example.tmdb_app.ui.helper

class formatCount {
    fun formatCount(count: Int): String {
        return when {
            count >= 1_000_000_000 -> {
                val billions = count / 1_000_000_000f
                String.format("%.1fB", billions).replace(".0", "")
            }
            count >= 1_000_000 -> {
                val millions = count / 1_000_000f
                String.format("%.1fM", millions).replace(".0", "")
            }
            count >= 1_000 -> {
                val thousands = count / 1_000f
                String.format("%.1fK", thousands).replace(".0", "")
            }
            else -> count.toString()
        }
    }
}