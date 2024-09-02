package com.vedic.pagination.data

import com.vedic.pagination.data.models.WallPaperViewData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PexelService {

    @GET("v1/curated")
    suspend fun getWallPapers(
        @Query("page") pageNumber: Int = 1,
        @Query("per_page") itemCount: Int = 20
    ): Response<WallPaperViewData>

    @GET("v1/search")
    suspend fun searchWallPapers(
        @Query("page") pageNumber: Int = 1,
        @Query("per_page") itemCount: Int = 20,
        @Query("query") itemName: String
    )
}