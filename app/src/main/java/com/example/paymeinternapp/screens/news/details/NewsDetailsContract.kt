package com.example.paymeinternapp.screens.news.details

import kotlinx.coroutines.flow.StateFlow

interface NewsDetailsContract {

    sealed interface Intent {

    }

    data class UiState(
        val isLoading: Boolean = false
    )

    interface ViewModel{
        val uiState: StateFlow<UiState>
        fun onEventDispatcher(intent: Intent)
    }
}