package com.vedic.pagination.domain

import com.vedic.pagination.core.ErrorStringType
import com.vedic.pagination.core.UiStateResource
import com.vedic.pagination.data.models.WallPaperViewData
import kotlinx.coroutines.flow.Flow

interface WallPaperRepo {
    suspend fun getWallPapers(pageNumber: Int = 1, itemCount: Int = 10): Flow<UiStateResource<WallPaperViewData>>
    suspend fun searchWallPapers(pageNumber: Int, itemCount: Int, itemName: String): Flow<UiStateResource<WallPaperViewData>>
}