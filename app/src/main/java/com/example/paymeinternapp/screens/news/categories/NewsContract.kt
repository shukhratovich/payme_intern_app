package com.example.paymeinternapp.screens.news.categories

import com.example.domain.model.ui.ArticleUIData
import com.example.domain.model.ui.NewsUIData
import kotlinx.coroutines.flow.StateFlow

interface NewsContract {
    sealed interface Intent {
        data class SearchByQuery(val query: String) : Intent
        data class OpenDetails(val item: ArticleUIData) : Intent
        data class ClickedCategory(val category: String) : Intent
        data class ClickedSource(val sourceId: String) : Intent
        data object PulledForRefresh : Intent
        data object FavoriteItemsClicked : Intent
        data object ByCategoryClicked : Intent
        data object BySourcesClicked : Intent
    }

    data class UiState(
        val isLoading: Boolean = false,
        val isRefreshSwiped: Boolean = false,
        val articles: List<NewsUIData> = listOf(),
        val categories: List<String> = listOf(
            "General", "Business", "Entertainment", "Health", "Science", "Sports", "Technology"
        ),
        val sources: List<Pair<String, String>> = listOf(),
        val favorites: List<ArticleUIData> = listOf(),
        val isFilterByCategory: Boolean = true,
        val isFavoriteItems: Boolean = false
    )

    interface ViewModel {
        val uiState: StateFlow<UiState>
        fun onEventDispatcher(intent: Intent)
    }
}