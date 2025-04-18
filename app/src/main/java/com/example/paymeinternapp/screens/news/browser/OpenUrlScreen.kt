package com.example.paymeinternapp.screens.news.browser

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import cafe.adriel.voyager.core.screen.Screen
import com.example.paymeinternapp.R
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState

class OpenUrlScreen(private val url: String) : Screen {
    @Composable
    override fun Content() {
        OpenUrl(url = url)
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun OpenUrl(url: String) {
    val state = rememberWebViewState(url = url)
    val navigator = rememberWebViewNavigator()


    Scaffold { innerPadding ->
        if (url.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
            ) {
                WebView(
                    state = state,
                    navigator = navigator,
                    onCreated = { it.settings.javaScriptEnabled = true },
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(R.drawable.page_not_found),
                    contentDescription = "Page not found",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}