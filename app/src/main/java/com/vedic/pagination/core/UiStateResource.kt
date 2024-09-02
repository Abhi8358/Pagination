package com.vedic.pagination.core

import androidx.annotation.StringRes

sealed interface UiStateResource {
    data class Loading(val loadingType: LoadingType) : UiStateResource
    data class Error<T>(val stringType: T) : UiStateResource
    data class Success<T>(val result: T) : UiStateResource
}

enum class LoadingType {
    INITIAL,
    PAGING
}

sealed interface StringType {
    data class StringResource(@StringRes val stringId: Int)
    data class StringText(val apiErrorString: String)
}