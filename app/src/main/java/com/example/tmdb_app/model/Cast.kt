package com.example.tmdb_app.model

data class Cast(
    var adult: Boolean,
    var cast_id: Int,
    var character: String,
    var credit_id: String,
    var gender: Int,
    var id: Int,
    var known_for_department: String,
    var name: String,
    var order: Int,
    var original_name: String,
    var popularity: Double,
    var profile_path: String
)