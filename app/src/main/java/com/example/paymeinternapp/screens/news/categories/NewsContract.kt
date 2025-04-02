package com.example.paymeinternapp.screens.news.categories

import com.example.domain.entities.ArticleUIData
import kotlinx.coroutines.flow.StateFlow

interface NewsContract {
    sealed interface Intent {
        data class SearchByQuery(val query: String) : Intent
        data class OpenDetails(val item: ArticleUIData) : Intent
        data class ClickedCategory(val category: String) : Intent
        data object PullToRefresh : Intent
    }

    data class UiState(
        val isLoading: Boolean = false,
        val articles: List<ArticleUIData> = listOf(),
        val categories: List<String> = listOf(
            "General", "Business", "Entertainment", "Health", "Science", "Sports", "Technology"
        ),
    )

    interface ViewModel {
        val uiState: StateFlow<UiState>
        fun onEventDispatcher(intent: Intent)
    }
}