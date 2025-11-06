package com.example.prm392_pe_se183033_trinm_kotlin.data.model

import com.google.gson.annotations.SerializedName

data class News(
    val id: Int? = null,
    val title: String,
    val content: String,
    val summary: String? = null,
    val author: String? = null,
    val category: String? = null,
    @SerializedName("thumbnailUrl")
    val thumbnailUrl: String? = null,
    val viewCount: Int = 0,
    val isPublished: Boolean = false,
    val isDeleted: Boolean = false,
    val publishedAt: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val newsImages: List<NewsImage>? = null
)

