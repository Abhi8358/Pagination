package com.vedic.pagination.domain

import com.vedic.pagination.core.ErrorStringType
import com.vedic.pagination.core.LoadingType
import com.vedic.pagination.core.UiStateResource
import com.vedic.pagination.data.models.WallPaperViewData
import kotlinx.coroutines.flow.Flow

interface WallPaperUseCase {

    suspend fun getWallPapers(pageNumber: Int = 1, itemCount: Int = 10, start: (LoadingType) -> Unit, onError: (ErrorStringType) -> Unit): Flow<WallPaperViewData>
    suspend fun searchWallPapers(pageNumber: Int, itemCount: Int, itemName: String)
}