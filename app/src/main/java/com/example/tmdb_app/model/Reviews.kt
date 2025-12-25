package com.example.tmdb_app.model

data class Reviews(
    var id: Int,
    var page: Int,
    var results: List<Review>,
    var total_pages: Int,
    var total_results: Int
)