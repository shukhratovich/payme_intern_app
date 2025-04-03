package com.example.paymeinternapp.screens.stopwatcher

import android.view.SoundEffectConstants
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.hilt.getViewModel
import com.example.paymeinternapp.R

class StopwatchScreen(private val modifier: Modifier = Modifier) : Screen {

    @Composable
    override fun Content() {
        val viewModel: StopwatchContract.ViewModel = getViewModel<StopwatchViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val stopwatchState by viewModel.stopwatchState.collectAsState()

        StopwatchScreenContent(
            modifier = modifier,
            stopwatchState = stopwatchState,
            uiState = uiState,
            onEventDispatcher = viewModel::onEventDispatcher
        )
    }
}

@Composable
private fun StopwatchScreenContent(
    modifier: Modifier = Modifier,
    stopwatchState: String,
    uiState: StopwatchContract.UiState,
    onEventDispatcher: (StopwatchContract.Intent) -> Unit
) {
    val listState = rememberLazyListState()
    LaunchedEffect(uiState.lapsList) {
        listState.animateScrollToItem(listState.layoutInfo.totalItemsCount)
    }
    Scaffold { innerPadding ->
        Column(
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .animateContentSize()
                    .clip(RoundedCornerShape(50.dp))
                    .background(color = MaterialTheme.colorScheme.primary)
                    .padding(24.dp)

            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (uiState.currentTime != "00:00:00") {
                        Text(
                            text = uiState.currentTime,
                            fontSize = 22.sp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        onEventDispatcher(StopwatchContract.Intent.RefreshTime)
                    }
                    Text(
                        text = stopwatchState,
                        fontSize = 32.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            if (uiState.lapsList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .animateContentSize()
                            .align(Alignment.Center),
                        painter = painterResource(R.drawable.timer),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                        contentDescription = "nothing"
                    )
                }
            } else {
                LazyColumn(
                    Modifier
                        .animateContentSize()
                        .height(250.dp)
                        .fillMaxWidth(),
                    state = listState
                ) {
                    items(uiState.lapsList.size) { index ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        ) {
                            Text(
                                modifier = Modifier.align(Alignment.CenterStart),
                                text = "Lap ${index + 1}",
                                fontSize = 24.sp
                            )
                            Text(
                                modifier = Modifier.align(Alignment.CenterEnd),
                                text = uiState.lapsList[index],
                                fontSize = 24.sp
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MyCustomButton(
                    text = if (uiState.isRunning) {
                        "Stop"
                    } else {
                        "Start"
                    },
                    colorBox = if (uiState.isRunning) {
                        MaterialTheme.colorScheme.tertiary
                    } else {
                        MaterialTheme.colorScheme.tertiaryContainer
                    },
                    modifier = Modifier,
                    onClick = {
                        if (uiState.isRunning) {
                            onEventDispatcher(StopwatchContract.Intent.ClickedStop)
                        } else {
                            onEventDispatcher(StopwatchContract.Intent.ClickedStart)
                        }
                    },
                    colorText = MaterialTheme.colorScheme.onTertiaryContainer
                )
                MyCustomButton(
                    text = if (uiState.isRunning) {
                        "Lap"
                    } else {
                        "Restart"
                    },
                    onClick = {
                        if (uiState.isRunning) {
                            onEventDispatcher(StopwatchContract.Intent.ClickedLap)
                        } else {
                            onEventDispatcher(StopwatchContract.Intent.ClickedReset)
                        }
                    },
                    colorBox = if (uiState.isRunning) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.errorContainer
                    },
                    colorText = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }

}

@Preview
@Composable
private fun StopwatchScreenPreview(modifier: Modifier = Modifier) {
    StopwatchScreenContent(
        stopwatchState = "",
        uiState = StopwatchContract.UiState(),
        onEventDispatcher = { }
    )
}


@Composable
fun MyCustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colorBox: Color,
    colorText: Color
) {
    val view = LocalView.current
    Box(
        modifier = modifier
            .animateContentSize()
            .size(100.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(color = colorBox)
            .clickable {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                onClick()
            }
    ) {
        Text(
            text = text,
            color = colorText,
            modifier = Modifier.align(alignment = Alignment.Center)
        )
    }
}

