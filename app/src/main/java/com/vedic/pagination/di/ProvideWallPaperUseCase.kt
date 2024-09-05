package com.vedic.pagination.di

import com.vedic.pagination.domain.WallPaperRepo
import com.vedic.pagination.domain.WallPaperUseCase
import com.vedic.pagination.domain.WallPaperUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ProvideWallPaperUseCase {

    @Provides
    fun getWallPaperUseCase(repo: WallPaperRepo): WallPaperUseCase = WallPaperUseCaseImpl(repo)
}