package com.vedic.pagination.data.models

import com.google.gson.annotations.SerializedName

data class WallPaperViewData(
    @SerializedName("next_page")
    val nextPage: String?,
    val page: Int?,
    @SerializedName("per_page")
    val perPage: Int?,
    val photos: List<PhotoViewData?>? = null,
    @SerializedName("prev_page")
    val prevPage: String?,
    @SerializedName("total_result")
    val totalResults: Int?
)