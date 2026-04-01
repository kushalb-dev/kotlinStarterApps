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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.starterkotlin.stopwatch.ui.theme.StopWatchTheme
import androidx.lifecycle.viewmodel.compose.viewModel

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StopWatchApp(
    modifier: Modifier = Modifier,
    viewModel: StopWatchViewModel = viewModel()
) {
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            when (event) {
                androidx.lifecycle.Lifecycle.Event.ON_PAUSE -> {
                    viewModel.pauseTimer(true)
                }
                androidx.lifecycle.Lifecycle.Event.ON_RESUME -> {
                    viewModel.resumeTimer()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
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
                text = viewModel.getLatestTime(),
                fontSize = 40.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(8.dp)
            )
            Button(
                onClick = {
                    viewModel.startTimer()
                },
                Modifier.padding(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.start)
                )
            }
            Button(
                onClick = {
                    viewModel.pauseTimer()
                },
                Modifier.padding(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.stop)
                )
            }
            Button(
                onClick = {
                    viewModel.resetTimer()
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