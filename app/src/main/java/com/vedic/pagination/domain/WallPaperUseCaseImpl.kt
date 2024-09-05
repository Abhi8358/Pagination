package com.vedic.pagination.domain

import com.vedic.pagination.core.ErrorStringType
import com.vedic.pagination.core.UiStateResource
import com.vedic.pagination.data.models.WallPaperViewData
import kotlinx.coroutines.flow.Flow

class WallPaperUseCaseImpl(private val repo: WallPaperRepo) : WallPaperUseCase{
    override suspend fun getWallPapers(pageNumber: Int, itemCount: Int): Flow<UiStateResource<WallPaperViewData>> {
        return repo.getWallPapers(pageNumber, itemCount)

    }

    override suspend fun searchWallPapers(pageNumber: Int, itemCount: Int, itemName: String) {
        TODO("Not yet implemented")
    }

}