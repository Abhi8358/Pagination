package com.vedic.pagination.domain

import com.vedic.pagination.core.UiStateResource
import kotlinx.coroutines.flow.Flow

class WallPaperUseCaseImpl(val repo: WallPaperRepo) : WallPaperUseCase{
    override suspend fun getWallPapers(pageNumber: Int, itemCount: Int): Flow<UiStateResource> {
        return repo.getWallPapers(pageNumber, itemCount)

    }

    override suspend fun searchWallPapers(pageNumber: Int, itemCount: Int, itemName: String) {
        TODO("Not yet implemented")
    }

}