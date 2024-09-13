package com.vedic.networklibrary

sealed class ApiResponse<out T> {
    data class Success<T>(val response: T) : ApiResponse<T>()
    data class Error(val exception: Exception) : ApiResponse<Nothing>()
}