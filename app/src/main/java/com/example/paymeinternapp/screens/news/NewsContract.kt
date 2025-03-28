package com.example.paymeinternapp.screens.news

import com.example.domain.entities.ArticleUIData
import kotlinx.coroutines.flow.StateFlow

interface NewsContract {
    interface Intent {

    }

    data class UiState(
        val isLoading: Boolean = false,
        val articles: List<ArticleUIData> = listOf()
    )

    interface ViewModel {
        val uiState: StateFlow<UiState>
        fun onEventDispatcher(intent: Intent)
    }
}