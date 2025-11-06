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

class NewsListViewModel(private val repository: NewsRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<NewsListUiState>(NewsListUiState.Loading)
    val uiState: StateFlow<NewsListUiState> = _uiState.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()
    
    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories.asStateFlow()
    
    init {
        loadNews()
        loadCategories()
    }
    
    fun loadNews() {
        viewModelScope.launch {
            _uiState.value = NewsListUiState.Loading
            try {
                repository.fetchAndCacheNews()
                observeNews()
            } catch (e: Exception) {
                // Try to load from database
                observeNews()
            }
        }
    }
    
    private fun observeNews() {
        viewModelScope.launch {
            val query = _searchQuery.value
            val category = _selectedCategory.value
            
            if (query.isNotEmpty()) {
                repository.searchNews(query, category).collect { news ->
                    if (news.isEmpty() && query.isNotEmpty()) {
                        _uiState.value = NewsListUiState.Empty
                    } else {
                        _uiState.value = NewsListUiState.Success(news)
                    }
                }
            } else if (category != null && category.isNotEmpty()) {
                repository.getNewsByCategory(category).collect { news ->
                    _uiState.value = NewsListUiState.Success(news)
                }
            } else {
                repository.getAllNews().collect { news ->
                    if (news.isEmpty()) {
                        _uiState.value = NewsListUiState.Error("Không thể tải dữ liệu. Vui lòng kiểm tra kết nối của bạn.")
                    } else {
                        _uiState.value = NewsListUiState.Success(news)
                    }
                }
            }
        }
    }
    
    fun loadCategories() {
        viewModelScope.launch {
            _categories.value = repository.getAllCategories()
        }
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        observeNews()
    }
    
    fun selectCategory(category: String?) {
        _selectedCategory.value = category
        observeNews()
    }
}

sealed class NewsListUiState {
    object Loading : NewsListUiState()
    data class Success(val news: List<NewsEntity>) : NewsListUiState()
    data class Error(val message: String) : NewsListUiState()
    object Empty : NewsListUiState()
}

class NewsListViewModelFactory(private val repository: NewsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewsListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

