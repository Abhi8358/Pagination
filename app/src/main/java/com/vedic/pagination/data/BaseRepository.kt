package com.vedic.pagination.data

import android.util.Log
import com.vedic.pagination.R
import com.vedic.pagination.core.DispatcherProvider
import com.vedic.pagination.core.ErrorStringType
import com.vedic.pagination.core.LoadingType
import com.vedic.pagination.core.NetworkUtils
import com.vedic.pagination.data.models.NetworkException
import com.vedic.pagination.data.models.OtherException
import com.vedic.pagination.data.models.ServerException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retryWhen
import retrofit2.Response

abstract class BaseRepository(
    private val networkUtils: NetworkUtils,
    private val dispatcher: DispatcherProvider
) {
    suspend fun <T> safeApiCall(
        pageNumber: Int,
        maxAttempt: Int = 3,
        getApiResponse: suspend () -> Response<T>,
        start: (LoadingType) -> Unit,
        onError: (ErrorStringType) -> Unit
    ): Flow<T> {
        return flow {
            if (!networkUtils.isNetworkConnected()) {
                throw NetworkException()
                return@flow
            }
            val response = getApiResponse.invoke()
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                emit(responseBody)
            } else if (response.code() in 500..507) {
                throw ServerException()
            } else {
                throw OtherException()
            }
        }.onStart {
            start(if (pageNumber == 1) LoadingType.INITIAL else LoadingType.PAGING)
        }.retryWhen { cause, attempt ->
            cause is ServerException && attempt < maxAttempt
        }.catch { cause ->
            Log.d("Abhishek", "cause :- $cause")
            when(cause) {
                is NetworkException -> {
                    onError(ErrorStringType.StringResource(R.string.no_internet))
                }
                is ServerException -> {
                    onError(ErrorStringType.StringResource(R.string.server_error_try_after_some_time))
                }
                else -> {
                    onError(ErrorStringType.StringResource(R.string.something_went_wrong))
                }
            }

        }.flowOn(dispatcher.io)
    }
}