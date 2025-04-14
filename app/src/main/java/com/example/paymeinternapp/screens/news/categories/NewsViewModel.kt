package com.example.paymeinternapp.screens.news.categories

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Network
import com.example.domain.model.ui.CategoryNews
import com.example.domain.model.ui.NewsUIData
import com.example.domain.usecases.news.GetAllFavoriteNewsUseCase
import com.example.domain.usecases.news.GetAllSourceUseCase
import com.example.domain.usecases.news.GetNewsBySearchUseCase
import com.example.domain.usecases.news.GetNewsBySourceUseCase
import com.example.domain.usecases.news.GetNewsCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
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
    private val getAllFavoriteNewsUseCase: GetAllFavoriteNewsUseCase,
    private val getSourcesUseCase: GetAllSourceUseCase
) :
    ViewModel(), NewsContract.ViewModel {

    override val uiState = MutableStateFlow(NewsContract.UiState())
    private val searchQuery = MutableStateFlow("")

    init {
        getNewsByCategory(CategoryNews.BUSINESS)
    }

    private inline fun reduce(block: (NewsContract.UiState) -> NewsContract.UiState) {
        val old = uiState.value
        val new = block(old)
        uiState.value = new
    }

    override fun onEventDispatcher(intent: NewsContract.Intent) {
        when (intent) {
            is NewsContract.Intent.OpenDetails -> {}
            is NewsContract.Intent.SearchByQuery -> {
                getNewsBySearch(intent.query)
            }

            is NewsContract.Intent.ClickedCategory -> {
                reduce { it.copy(isFavoriteItems = false) }
                getNewsByCategory(intent.category)
            }

            NewsContract.Intent.PulledForRefresh -> {
                reduce { it.copy(isRefreshSwiped = true) }
                viewModelScope.launch {
                    delay(1000)
                    getSourceList()
                    reduce { it.copy(isRefreshSwiped = false) }
                }
            }

            NewsContract.Intent.FavoriteItemsClicked -> {
                getFavorites()
            }

            NewsContract.Intent.ByCategoryClicked -> {
                reduce { it.copy(isFilterByCategory = true) }
            }

            NewsContract.Intent.BySourcesClicked -> {
                getSourceList()
            }

            is NewsContract.Intent.ClickedSource -> {
                getNewsBySource(intent.sourceId)
            }
        }
    }

    private fun getFavorites() {
        viewModelScope.launch {
            val favoriteNews = getAllFavoriteNewsUseCase.invoke().first()
            reduce { it.copy(isFavoriteItems = true, articles = favoriteNews.map { it.article }) }
        }
    }

    private fun getNewsByCategory(category: CategoryNews) {
        reduce { it.copy(isLoading = true) }
        val response = getNewsCategoryUseCase.invoke(category)
        response.onEach { result ->
            when (result) {
                is Network.Error -> reduce {
                    it.copy(
                        errorMessage = result.message.message,
                        isLoading = false
                    )
                }

                is Network.Success<List<NewsUIData>> -> {
                    reduce { it.copy(articles = result.data, isLoading = false) }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getNewsBySource(sourceId: String) {
        reduce { it.copy(isLoading = true) }
        val response = getNewsBySourceUseCase.invoke(sourceId)
        response.onEach { result ->
            when (result) {
                is Network.Error -> reduce {
                    it.copy(
                        errorMessage = result.message.message,
                        isLoading = false
                    )
                }

                is Network.Success<List<NewsUIData>> -> {
                    reduce { it.copy(articles = result.data, isLoading = false) }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getSourceList() {
        viewModelScope.launch {
            getSourcesUseCase.invoke().collect { result ->
                result.fold(
                    onSuccess = { sources ->
                        reduce {
                            it.copy(
                                sources = sources,
                                isFilterByCategory = false,
                                isFavoriteItems = false
                            )
                        }
                    },
                    onFailure = { error ->
                        reduce { it.copy(errorMessage = error.message) }
                    }
                )
            }
        }
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun getNewsBySearch(query: String) {
        if (query.isNotBlank()) {
            reduce { it.copy(isLoading = true) }
            searchQuery.value = query
            searchQuery
                .debounce(500)
                .distinctUntilChanged()
                .filter { it.isNotBlank() }
                .flatMapLatest { query ->
                    getNewsBySearchUseCase(q = query, from = "")
                }
                .onEach { response ->
                    delay(300)
                    when (response) {
                        is Network.Error -> reduce {
                            it.copy(
                                errorMessage = response.message.message,
                                isLoading = false,
                                isFavoriteItems = false
                            )
                        }

                        is Network.Success<List<NewsUIData>> -> {
                            reduce {
                                it.copy(
                                    articles = response.data,
                                    isLoading = false,
                                    isFavoriteItems = false
                                )
                            }
                        }
                    }
                }.launchIn(viewModelScope)
        }
    }
}