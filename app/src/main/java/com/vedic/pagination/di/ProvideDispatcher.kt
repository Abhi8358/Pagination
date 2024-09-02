package com.vedic.pagination.di

import com.vedic.pagination.core.DispatcherProvider
import com.vedic.pagination.core.DispatcherProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ProvideDispatcher {

    @Binds
    fun getProvideDispatcher(dispatcherProviderImpl: DispatcherProviderImpl): DispatcherProvider
}