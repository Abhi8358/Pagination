package com.vedic.pagination.di

import com.vedic.pagination.data.PexelService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object ProvidePexelService {


    @Provides
    fun providePexelService(retrofit: Retrofit): PexelService {
        return retrofit.create(PexelService::class.java)
    }
}