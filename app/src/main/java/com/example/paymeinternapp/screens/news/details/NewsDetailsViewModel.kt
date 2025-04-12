package com.example.paymeinternapp.screens.news.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.ui.NewsUIData
import com.example.domain.usecases.news.AddFavoriteNewsToLocalUseCase
import com.example.domain.usecases.news.GetAllFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailsViewModel @Inject constructor(
    private val addFavoriteNewsToLocalUseCase: AddFavoriteNewsToLocalUseCase,
    private val getAllFavoriteNewsUseCase: GetAllFavoriteUseCase
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
                reduce { it.copy(isFavorite = !uiState.value.isFavorite) }
                viewModelScope.launch {
                    addFavoriteNewsToLocalUseCase.invoke(
                        intent.article,
                        isFavorite = uiState.value.isFavorite
                    )
                        .collect()
                }
            }

            is NewsDetailsContract.Intent.CheckFavorite -> {
                dataIsFavorite(intent.data)
            }
        }
    }

    private fun dataIsFavorite(data: NewsUIData) {
        viewModelScope.launch {
            getAllFavoriteNewsUseCase.invoke().collect { favoriteNews ->
                reduce { it.copy(isFavorite = favoriteNews.any { it.article.url == data.url }) }
            }
        }
    }
}