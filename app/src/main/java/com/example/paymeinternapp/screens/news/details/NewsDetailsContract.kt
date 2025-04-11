package com.example.paymeinternapp.screens.news.details

import com.example.domain.model.ui.ArticleUIData
import com.example.domain.model.ui.NewsUIData
import kotlinx.coroutines.flow.StateFlow

interface NewsDetailsContract {

    sealed interface Intent {
        data class CheckFavorite(val data: NewsUIData) : Intent
        data class FavoriteClicked(val article: NewsUIData) : Intent
    }

    data class UiState(
        val isLoading: Boolean = false,
        val isFavorite: Boolean = false
    )

    interface ViewModel {
        val uiState: StateFlow<UiState>
        fun onEventDispatcher(intent: Intent)
    }
}