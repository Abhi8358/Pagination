package com.vedic.pagination.data.models

import com.google.gson.annotations.SerializedName

data class PhotoViewData(
    val alt: String?,
    @SerializedName("avgColor")
    val avg_color: String?,
    val height: Int?,
    val id: Int?,
    val liked: Boolean?,
    val photographer: String?,
    @SerializedName("photographerId")
    val photographer_id: Int?,
    @SerializedName("photographerUrl")
    val photographer_url: String?,
    val src: SrcViewData?,
    val url: String?,
    val width: Int?
)