package com.example.prm392_pe_se183033_trinm_kotlin.data.model

import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("totalCount")
    val totalCount: Int? = null,
    @SerializedName("publishedCount")
    val publishedCount: Int? = null,
    @SerializedName("returnedCount")
    val returnedCount: Int? = null,
    @SerializedName("data")
    val data: List<News>? = null
)

