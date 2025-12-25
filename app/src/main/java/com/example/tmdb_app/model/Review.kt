package com.example.tmdb_app.model

data class Review(
    var author : String,
    var author_details : AuthorDetails,
    var content : String,
    var created_at : String,
    var id : String,
    var updated_at : String,
    var url : String
)