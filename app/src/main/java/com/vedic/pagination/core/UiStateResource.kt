package com.vedic.pagination.core

import androidx.annotation.StringRes

sealed interface UiStateResource<out T> {
    data class Loading(val loadingType: LoadingType) : UiStateResource<Nothing>
    data class Error(val stringType: ErrorStringType) : UiStateResource<Nothing>
    data class Success<T>(val result: T) : UiStateResource<T>
}

enum class LoadingType {
    INITIAL,
    PAGING
}

sealed interface ErrorStringType {
    data class StringResource(@StringRes val stringId: Int): ErrorStringType
    data class StringText(val apiErrorString: String): ErrorStringType
}