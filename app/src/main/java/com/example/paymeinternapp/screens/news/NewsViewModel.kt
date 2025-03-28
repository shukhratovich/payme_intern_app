package com.example.paymeinternapp.screens.news

import androidx.lifecycle.ViewModel
import com.example.domain.usecases.news.GetNewsBySearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val getNewsBySearchUseCase: GetNewsBySearchUseCase) :
    ViewModel() {

        init {

        }
}