package com.example.tmdb_app.service

import android.util.Log
import com.example.tmdb_app.BuildConfig
import com.example.tmdb_app.model.Credit
import com.example.tmdb_app.model.MovieDetail
import com.example.tmdb_app.model.MovieResponse
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.IOException

class ApiService {
    private val client = OkHttpClient()
    private val gson = Gson()
    private val apiKey = BuildConfig.TMDB_API_KEY
    private val baseUrl = "https://api.themoviedb.org/3"
    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    // Vì gọi API là bất đồng bộ, chúng ta cần một Callback để trả kết quả về sau
    suspend fun getPopularMovies(page: Int = 1, onSuccess: (MovieResponse?) -> Unit, onError: (String) -> Unit) {

        val url = "$baseUrl/movie/popular?language=en-US&page=$page"
        println("apiKey: $apiKey")

        val request = withContext(Dispatchers.IO) {
            Request.Builder()
                .url("${baseUrl}/movie/popular?language=en-US&page=1")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer $apiKey")
                .build()
            }


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
    suspend fun getTopMovies(page: Int = 1, onSuccess: (MovieResponse?) -> Unit, onError: (String) -> Unit) {

        val url = "$baseUrl/movie/top_rated?language=en-US&page=$page"
        println("apiKey: $apiKey")

        val request = withContext(Dispatchers.IO){ Request.Builder()
            .url("$url")
            .get()
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .build()}

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
    suspend  fun getNewMovies(page: Int = 1, onSuccess: (MovieResponse?) -> Unit, onError: (String) -> Unit) {

        val url = "$baseUrl/movie/top_rated?language=en-US&page=$page"
        println("apiKey: $apiKey")

        val request = withContext(Dispatchers.IO){ Request.Builder()
            .url("$url")
            .get()
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .build()}

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
    suspend fun getDetailMovie(
        movieId: Int,
        onSuccess: (MovieDetail?) -> Unit,
        onError: (String) -> Unit
    ) {
        val url = "$baseUrl/movie/$movieId?language=en-US"

        withContext(Dispatchers.IO) {
            try {
                Log.d("message111111", "url: $url")
                val request = Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer $apiKey")
                    .build()
                Log.d("API_DEBUG", "Full URL: ${request.url}")
                Log.d("API_DEBUG", "Auth Token: ${request.header("Authorization")}")                // Sử dụng execute() thay vì enqueue() để code chạy tuần tự và dễ đọc
                val response = client.newCall(request).execute()

                response.use { resp ->
                    val jsonString = resp.body?.string()
                    Log.d("message111111", "response: $jsonString")

                    if (resp.isSuccessful && jsonString != null) {
                        val movieDetail = gson.fromJson(jsonString, MovieDetail::class.java)

                        // Trả kết quả về Main Thread để cập nhật UI Compose
                        withContext(Dispatchers.Main) {
                            Log.d("message11111111", "getDetailMovie: $movieDetail")
                            onSuccess(movieDetail)
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            onError("Lỗi API: ${resp.code} ${resp.message}")
                        }
                    }
                }
            } catch (e: Exception) {
                // Bắt các lỗi như mất mạng, timeout...
                withContext(Dispatchers.Main) {
                    onError("Lỗi kết nối: ${e.message}")
                }
            }
        }
    }

    suspend fun getCredit(
        movieId: Int,
        onSuccess: (Credit?) -> Unit,
        onError: (String) -> Unit
    ) {
        // Lưu ý: TMDB dùng /movie/{id}/credits cho phim, bạn đang dùng /tv/
        val url = "$baseUrl/movie/$movieId/credits?language=en-US"

        withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer $apiKey")
                    .build()

                // Thực thi gọi API
                val response = client.newCall(request).execute()
                val jsonString = response.body?.string()

                if (response.isSuccessful && jsonString != null) {
                    val creditResponse = gson.fromJson(jsonString, Credit::class.java)

                    // Trả về kết quả trên Main Thread để update UI an toàn
                    withContext(Dispatchers.Main) {
                        onSuccess(creditResponse)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError("Lỗi API: ${response.code}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError("Lỗi kết nối: ${e.message}")
                }
            }
        }
    }

}

