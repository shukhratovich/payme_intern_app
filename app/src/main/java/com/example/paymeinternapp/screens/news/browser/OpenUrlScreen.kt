package com.example.paymeinternapp.screens.news.browser

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState

class OpenUrlScreen(private val url: String) : Screen {
    @Composable
    override fun Content() {
        OpenUrl(url = url)
    }
}

@Composable
private fun OpenUrl(url: String) {
    val state = rememberWebViewState(url = url)
    val navigator = rememberWebViewNavigator()
    Scaffold { innerPadding ->
        WebView(
            state = state,
            navigator = navigator,
            onCreated = { it.settings.javaScriptEnabled = true },
            modifier = Modifier.padding(innerPadding)
        )
    }
}