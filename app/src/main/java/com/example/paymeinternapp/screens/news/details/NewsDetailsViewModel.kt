package com.example.paymeinternapp.screens.news.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecases.news.AddFavoriteNewsToLocalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailsViewModel @Inject constructor(
    private val addFavoriteNewsToLocalUseCase: AddFavoriteNewsToLocalUseCase
) : ViewModel(), NewsDetailsContract.ViewModel {

    override val uiState = MutableStateFlow(NewsDetailsContract.UiState())


    private inline fun reduce(block: (NewsDetailsContract.UiState) -> NewsDetailsContract.UiState) {
        val old = uiState.value
        val new = block(old)
        uiState.value = new
    }

    override fun onEventDispatcher(intent: NewsDetailsContract.Intent) {
        when (intent) {
            is NewsDetailsContract.Intent.FavoriteClicked -> {
                reduce { it.copy(isFavorite = !intent.isFavorite) }
                viewModelScope.launch {
                    addFavoriteNewsToLocalUseCase.invoke(intent.article.copy(/*isFavorite = !intent.isFavorite*/))
                        .collect()
                }
            }
        }
    }
}