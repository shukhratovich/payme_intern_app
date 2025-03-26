package com.example.presenters.stopwatch

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.usecase.datastore.GetSavedTimeUseCase
import com.example.usecase.datastore.SaveTimerUseCase
import com.example.usecase.timer.TimerClock
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class StopwatchViewModel @Inject constructor(
    private val saveTimerUseCase: SaveTimerUseCase,
    private val getSavedTimeUseCase: GetSavedTimeUseCase
) : ViewModel(),
    StopwatchContract.ViewModel {
    override val uiState = MutableStateFlow(StopwatchContract.UiState())
    override val stopwatchState = TimerClock.stopwatchState

    init {
        TimerClock.runClock()
        viewModelScope.launch {
            getSavedTimeUseCase.invoke().collectLatest { elapsedTime ->
                TimerClock.setElapsedTime(elapsedTime)
                stopwatchState.value =
                    SimpleDateFormat("mm:ss:SS", Locale.getDefault()).format(Date(elapsedTime))
            }
        }
        TimerClock.onCurrentTimeListener = { currentTime ->
            reduce { it.copy(currentTime = currentTime) }
        }

//        timer.onElapsedTimeListener = { time ->
//                reduce { it.copy(currentStopWatch = DateFormatter.formatTimer(time)) }
//            stopwatchState.value = DateFormatter.formatTimer(time)
//        }
    }

    private inline fun reduce(block: (StopwatchContract.UiState) -> StopwatchContract.UiState) {
        val old = uiState.value
        val new = block(old)
        uiState.update { new }
    }

    override fun onEventDispatcher(intent: StopwatchContract.Intent) {
        when (intent) {
            is StopwatchContract.Intent.ClickedStart -> {
                reduce { it.copy(isRunning = true) }
                TimerClock.startTimer()
            }

            is StopwatchContract.Intent.ClickedStop -> {
                reduce { it.copy(isRunning = false) }
                TimerClock.pauseTimer()
            }

            is StopwatchContract.Intent.ClickedReset -> {
                reduce { it.copy(isRunning = false, lapsList = emptyList()) }
                TimerClock.resetTimer()
            }

            is StopwatchContract.Intent.ClickedLap -> {
                val newList: MutableList<String> = mutableListOf()
                newList.addAll(uiState.value.lapsList)
                newList.add(stopwatchState.value)
                reduce { it.copy(lapsList = newList) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        runBlocking {
            saveTimerUseCase.invoke(TimerClock.getElapsedTime())
        }
        TimerClock.onDestroy()
    }
}