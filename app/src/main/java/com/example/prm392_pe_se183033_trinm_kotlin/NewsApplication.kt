package com.example.prm392_pe_se183033_trinm_kotlin

import android.app.Application
import com.example.prm392_pe_se183033_trinm_kotlin.data.local.AppDatabase
import com.example.prm392_pe_se183033_trinm_kotlin.data.repository.NewsRepository

class NewsApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { NewsRepository(database) }
    
    companion object {
        lateinit var instance: NewsApplication
            private set
    }
    
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}

