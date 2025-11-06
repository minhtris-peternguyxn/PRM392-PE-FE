package com.example.prm392_pe_se183033_trinm_kotlin.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.prm392_pe_se183033_trinm_kotlin.data.model.NewsImage

@Entity(tableName = "news")
@TypeConverters(Converters::class)
data class NewsEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val content: String,
    val summary: String? = null,
    val author: String? = null,
    val category: String? = null,
    val thumbnailUrl: String? = null,
    val viewCount: Int = 0,
    val isPublished: Boolean = false,
    val isDeleted: Boolean = false,
    val publishedAt: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val newsImages: List<NewsImage>? = null
)

