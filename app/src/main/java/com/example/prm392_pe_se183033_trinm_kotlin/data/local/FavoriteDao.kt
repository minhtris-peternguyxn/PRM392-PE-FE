package com.example.prm392_pe_se183033_trinm_kotlin.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites ORDER BY addedAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>
    
    @Query("SELECT * FROM favorites WHERE newsId = :newsId")
    suspend fun getFavoriteById(newsId: Int): FavoriteEntity?
    
    @Query("SELECT newsId FROM favorites")
    suspend fun getAllFavoriteIds(): List<Int>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteEntity)
    
    @Delete
    suspend fun delete(favorite: FavoriteEntity)
    
    @Query("DELETE FROM favorites WHERE newsId = :newsId")
    suspend fun deleteByNewsId(newsId: Int)
    
    @Query("SELECT * FROM news n INNER JOIN favorites f ON n.id = f.newsId WHERE n.isDeleted = 0 ORDER BY f.addedAt DESC")
    fun getFavoriteNews(): Flow<List<NewsEntity>>
    
    @Query("SELECT * FROM news n INNER JOIN favorites f ON n.id = f.newsId WHERE n.category = :category AND n.isDeleted = 0 ORDER BY f.addedAt DESC")
    fun getFavoriteNewsByCategory(category: String): Flow<List<NewsEntity>>
    
    @Query("SELECT * FROM news n INNER JOIN favorites f ON n.id = f.newsId WHERE (LOWER(n.title) LIKE '%' || LOWER(:query) || '%' OR LOWER(n.content) LIKE '%' || LOWER(:query) || '%') AND n.isDeleted = 0 ORDER BY f.addedAt DESC")
    fun searchFavoriteNews(query: String): Flow<List<NewsEntity>>
    
    @Query("SELECT * FROM news n INNER JOIN favorites f ON n.id = f.newsId WHERE (LOWER(n.title) LIKE '%' || LOWER(:query) || '%' OR LOWER(n.content) LIKE '%' || LOWER(:query) || '%') AND n.category = :category AND n.isDeleted = 0 ORDER BY f.addedAt DESC")
    fun searchFavoriteNewsByCategory(query: String, category: String): Flow<List<NewsEntity>>
}

