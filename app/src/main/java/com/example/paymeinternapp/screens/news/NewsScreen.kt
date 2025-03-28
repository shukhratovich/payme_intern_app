package com.example.paymeinternapp.screens.news

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.example.paymeinternapp.utils.SimpleSearchBar

class NewsScreen(private val modifier: Modifier = Modifier) : Screen {
    @Composable
    override fun Content() {
        NewsScreenContent(modifier)
    }
}

@Composable
private fun NewsScreenContent(modifier: Modifier = Modifier) {
    val textFieldState = rememberTextFieldState()
    var searchResults by remember { mutableStateOf(listOf<String>()) }
    val allItems = listOf("Apple", "Banana", "Cherry", "Date", "Elderberry", "Fig", "Grapes","Apple", "Banana", "Cherry", "Date", "Elderberry", "Fig", "Grapes","Apple", "Banana", "Cherry", "Date", "Elderberry", "Fig", "Grapes","Apple", "Banana", "Cherry", "Date", "Elderberry", "Fig", "Grapes","Apple", "Banana", "Cherry", "Date", "Elderberry", "Fig", "Grapes")

    fun performSearch(query: String, allItems: List<String>) {
        searchResults = if (query.isNotEmpty()) {
            allItems.filter { it.contains(query, ignoreCase = true) }
        } else emptyList()
    }
    LaunchedEffect(textFieldState.text) {
        performSearch(textFieldState.text.toString(),allItems)
    }
    Column(modifier = modifier) {
        SimpleSearchBar(
            textFieldState = textFieldState,
            onSearch = {},
            searchResults = searchResults,
        )

    }
}

