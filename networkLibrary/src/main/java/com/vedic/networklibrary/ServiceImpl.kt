package com.vedic.networklibrary


import okhttp3.*
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.net.URL

class ServiceImpl : Service {
    private val client = OkHttpClient()
    private val gson = Gson()

    override suspend fun <T> get(url: URL, responseType: Class<T>): ApiResponse<T> {
        val request = Request.Builder().url(url).get().build()

        return try {
            val response = client.newCall(request).execute()
            handleResponse(response, responseType)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    override suspend fun <T> post(url: URL, body: Any, responseType: Class<T>): ApiResponse<T> {
        val jsonBody = gson.toJson(body).toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder().url(url).post(jsonBody).build()

        return try {
            val response = client.newCall(request).execute()
            handleResponse(response, responseType)
        } catch (e: Exception) {
            ApiResponse.Error(e)
        }
    }

    private fun <T> handleResponse(response: Response, responseType: Class<T>): ApiResponse<T> {
        return if (response.isSuccessful) {
            val body = response.body?.string()
            val data = gson.fromJson(body, responseType)
            ApiResponse.Success(data)
        } else {
            ApiResponse.Error(IOException("Network error with code: ${response.code}"))
        }
    }
}
