package com.example.prm392_pe_se183033_trinm_kotlin.data.api

import com.example.prm392_pe_se183033_trinm_kotlin.data.model.News
import com.example.prm392_pe_se183033_trinm_kotlin.data.model.NewsImage
import com.example.prm392_pe_se183033_trinm_kotlin.data.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface NewsApiService {
    @GET("News")
    suspend fun getAllNews(): Response<NewsResponse>
    
    @GET("News/{id}")
    suspend fun getNewsById(@Path("id") id: Int): Response<News>
    
    @GET("News/category/{category}")
    suspend fun getNewsByCategory(@Path("category") category: String): Response<NewsResponse>
    
    @GET("News/{newsId}/images")
    suspend fun getNewsImages(@Path("newsId") newsId: Int): Response<List<NewsImage>>
}

