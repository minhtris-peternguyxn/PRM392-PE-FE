package com.example.prm392_pe_se183033_trinm_kotlin.data.repository

import com.example.prm392_pe_se183033_trinm_kotlin.BuildConfig
import com.example.prm392_pe_se183033_trinm_kotlin.data.api.ApiClient
import com.example.prm392_pe_se183033_trinm_kotlin.data.local.AppDatabase
import com.example.prm392_pe_se183033_trinm_kotlin.data.local.FavoriteEntity
import com.example.prm392_pe_se183033_trinm_kotlin.data.local.NewsEntity
import com.example.prm392_pe_se183033_trinm_kotlin.data.model.News
import com.example.prm392_pe_se183033_trinm_kotlin.utils.StringUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewsRepository(private val database: AppDatabase) {
    private val newsDao = database.newsDao()
    private val favoriteDao = database.favoriteDao()
    private val apiService = ApiClient.newsApiService
    
    suspend fun fetchAndCacheNews(): Result<Unit> {
        return try {
            val response = apiService.getAllNews()
            if (response.isSuccessful && response.body() != null) {
                val newsResponse = response.body()!!
                val newsList = newsResponse.data ?: emptyList()
                val newsEntities = newsList.map { it.toEntity() }
                newsDao.insertAll(newsEntities)
                Result.success(Unit)
            } else {
                val errorMessage = "Failed to fetch news: ${response.code()} - ${response.message()}"
                if (BuildConfig.DEBUG) {
                    android.util.Log.e("NewsRepository", errorMessage)
                }
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                android.util.Log.e("NewsRepository", "Error fetching news: ${e.message}", e)
            }
            Result.failure(e)
        }
    }
    
    fun getAllNews(): Flow<List<NewsEntity>> = newsDao.getAllNews()
    
    suspend fun getNewsById(id: Int): NewsEntity? {
        // Try to fetch from API first, then cache
        try {
            val response = apiService.getNewsById(id)
            if (response.isSuccessful && response.body() != null) {
                val news = response.body()!!
                newsDao.insert(news.toEntity())
                return news.toEntity()
            }
        } catch (e: Exception) {
            // Fallback to database
        }
        return newsDao.getNewsById(id)
    }
    
    suspend fun getNewsImages(newsId: Int): List<com.example.prm392_pe_se183033_trinm_kotlin.data.model.NewsImage> {
        return try {
            val response = apiService.getNewsImages(newsId)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    fun getNewsByCategory(category: String): Flow<List<NewsEntity>> = newsDao.getNewsByCategory(category)
    
    suspend fun getAllCategories(): List<String> = newsDao.getAllCategories()
    
    fun searchNews(query: String, category: String? = null): Flow<List<NewsEntity>> = flow {
        if (query.isEmpty()) {
            if (category != null && category.isNotEmpty()) {
                newsDao.getNewsByCategory(category).collect { emit(it) }
            } else {
                newsDao.getAllNews().collect { emit(it) }
            }
        } else {
            // Get all news first (either by category or all), then filter
            val sourceFlow = if (category != null && category.isNotEmpty()) {
                newsDao.getNewsByCategory(category)
            } else {
                newsDao.getAllNews()
            }
            
            sourceFlow.collect { allNews ->
                // Filter results to support Vietnamese diacritic-insensitive search
                val filtered = allNews.filter { news ->
                    StringUtils.containsIgnoreCaseAndDiacritics(news.title, query) ||
                    (news.content?.let { StringUtils.containsIgnoreCaseAndDiacritics(it, query) } ?: false)
                }
                emit(filtered)
            }
        }
    }
    
    suspend fun toggleFavorite(newsId: Int): Boolean {
        val existing = favoriteDao.getFavoriteById(newsId)
        return if (existing != null) {
            favoriteDao.deleteByNewsId(newsId)
            false
        } else {
            favoriteDao.insert(FavoriteEntity(newsId = newsId))
            true
        }
    }
    
    suspend fun isFavorite(newsId: Int): Boolean {
        return favoriteDao.getFavoriteById(newsId) != null
    }
    
    fun getFavoriteNews(category: String? = null): Flow<List<NewsEntity>> = flow {
        if (category != null && category.isNotEmpty()) {
            favoriteDao.getFavoriteNewsByCategory(category).collect { emit(it) }
        } else {
            favoriteDao.getFavoriteNews().collect { emit(it) }
        }
    }
    
    fun searchFavoriteNews(query: String, category: String? = null): Flow<List<NewsEntity>> = flow {
        if (query.isEmpty()) {
            if (category != null && category.isNotEmpty()) {
                favoriteDao.getFavoriteNewsByCategory(category).collect { emit(it) }
            } else {
                favoriteDao.getFavoriteNews().collect { emit(it) }
            }
        } else {
            // Get all favorite news first (either by category or all), then filter
            val sourceFlow = if (category != null && category.isNotEmpty()) {
                favoriteDao.getFavoriteNewsByCategory(category)
            } else {
                favoriteDao.getFavoriteNews()
            }
            
            sourceFlow.collect { allNews ->
                // Filter results to support Vietnamese diacritic-insensitive search
                val filtered = allNews.filter { news ->
                    StringUtils.containsIgnoreCaseAndDiacritics(news.title, query) ||
                    (news.content?.let { StringUtils.containsIgnoreCaseAndDiacritics(it, query) } ?: false)
                }
                emit(filtered)
            }
        }
    }
    
    fun getFavoriteNewsCategories(): Flow<List<String>> = flow {
        favoriteDao.getFavoriteNews().collect { newsList ->
            emit(newsList.mapNotNull { it.category }.distinct().sorted())
        }
    }
    
    private fun News.toEntity(): NewsEntity {
        return NewsEntity(
            id = this.id ?: 0,
            title = this.title,
            content = this.content,
            summary = this.summary,
            author = this.author,
            category = this.category,
            thumbnailUrl = this.thumbnailUrl,
            viewCount = this.viewCount,
            isPublished = this.isPublished,
            isDeleted = this.isDeleted,
            publishedAt = this.publishedAt,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            newsImages = this.newsImages
        )
    }
}

