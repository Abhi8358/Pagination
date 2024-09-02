package com.vedic.pagination.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlin.coroutines.CoroutineContext

interface DispatcherProvider {
    val io: CoroutineContext
    val main: CoroutineContext
    val default: CoroutineContext
}