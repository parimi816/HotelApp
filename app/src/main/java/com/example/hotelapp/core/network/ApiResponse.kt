package com.example.hotelapp.core.network

sealed class ApiResponse<out T> {
    data class Success<out T>(val data: T) : ApiResponse<T>()
    data class Error(val code: Int, val message: String?) : ApiResponse<Nothing>()
    object NetworkError : ApiResponse<Nothing>()
}
