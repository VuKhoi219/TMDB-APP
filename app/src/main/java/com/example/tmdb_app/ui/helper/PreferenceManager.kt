package com.example.tmdb_app.ui.helper

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
    // Khởi tạo SharedPreferences với tên file là "UserPrefs"
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    // Hàm lưu Token
    fun saveToken(token: String) {
        sharedPreferences.edit().putString("auth_token", token).apply()
    }

    // Hàm lấy Token
    fun getToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }

    fun saveEmailPassword(email: String, password: String) {
        sharedPreferences.edit().putString("email", email).apply()
        sharedPreferences.edit().putString("password", password).apply()
    }



    // Hàm xóa dữ liệu khi Logout
    fun clearData() {
        sharedPreferences.edit().clear().apply()
    }
}