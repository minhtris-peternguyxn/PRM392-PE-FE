package com.example.prm392_pe_se183033_trinm_kotlin.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.prm392_pe_se183033_trinm_kotlin.data.local.NewsEntity
import com.example.prm392_pe_se183033_trinm_kotlin.data.model.NewsImage
import com.example.prm392_pe_se183033_trinm_kotlin.data.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewsDetailViewModel(private val repository: NewsRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<NewsDetailUiState>(NewsDetailUiState.Loading)
    val uiState: StateFlow<NewsDetailUiState> = _uiState.asStateFlow()
    
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()
    
    private val _images = MutableStateFlow<List<NewsImage>>(emptyList())
    val images: StateFlow<List<NewsImage>> = _images.asStateFlow()
    
    fun loadNews(id: Int) {
        viewModelScope.launch {
            _uiState.value = NewsDetailUiState.Loading
            try {
                val news = repository.getNewsById(id)
                if (news != null) {
                    _uiState.value = NewsDetailUiState.Success(news)
                    _isFavorite.value = repository.isFavorite(id)
                    _images.value = repository.getNewsImages(id)
                } else {
                    _uiState.value = NewsDetailUiState.Error("Không tìm thấy bài viết")
                }
            } catch (e: Exception) {
                _uiState.value = NewsDetailUiState.Error("Lỗi: ${e.message}")
            }
        }
    }
    
    fun toggleFavorite(newsId: Int) {
        viewModelScope.launch {
            _isFavorite.value = repository.toggleFavorite(newsId)
        }
    }
}

sealed class NewsDetailUiState {
    object Loading : NewsDetailUiState()
    data class Success(val news: NewsEntity) : NewsDetailUiState()
    data class Error(val message: String) : NewsDetailUiState()
}

class NewsDetailViewModelFactory(private val repository: NewsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewsDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

