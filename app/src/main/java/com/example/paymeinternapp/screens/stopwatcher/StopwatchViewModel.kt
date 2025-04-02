package com.example.paymeinternapp.screens.stopwatcher

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecases.stopwatch.GetSavedTimeUseCase
import com.example.domain.usecases.stopwatch.SaveTimeUseCase
import com.example.paymeinternapp.timer.TimerClock
import com.example.paymeinternapp.utils.DateFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StopwatchViewModel @Inject constructor(
    private val saveTimeUseCase: SaveTimeUseCase,
    private val getSavedTimeUseCase: GetSavedTimeUseCase
) : ViewModel(), StopwatchContract.ViewModel {
    override val uiState = MutableStateFlow(StopwatchContract.UiState())
    override val stopwatchState = TimerClock.stopwatchState

    init {
        TimerClock.runClock()
        viewModelScope.launch {
            getSavedTimeUseCase.invoke().collectLatest { elapsedTime ->
                TimerClock.setElapsedTime(elapsedTime)
                stopwatchState.value = DateFormatter.formatTimer(elapsedTime)
                Log.d("TTT", "stopwatch: ${stopwatchState.value}")
                Log.d("TTT", "elapsedTime: $elapsedTime")
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
        {
            super.onCleared()
            TimerClock.onDestroy()
            viewModelScope.launch {
                Log.d("TTT", "onCleared: ")
                saveTimeUseCase.invoke(TimerClock.getElapsedTime())
            }
            Log.d("TTT", "onCleared: ")
        }
    }
}