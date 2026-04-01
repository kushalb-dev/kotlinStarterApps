package com.starterkotlin.stopwatch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class StopWatchViewModel : ViewModel() {
    var timeMilSecs by mutableLongStateOf(0L)
        private set
    var isRunning by mutableStateOf(false)
        private set
    private var wasRunning = false
    private var timerJob: Job? = null

    fun getLatestTime(): String {
        return formatTime(timeMilSecs)
    }

    fun formatTime(
        millis: Long
    ): String {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / (1000 * 60)) % 60
        val hours = (millis / (1000 * 60 * 60))
        return String.format(Locale.getDefault(), "%02d:%02d:%02d:%03d", hours, minutes, seconds, millis % 1000)
    }

    fun startTimer() {
        if (isRunning) return
        isRunning = true
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            val startTime = android.os.SystemClock.elapsedRealtime() - timeMilSecs
            while (isRunning) {
                timeMilSecs = android.os.SystemClock.elapsedRealtime() - startTime
                delay(10L)
            }
        }
    }

    fun pauseTimer(isSystemAction: Boolean = false) {
        if (isSystemAction && isRunning) {
            wasRunning = true
        }
        isRunning = false
        timerJob?.cancel()
    }

    fun resumeTimer() {
        if (wasRunning) {
            startTimer()
            wasRunning = false
        }
    }

    fun resetTimer() {
        pauseTimer()
        wasRunning = false
        timeMilSecs = 0L
    }
}