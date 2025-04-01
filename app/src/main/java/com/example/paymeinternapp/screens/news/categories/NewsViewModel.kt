package com.example.paymeinternapp.screens.news.categories

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecases.news.GetNewsBySearchUseCase
import com.example.domain.usecases.news.GetNewsCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getNewsBySearchUseCase: GetNewsBySearchUseCase,
    private val getNewsCategoryUseCase: GetNewsCategoryUseCase
) :
    ViewModel(), NewsContract.ViewModel {

    override val uiState = MutableStateFlow(NewsContract.UiState())

    init {
//        getNewsByQuery("Apple", "2025-03-25")
        getNewsByCategory("general")
    }

    private inline fun reduce(block: (NewsContract.UiState) -> NewsContract.UiState) {
        val old = uiState.value
        val new = block(old)
        uiState.value = new
    }

    override fun onEventDispatcher(intent: NewsContract.Intent) {
        when (intent) {
            is NewsContract.Intent.OpenDetails -> TODO()
            is NewsContract.Intent.SearchByQuery -> {
                Log.d("TTT", "onEventDispatcher: ${intent.query}")
                getNewsByQuery(query = intent.query, from = "")
            }

            is NewsContract.Intent.ClickedCategory -> getNewsByCategory(intent.category)
        }
    }


    private fun getNewsByQuery(query: String, from: String) {
        val response = getNewsBySearchUseCase.invoke(query, from)

        response.onEach { response ->

            response.onSuccess { success ->
                reduce { it.copy(articles = success.articles) }
            }
            response.onFailure { error ->
            }

        }.launchIn(viewModelScope)
    }

    private fun getNewsByCategory(category: String) {
        val response = getNewsCategoryUseCase.invoke(category)

        response.onEach { response ->

            response.onSuccess { success ->
                reduce { it.copy(articles = success.articles) }
            }
            response.onFailure { error ->
            }

        }.launchIn(viewModelScope)
    }
}