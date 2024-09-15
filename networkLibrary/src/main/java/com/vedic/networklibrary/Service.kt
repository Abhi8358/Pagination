package com.vedic.networklibrary

import java.net.URL

interface Service {
    suspend fun <T> get(url: URL, responseType: Class<T>): ApiResponse<T>
    suspend fun <T> post(url: URL, body: Any, responseType: Class<T>): ApiResponse<T>
}