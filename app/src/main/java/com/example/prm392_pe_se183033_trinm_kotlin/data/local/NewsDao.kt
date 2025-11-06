package com.example.prm392_pe_se183033_trinm_kotlin.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Query("SELECT * FROM news WHERE isDeleted = 0 ORDER BY createdAt DESC")
    fun getAllNews(): Flow<List<NewsEntity>>
    
    @Query("SELECT * FROM news WHERE id = :id")
    suspend fun getNewsById(id: Int): NewsEntity?
    
    @Query("SELECT * FROM news WHERE category = :category AND isDeleted = 0 ORDER BY createdAt DESC")
    fun getNewsByCategory(category: String): Flow<List<NewsEntity>>
    
    @Query("SELECT DISTINCT category FROM news WHERE category IS NOT NULL AND category != '' AND isDeleted = 0")
    suspend fun getAllCategories(): List<String>
    
    @Query("SELECT * FROM news WHERE (LOWER(title) LIKE '%' || LOWER(:query) || '%' OR LOWER(content) LIKE '%' || LOWER(:query) || '%') AND isDeleted = 0 ORDER BY createdAt DESC")
    fun searchNews(query: String): Flow<List<NewsEntity>>
    
    @Query("SELECT * FROM news WHERE (LOWER(title) LIKE '%' || LOWER(:query) || '%' OR LOWER(content) LIKE '%' || LOWER(:query) || '%') AND category = :category AND isDeleted = 0 ORDER BY createdAt DESC")
    fun searchNewsByCategory(query: String, category: String): Flow<List<NewsEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(news: List<NewsEntity>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(news: NewsEntity)
    
    @Update
    suspend fun update(news: NewsEntity)
    
    @Delete
    suspend fun delete(news: NewsEntity)
    
    @Query("DELETE FROM news")
    suspend fun deleteAll()
}

