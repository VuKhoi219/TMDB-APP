package com.example.tmdb_app.service

import com.example.tmdb_app.model.GenreResponse
import com.example.tmdb_app.model.PopularMovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String
    ): PopularMovieResponse

    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("api_key") apiKey: String
    ): GenreResponse
}
