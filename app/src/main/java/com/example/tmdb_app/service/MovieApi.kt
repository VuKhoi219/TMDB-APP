package com.example.tmdb_app.service

import com.example.tmdb_app.BuildConfig
import com.example.tmdb_app.model.MovieResponse
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class ApiService {
    private val client = OkHttpClient()
    private val gson = Gson()
    private val apiKey = BuildConfig.TMDB_API_KEY
    private val baseUrl = "https://api.themoviedb.org/3"

    // Vì gọi API là bất đồng bộ, chúng ta cần một Callback để trả kết quả về sau
    fun getPopularMovies(page: Int = 1, onSuccess: (MovieResponse?) -> Unit, onError: (String) -> Unit) {

        val url = "$baseUrl/movie/popular?language=en-US&page=$page"
        println("apiKey: $apiKey")

        val request = Request.Builder()
            .url("${baseUrl}/movie/popular?language=en-US&page=1")
            .get()
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError(e.message ?: "Unknown error")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        onError("Lỗi: ${response.code} ${response.message}")
                        return
                    }

                    val jsonString = response.body?.string()
                    if (jsonString != null) {
                        val movieResponse = gson.fromJson(jsonString, MovieResponse::class.java)
                        onSuccess(movieResponse)
                    } else {
                        onError("Không có dữ liệu trả về")
                    }
                }
            }
        })
    }
    fun getTopMovies(page: Int = 1, onSuccess: (MovieResponse?) -> Unit, onError: (String) -> Unit) {

        val url = "$baseUrl/movie/top_rated?language=en-US&page=$page"
        println("apiKey: $apiKey")

        val request = Request.Builder()
            .url("$url")
            .get()
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError(e.message ?: "Unknown error")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        onError("Lỗi: ${response.code} ${response.message}")
                        return
                    }

                    val jsonString = response.body?.string()
                    if (jsonString != null) {
                        val movieResponse = gson.fromJson(jsonString, MovieResponse::class.java)
                        onSuccess(movieResponse)
                    } else {
                        onError("Không có dữ liệu trả về")
                    }
                }
            }
        })
    }
    fun getNewMovies(page: Int = 1, onSuccess: (MovieResponse?) -> Unit, onError: (String) -> Unit) {

        val url = "$baseUrl/movie/top_rated?language=en-US&page=$page"
        println("apiKey: $apiKey")

        val request = Request.Builder()
            .url("$url")
            .get()
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError(e.message ?: "Unknown error")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        onError("Lỗi: ${response.code} ${response.message}")
                        return
                    }

                    val jsonString = response.body?.string()
                    if (jsonString != null) {
                        val movieResponse = gson.fromJson(jsonString, MovieResponse::class.java)
                        onSuccess(movieResponse)
                    } else {
                        onError("Không có dữ liệu trả về")
                    }
                }
            }
        })
    }
}
