package com.starterkotlin.guessinggame

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.starterkotlin.guessinggame.ui.theme.GuessingGameTheme
import kotlinx.serialization.Serializable

@Serializable
object GameScreen
@Serializable
data class ResultScreen(val won: Boolean, val word: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GuessingGameTheme {
                GuessingGameApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuessingGameApp(
    modifier: Modifier = Modifier,
    gameViewModel: GameViewModel = viewModel(),
) {
    val navController = rememberNavController()
    LaunchedEffect(gameViewModel.gameResult) {
        gameViewModel.gameResult?.let {
            navController.navigate(ResultScreen(it == GameResult.Won, gameViewModel.currentCorrectWord))
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
            )
        },
        modifier = modifier
            .fillMaxSize()
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = GameScreen,
        ) {
            composable<GameScreen> {
                GameScreen(
                    modifier = Modifier
                        .padding(innerPadding),
                    gameViewModel = gameViewModel
                )
            }
            composable<ResultScreen> { backStackEntry ->
                val args = backStackEntry.toRoute<ResultScreen>()
                ResultScreen(
                    won = args.won,
                    correctWord = args.word,
                    onPlayAgain = {
                        gameViewModel.resetGame()
                        navController.navigate(GameScreen) {
                            popUpTo(GameScreen) { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun ResultScreen(
    won: Boolean,
    correctWord: String,
    onPlayAgain: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (won) "You Won! 🎉" else "Game Over 💀",
            style = MaterialTheme.typography.displayMedium
        )
        Text(
            text = "The word was: $correctWord",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = onPlayAgain) {
            Text("Play Again")
        }
    }
}

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    gameViewModel: GameViewModel = viewModel(),
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = gameViewModel.currentWord,
            style = MaterialTheme.typography.displayLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 10.dp)
        )
        Spacer(
            modifier = Modifier
                .height(10.dp)
        )
        Text(
            text = "You have ${gameViewModel.remainingGuesses} guesses left.",
            fontSize = 15.sp,
            textAlign = TextAlign.Start,
        )
        Text(
            text = "Incorrect Guesses: ${gameViewModel.incorrectGuesses}",
            fontSize = 15.sp,
            textAlign = TextAlign.Start,
        )
        Spacer(
            modifier = Modifier
                .height(10.dp)
        )
        TextField(
            value = gameViewModel.guessInput,
            onValueChange = { gameViewModel.onGuessInputChange(it) },
            label = { Text(
                stringResource(R.string.guess_a_letter),
                fontSize = 9.sp,
            ) },
            modifier = Modifier
                .padding(top = 8.dp)
        )
        Spacer(
            modifier = Modifier
                .height(10.dp)
        )
        Button(
            onClick = {
                focusManager.clearFocus()
                if (gameViewModel.guessInput.isNotBlank() && gameViewModel.guessInput.length == 1) {
                    gameViewModel.guessLetter(gameViewModel.guessInput.uppercase())
                } else {
                    Toast.makeText(
                        context,
                        "Please enter a single letter.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            Text(stringResource(R.string.guess))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GuessingGameAppPreview() {
    GuessingGameTheme {
        GuessingGameApp()
    }
}