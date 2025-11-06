package com.example.prm392_pe_se183033_trinm_kotlin.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey
    val newsId: Int,
    val addedAt: Long = System.currentTimeMillis()
)

