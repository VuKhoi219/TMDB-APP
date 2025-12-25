package com.example.tmdb_app.model

data class Trailer(
    var iso_639_1: String,
    var iso_3166_1: String,
    var name: String,
    var key: String,
    var site: String,
    var size: Int,
    var type: String,
    var official: Boolean,
    var published_at: String,
    var id: String
)
