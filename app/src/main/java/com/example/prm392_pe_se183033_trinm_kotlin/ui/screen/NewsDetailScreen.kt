package com.example.prm392_pe_se183033_trinm_kotlin.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.prm392_pe_se183033_trinm_kotlin.NewsApplication
import com.example.prm392_pe_se183033_trinm_kotlin.ui.viewmodel.NewsDetailViewModel
import com.example.prm392_pe_se183033_trinm_kotlin.ui.viewmodel.NewsDetailViewModelFactory
import com.example.prm392_pe_se183033_trinm_kotlin.ui.viewmodel.NewsDetailUiState

@Composable
fun NewsDetailScreen(
    newsId: Int,
    onBack: () -> Unit,
    viewModel: NewsDetailViewModel = viewModel(factory = NewsDetailViewModelFactory(
        NewsApplication.instance.repository
    ))
) {
    val uiState by viewModel.uiState.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    val images by viewModel.images.collectAsState()
    
    LaunchedEffect(newsId) {
        viewModel.loadNews(newsId)
    }
    
    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Chi tiết bài viết") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.toggleFavorite(newsId) }
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is NewsDetailUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is NewsDetailUiState.Success -> {
                NewsDetailContent(
                    news = state.news,
                    images = images,
                    modifier = Modifier.padding(paddingValues)
                )
            }
            is NewsDetailUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun NewsDetailContent(
    news: com.example.prm392_pe_se183033_trinm_kotlin.data.local.NewsEntity,
    images: List<com.example.prm392_pe_se183033_trinm_kotlin.data.model.NewsImage>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = news.title,
                style = MaterialTheme.typography.headlineMedium
            )
        }
        
        if (!news.category.isNullOrEmpty()) {
            item {
                Text(
                    text = news.category,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        if (!news.author.isNullOrEmpty()) {
            item {
                Text(
                    text = "Tác giả: ${news.author}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        if (!news.thumbnailUrl.isNullOrEmpty()) {
            item {
                AsyncImage(
                    model = news.thumbnailUrl,
                    contentDescription = news.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
        
        if (images.isNotEmpty()) {
            items(images) { image ->
                AsyncImage(
                    model = image.imageUrl,
                    contentDescription = image.caption ?: "News image",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )
                if (!image.caption.isNullOrEmpty()) {
                    Text(
                        text = image.caption,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        
        if (!news.summary.isNullOrEmpty()) {
            item {
                Text(
                    text = news.summary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        
        item {
            Text(
                text = news.content,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        
        item {
            Text(
                text = "Lượt xem: ${news.viewCount}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

