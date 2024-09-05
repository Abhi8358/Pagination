package com.vedic.pagination.di

import com.vedic.pagination.data.PexelService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ProvidePexelService {


    @Provides
    fun providePexelService(retrofit: Retrofit): PexelService {
        return retrofit.create(PexelService::class.java)
    }
}