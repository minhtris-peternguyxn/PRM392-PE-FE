package com.example.prm392_pe_se183033_trinm_kotlin.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.prm392_pe_se183033_trinm_kotlin.data.local.NewsEntity
import com.example.prm392_pe_se183033_trinm_kotlin.data.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(private val repository: NewsRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Loading)
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()
    
    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories.asStateFlow()
    
    init {
        loadFavorites()
        loadCategories()
    }
    
    fun loadFavorites() {
        viewModelScope.launch {
            observeFavorites()
        }
    }
    
    private fun observeFavorites() {
        viewModelScope.launch {
            val query = _searchQuery.value
            val category = _selectedCategory.value
            
            if (query.isNotEmpty()) {
                repository.searchFavoriteNews(query, category).collect { news ->
                    if (news.isEmpty()) {
                        _uiState.value = FavoritesUiState.Empty
                    } else {
                        _uiState.value = FavoritesUiState.Success(news)
                    }
                }
            } else if (category != null && category.isNotEmpty()) {
                repository.getFavoriteNews(category).collect { news ->
                    if (news.isEmpty()) {
                        _uiState.value = FavoritesUiState.Empty
                    } else {
                        _uiState.value = FavoritesUiState.Success(news)
                    }
                }
            } else {
                repository.getFavoriteNews().collect { news ->
                    if (news.isEmpty()) {
                        _uiState.value = FavoritesUiState.Empty
                    } else {
                        _uiState.value = FavoritesUiState.Success(news)
                    }
                }
            }
        }
    }
    
    fun loadCategories() {
        viewModelScope.launch {
            repository.getFavoriteNewsCategories().collect { categories ->
                _categories.value = categories
            }
        }
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        observeFavorites()
    }
    
    fun selectCategory(category: String?) {
        _selectedCategory.value = category
        observeFavorites()
    }
}

sealed class FavoritesUiState {
    object Loading : FavoritesUiState()
    data class Success(val news: List<NewsEntity>) : FavoritesUiState()
    object Empty : FavoritesUiState()
}

class FavoritesViewModelFactory(private val repository: NewsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoritesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

