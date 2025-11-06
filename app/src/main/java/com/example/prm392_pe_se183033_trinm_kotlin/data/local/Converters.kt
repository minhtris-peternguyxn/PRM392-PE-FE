package com.example.prm392_pe_se183033_trinm_kotlin.data.local

import androidx.room.TypeConverter
import com.example.prm392_pe_se183033_trinm_kotlin.data.model.NewsImage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()
    
    @TypeConverter
    fun fromNewsImageList(value: List<NewsImage>?): String? {
        return if (value == null) null else gson.toJson(value)
    }
    
    @TypeConverter
    fun toNewsImageList(value: String?): List<NewsImage>? {
        return if (value == null) null else {
            val listType = object : TypeToken<List<NewsImage>>() {}.type
            gson.fromJson(value, listType)
        }
    }
}

