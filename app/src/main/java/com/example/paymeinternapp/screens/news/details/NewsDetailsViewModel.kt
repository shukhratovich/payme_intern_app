package com.example.paymeinternapp.screens.news.details

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class NewsDetailsViewModel @Inject constructor() : ViewModel(), NewsDetailsContract.ViewModel {

    override val uiState = MutableStateFlow(NewsDetailsContract.UiState())


    override fun onEventDispatcher(intent: NewsDetailsContract.Intent) {
        when (intent) {

            else -> {}
        }
    }
}