package com.vedic.pagination.data

import com.vedic.pagination.core.DispatcherProvider
import com.vedic.pagination.core.NetworkUtils
import com.vedic.pagination.core.UiStateResource
import com.vedic.pagination.domain.WallPaperRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WallPaperRepoImpl @Inject constructor(
    private val pexelService: PexelService,
    dispatcherProvider: DispatcherProvider,
    networkUtils: NetworkUtils
) : WallPaperRepo, BaseRepository(networkUtils, dispatcherProvider) {

    override suspend fun getWallPapers(pageNumber: Int, itemCount: Int): Flow<UiStateResource> {
        return safeApiCall(pageNumber) {
            pexelService.getWallPapers(pageNumber, itemCount)
        }
    }

    override suspend fun searchWallPapers(
        pageNumber: Int,
        itemCount: Int,
        itemName: String
    ): Flow<UiStateResource> {
        TODO("Not yet implemented")
    }
}