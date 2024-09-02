package com.vedic.pagination.di

import com.vedic.pagination.core.DispatcherProvider
import com.vedic.pagination.core.NetworkUtils
import com.vedic.pagination.data.PexelService
import com.vedic.pagination.data.WallPaperRepoImpl
import com.vedic.pagination.domain.WallPaperRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ProvideWallPaperRepository {

    @Provides
    fun getWallPaperRepository(pexelService: PexelService, dispatcher: DispatcherProvider, networkUtils: NetworkUtils): WallPaperRepo {
        return WallPaperRepoImpl(pexelService, dispatcher, networkUtils)
    }
}