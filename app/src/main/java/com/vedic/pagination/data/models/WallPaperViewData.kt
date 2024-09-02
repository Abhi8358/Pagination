package com.vedic.pagination.data.models

import com.google.gson.annotations.SerializedName

data class WallPaperViewData(
    @SerializedName("nextPage")
    val next_page: String?,
    val page: Int?,
    @SerializedName("perPage")
    val per_page: Int?,
    val photos: List<PhotoViewData?>? = null,
    @SerializedName("prevPage")
    val prev_page: String?,
    @SerializedName("totalResult")
    val total_results: Int?
)