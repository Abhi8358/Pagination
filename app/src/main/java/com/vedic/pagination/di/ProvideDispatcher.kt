package com.vedic.pagination.di

import com.vedic.pagination.core.DispatcherProvider
import com.vedic.pagination.core.DispatcherProviderImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ProvideDispatcher {

    @Provides
    fun getProvideDispatcher(): DispatcherProvider = DispatcherProviderImpl()
}