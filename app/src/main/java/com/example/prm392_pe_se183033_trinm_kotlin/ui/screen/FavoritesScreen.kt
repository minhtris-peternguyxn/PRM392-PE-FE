package com.example.prm392_pe_se183033_trinm_kotlin.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.prm392_pe_se183033_trinm_kotlin.NewsApplication
import com.example.prm392_pe_se183033_trinm_kotlin.data.local.NewsEntity
import com.example.prm392_pe_se183033_trinm_kotlin.ui.screen.CategoryFilter
import com.example.prm392_pe_se183033_trinm_kotlin.ui.screen.NewsList
import com.example.prm392_pe_se183033_trinm_kotlin.ui.screen.SearchBar
import com.example.prm392_pe_se183033_trinm_kotlin.ui.viewmodel.FavoritesViewModel
import com.example.prm392_pe_se183033_trinm_kotlin.ui.viewmodel.FavoritesViewModelFactory
import com.example.prm392_pe_se183033_trinm_kotlin.ui.viewmodel.FavoritesUiState

@Composable
fun FavoritesScreen(
    onNewsClick: (Int) -> Unit,
    viewModel: FavoritesViewModel = viewModel(factory = FavoritesViewModelFactory(
        NewsApplication.instance.repository
    ))
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val categories by viewModel.categories.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadFavorites()
    }
    
    Column(modifier = Modifier.fillMaxSize()) {
        // Search Bar
        SearchBar(
            query = searchQuery,
            onQueryChange = { viewModel.updateSearchQuery(it) },
            modifier = Modifier.fillMaxWidth()
        )
        
        // Category Filter
        if (categories.isNotEmpty()) {
            CategoryFilter(
                categories = categories,
                selectedCategory = selectedCategory,
                onCategorySelected = { viewModel.selectCategory(it) },
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        // Content
        when (val state = uiState) {
            is FavoritesUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is FavoritesUiState.Success -> {
                NewsList(
                    news = state.news,
                    onNewsClick = onNewsClick
                )
            }
            is FavoritesUiState.Empty -> {
                EmptyFavoritesState()
            }
        }
    }
}

@Composable
fun EmptyFavoritesState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Bạn chưa có bài viết yêu thích nào",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

