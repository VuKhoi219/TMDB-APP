package com.example.tmdb_app.model

data class PopularMovieResponse(
    val page: Int,
    val results: List<PopularMovie>,
    val total_pages: Int,
    val total_results: Int
)
