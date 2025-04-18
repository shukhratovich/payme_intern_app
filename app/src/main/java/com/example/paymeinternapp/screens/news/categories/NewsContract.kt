package com.example.paymeinternapp.screens.news.categories

import com.example.domain.model.ui.ArticleUIData
import com.example.domain.model.ui.CategoryNews
import com.example.domain.model.ui.NewsSourcesUIData
import com.example.domain.model.ui.NewsUIData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface NewsContract {
    sealed interface Intent {
        data class SearchByQuery(val query: String) : Intent
        data class OpenDetails(val item: ArticleUIData) : Intent
        data class ClickedCategory(val category: CategoryNews) : Intent
        data class ClickedSource(val sourceId: String) : Intent
        data object PulledForRefresh : Intent
        data object FavoriteItemsClicked : Intent
        data object ByCategoryClicked : Intent
        data object BySourcesClicked : Intent
    }

    data class UiState(
        val errorMessage: String? = null,
        val isLoading: Boolean = false,
        val isRefreshSwiped: Boolean = false,
        val articles: List<NewsUIData> = listOf(),
        val categories: List<CategoryNews> = CategoryNews.entries.filter { it != CategoryNews.NULL },
        val sources: List<NewsSourcesUIData> = listOf(),
        val isFilterByCategory: Boolean = true,
        val isFavoriteItems: Boolean = false,
    )

    sealed class SideEffect {
        data class Snackbar(val message: String): SideEffect()
    }

    interface ViewModel {
        val uiState: StateFlow<UiState>
        val effect: SharedFlow<SideEffect>
        fun onEventDispatcher(intent: Intent)
    }

}