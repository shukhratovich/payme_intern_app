package com.example.paymeinternapp.screens.news.categories

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.entities.CategoryNews
import com.example.domain.entities.NewsUIData
import com.example.domain.usecases.news.GetNewsBySearchUseCase
import com.example.domain.usecases.news.GetNewsCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.text.isNotBlank

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getNewsBySearchUseCase: GetNewsBySearchUseCase,
    private val getNewsCategoryUseCase: GetNewsCategoryUseCase
) :
    ViewModel(), NewsContract.ViewModel {

    override val uiState = MutableStateFlow(NewsContract.UiState())
    private val searchQuery = MutableStateFlow("")
    private val categoryForSearch = MutableStateFlow(CategoryNews.GENERAL)

    init {
//        getNewsByQuery("Apple", "2025-03-25")
        getNewsByCategory("general")
    }

    private inline fun reduce(block: (NewsContract.UiState) -> NewsContract.UiState) {
        val old = uiState.value
        val new = block(old)
        uiState.value = new
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    override fun onEventDispatcher(intent: NewsContract.Intent) {
        when (intent) {
            is NewsContract.Intent.OpenDetails -> {}
            is NewsContract.Intent.SearchByQuery -> {
                searchQuery.value = intent.query
                searchQuery
                    .debounce(1000)
                    .distinctUntilChanged()
                    .filter { it.isNotBlank() }
                    .flatMapLatest { query ->
                        Log.d("TTT", "debounce: ${intent.query}")
                        getNewsBySearchUseCase(q = query, from = "")
                    }
                    .onEach { response ->
                        delay(300)
                        Log.d("TTT", "onEach: ${intent.query}")
                        response.onSuccess { success ->
                            reduce { it.copy(articles = success.articles, isLoading = false) }
                        }
                        response.onFailure { error ->
                            reduce { it.copy(isLoading = false) }
                        }

                    }.launchIn(viewModelScope)

                Log.d("TTT", "out of debounce: ${intent.query}")
            }

            is NewsContract.Intent.ClickedCategory -> {
                categoryForSearch.value = when (intent.category.uppercase()) {
                    "BUSINESS" -> CategoryNews.BUSINESS
                    "GENERAL" -> CategoryNews.GENERAL
                    "ENTERTAINMENT" -> CategoryNews.ENTERTAINMENT
                    "TECHNOLOGY" -> CategoryNews.TECHNOLOGY
                    "HEALTH" -> CategoryNews.HEALTH
                    "SCIENCE" -> CategoryNews.SCIENCE
                    "SPORTS" -> CategoryNews.SPORTS
                    else -> CategoryNews.GENERAL
                }
                getNewsByCategory(intent.category)
            }

            NewsContract.Intent.PullToRefresh -> {

            }
        }
    }
//
//    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
//    fun observeSearchResults(from: String = ""): Flow<Result<NewsUIData>> {
//        Log.d("TTT", "observeSearchResults: ${searchQuery.value}")
//        return
//    }

    private fun getNewsByCategory(category: String) {
        val response = getNewsCategoryUseCase.invoke(category)
        receiving(response)
    }

    private fun receiving(response: Flow<Result<NewsUIData>>) {
        reduce { it.copy(isLoading = true) }
        response.onEach { response ->
            delay(300)
            response.onSuccess { success ->
                reduce { it.copy(articles = success.articles, isLoading = false) }
            }
            response.onFailure { error ->
                reduce { it.copy(isLoading = false) }
                Log.d("TTT", "getNewsByCategory: error -> ${error.message}")
            }

        }.launchIn(viewModelScope)
    }
}