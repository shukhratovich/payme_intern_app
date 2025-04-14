package com.example.paymeinternapp.screens.weather

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import coil3.compose.AsyncImage

class WeatherScreen(private val modifier: Modifier = Modifier) : Screen {
    @Composable
    override fun Content() {
        val viewModel: WeatherContract.ViewModel = getViewModel<WeatherViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        WeatherScreenContent(
            modifier = modifier,
            uiState = uiState,
            onEventDispatcher = viewModel::onEventDispatcher
        )
    }
}

@Composable
fun WeatherScreenContent(
    modifier: Modifier = Modifier,
    uiState: WeatherContract.UiState,
    onEventDispatcher: (WeatherContract.Intent) -> Unit
) {
    val backgroundColor by animateColorAsState(
        uiState.backgroundColor
    )
    Column(
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                if (uiState.errorMessage != null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(textAlign = TextAlign.Center, text = "${uiState.errorMessage}\n")
                        Text(text = "Click icon for refresh")

                        Spacer(modifier = Modifier.height(16.dp))

                        RefreshButton {
                            onEventDispatcher(WeatherContract.Intent.Refresh)
                        }
                    }
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = uiState.cityName,
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        AsyncImage(
                            modifier = Modifier.size(125.dp),
                            model = uiState.icon,
                            contentDescription = "Weather Icon"
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${uiState.temp} °C",
                            color = Color.White,
                            fontSize = 50.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.description,
                            color = Color.White,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            WeatherDetail("Wind speed", "${uiState.wind} m/s")
                            WeatherDetail("Humidity", "${uiState.humidity} %")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        RefreshButton {
                            onEventDispatcher(WeatherContract.Intent.Refresh)
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun WeatherDetail(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp)
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun RefreshButton(onClick: () -> Unit) {
    Icon(
        imageVector = Icons.Default.Refresh,
        tint = MaterialTheme.colorScheme.primary,
        contentDescription = "Refresh",
        modifier = Modifier
            .size(100.dp)
            .clickable { onClick() },
    )
}