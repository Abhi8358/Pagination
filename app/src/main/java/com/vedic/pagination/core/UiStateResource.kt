package com.vedic.pagination.core

import androidx.annotation.StringRes

sealed interface UiStateResource {
    data class Loading(val loadingType: LoadingType) : UiStateResource
    data class Error(val stringType: ErrorStringType, val isFirstPage: Boolean = false) : UiStateResource
    data object Success : UiStateResource
}

enum class LoadingType {
    INITIAL,
    PAGING
}

sealed interface ErrorStringType {
    data class StringResource(@StringRes val stringId: Int): ErrorStringType
    data class StringText(val apiErrorString: String): ErrorStringType
}