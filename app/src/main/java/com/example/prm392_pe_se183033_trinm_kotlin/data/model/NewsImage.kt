package com.example.prm392_pe_se183033_trinm_kotlin.data.model

import com.google.gson.annotations.SerializedName

data class NewsImage(
    val id: Int? = null,
    val newsId: Int,
    @SerializedName("imageUrl")
    val imageUrl: String,
    val caption: String? = null,
    val displayOrder: Int = 0,
    val createdAt: String? = null
)

