package com.example.paymeinternapp.screens.news.categories

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecases.news.GetNewsBySearchUseCase
import com.example.domain.usecases.news.GetNewsBySourceUseCase
import com.example.domain.usecases.news.GetNewsCategoryUseCase
import com.example.domain.usecases.news.GetSourcesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.text.isNotBlank

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getNewsBySearchUseCase: GetNewsBySearchUseCase,
    private val getNewsCategoryUseCase: GetNewsCategoryUseCase,
    private val getNewsBySourceUseCase: GetNewsBySourceUseCase,
    private val getSourcesUseCase: GetSourcesUseCase
) :
    ViewModel(), NewsContract.ViewModel {

    override val uiState = MutableStateFlow(NewsContract.UiState())
    private val searchQuery = MutableStateFlow("")

    init {
        getNewsByCategory("general")
        getSourceList()
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
                            reduce {
                                it.copy(
                                    articles = success,
                                    isLoading = false,
                                    isFavoriteItems = false
                                )
                            }
                        }
                        response.onFailure { error ->
                            reduce { it.copy(isLoading = false, isFavoriteItems = false) }
                        }

                    }.launchIn(viewModelScope)

                Log.d("TTT", "out of debounce: ${intent.query}")
            }

            is NewsContract.Intent.ClickedCategory -> {
                reduce { it.copy(isFavoriteItems = false) }
                getNewsByCategory(intent.category)
            }

            NewsContract.Intent.PulledForRefresh -> {
                reduce { it.copy(isRefreshSwiped = true) }
                viewModelScope.launch {
                    Log.d("TTT", "onEventDispatcher: pull to refresh1")
                    delay(1000)
                    reduce { it.copy(isRefreshSwiped = false) }
                    Log.d("TTT", "onEventDispatcher: pull to refresh2")
                }
            }

            NewsContract.Intent.FavoriteItemsClicked -> {
//                val favorites = localNewsRepositoryTest.getFavoriteNews()
//                favorites.onEach { result ->
//                    result.filter { it.isFavorite == true
//                    }.map {
//                        list.add(it.article)
//                        Log.d("AAA", "localNewsRepositoryTest: ${it.article}")
//                    }
//                }.launchIn(viewModelScope)
            }

            NewsContract.Intent.ByCategoryClicked -> {
                reduce { it.copy(isFilterByCategory = true) }
            }

            NewsContract.Intent.BySourcesClicked -> {
                reduce { it.copy(isFilterByCategory = false) }
            }

            is NewsContract.Intent.ClickedSource -> {
                getNewsBySource(intent.sourceId)
            }
        }
    }

    private fun getNewsByCategory(category: String) {
        reduce { it.copy(isLoading = true) }
        val response = getNewsCategoryUseCase.invoke(category)
        response.onEach { result ->
            result.onSuccess { success ->
                reduce { it.copy(articles = success, isLoading = false) }
            }
            result.onFailure { error ->
                reduce { it.copy(isLoading = false) }
            }

        }.launchIn(viewModelScope)
//        reduce { it.copy(articles = uiState.value.articles.map { it.copy(category = categoryForSearch.value) }) }
    }

    private fun getNewsBySource(sourceId: String) {
        reduce { it.copy(isLoading = true) }
        val response = getNewsBySourceUseCase.invoke(sourceId)
        response.onEach { result ->
            result.onSuccess { success ->
                reduce { it.copy(articles = success, isLoading = false) }
            }
            result.onFailure { error ->
                reduce { it.copy(isLoading = false) }
            }

        }.launchIn(viewModelScope)
    }
//    private fun receiving(response: Flow<Result<NewsUIData>>) {
//        reduce { it.copy(isLoading = true) }
//        response.onEach { result ->
//            result.onSuccess { success ->
//                reduce { it.copy(articles = success.articles, isLoading = false) }
//            }
//            result.onFailure { error ->
//                reduce { it.copy(isLoading = false) }
//                Log.d("TTT", "getNewsByCategory: error -> ${error.message}")
//            }
//
//        }.launchIn(viewModelScope)
//    }

    private fun getSourceList() {
        viewModelScope.launch {
            val result = getSourcesUseCase.invoke()
            if (result.isSuccess) {
                result.onSuccess { sources ->
                    reduce { it.copy(sources = sources) }
                }
            }
        }

    }
}