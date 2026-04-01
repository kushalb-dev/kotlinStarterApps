package com.starterkotlin.stopwatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.starterkotlin.stopwatch.ui.theme.StopWatchTheme
import kotlinx.coroutines.delay
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StopWatchTheme {
                StopWatchApp()
            }
        }
    }
}

fun formatTime(
    millis: Long
): String {
    val seconds = (millis / 1000) % 60
    val minutes = (millis / (1000 * 60)) % 60
    val hours = (millis / (1000 * 60 * 60))
    return String.format(Locale.getDefault(), "%02d:%02d:%02d:%03d", hours, minutes, seconds, millis % 1000)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StopWatchApp(
    modifier: Modifier = Modifier
) {
    var timiMilSecs by rememberSaveable { mutableLongStateOf(0L) }
    var isRunning by rememberSaveable { mutableStateOf(false) }
    var wasRunning by rememberSaveable { mutableStateOf(false) }
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            when (event) {
                androidx.lifecycle.Lifecycle.Event.ON_PAUSE -> {
                    if (isRunning) {
                        wasRunning = true
                        isRunning = false
                    }
                }
                androidx.lifecycle.Lifecycle.Event.ON_RESUME -> {
                    if (wasRunning) {
                        isRunning = true
                        wasRunning = false
                    }
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    LaunchedEffect(isRunning) {
        if (isRunning) {
            val startTime = android.os.SystemClock.elapsedRealtime() - timiMilSecs
            while (isRunning) {
                timiMilSecs = android.os.SystemClock.elapsedRealtime() - startTime
                delay(10L)
            }
        }
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.stopwatch))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = formatTime(timiMilSecs),
                fontSize = 40.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(8.dp)
            )
            Button(
                onClick = {
                    isRunning = true
                },
                Modifier.padding(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.start)
                )
            }
            Button(
                onClick = {
                    isRunning = false
                },
                Modifier.padding(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.stop)
                )
            }
            Button(
                onClick = {
                    isRunning = false
                    timiMilSecs = 0L
                },
                Modifier.padding(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.reset)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StopwatchPreview() {
    StopWatchApp()
}