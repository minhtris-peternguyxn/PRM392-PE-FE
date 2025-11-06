package com.example.prm392_pe_se183033_trinm_kotlin.data.api

import com.example.prm392_pe_se183033_trinm_kotlin.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }
    
    private val apiInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header("User-Agent", "Android-App")
            .header("Accept", "application/json")
            .header("Content-Type", "application/json")
            .build()
        val response = chain.proceed(newRequest)
        
        if (!response.isSuccessful && BuildConfig.DEBUG) {
            android.util.Log.e("ApiClient", "Request failed: ${response.code} ${response.message}")
        }
        
        response
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(apiInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL + "/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    val newsApiService: NewsApiService = retrofit.create(NewsApiService::class.java)
}

