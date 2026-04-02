package com.starterkotlin.secretmessage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.starterkotlin.secretmessage.ui.theme.SecretMessageTheme
import kotlin.io.encoding.Base64

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SecretMessageTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text(stringResource(R.string.app_name)) },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                            )
                        )
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Welcome.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Welcome.route) {
                            WelcomeScreen(
                                onStart = {
                                    navController.navigate(Screen.EnterMessage.route)
                                }
                            )
                        }

                        composable(Screen.EnterMessage.route) {
                            EnterScreen( onEncrypt = { typedText ->
                                val encrypted = Base64.encode(typedText.toByteArray())
                                navController.navigate(Screen.ShowEncrypted.createRoute(encrypted)) {
                                    popUpTo(Screen.Welcome.route) { inclusive = false }
                                }
                            })
                        }


                        composable(
                            route = Screen.ShowEncrypted.route,
                            arguments = listOf(navArgument("msg") { type = NavType.StringType} )
                        ) { backStackEntry ->
                            val msg = backStackEntry.arguments?.getString("msg") ?: ""
                            ResultScreen(encryptedMessage = msg)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WelcomeScreen(
    onStart : () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.welcome_text),
            textAlign = TextAlign.Center,
            modifier = Modifier
        )
        Spacer (modifier = Modifier.height(10.dp))
        Button(
            onClick = onStart,
            modifier = Modifier
        ) {
            Text(
                text = stringResource(R.string.start),
                textAlign = TextAlign.Center,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun EnterScreen(
    onEncrypt: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var message by remember { mutableStateOf("") }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        TextField(
            value = message,
            onValueChange = { message = it },
            label = { Text(stringResource(R.string.enter_your_secret_message)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = { onEncrypt(message) },
            enabled = message.isNotBlank()
        ) {
            Text(stringResource(R.string.encrypt_view))
        }
    }
}

@Composable
fun ResultScreen(
    encryptedMessage: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.your_encrypted_message),
            style = MaterialTheme.typography.labelLarge
        )
        Card(
            modifier = modifier
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Text(
                encryptedMessage,
                textAlign = TextAlign.Center,
                modifier = modifier
                    .padding(24.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    SecretMessageTheme {
        WelcomeScreen({})
    }
}

@Preview(showBackground = true)
@Composable
fun EnterScreenPreview() {
    SecretMessageTheme {
        EnterScreen({})
    }
}

@Preview(showBackground = true)
@Composable
fun ResultScreenPreview() {
    SecretMessageTheme {
        ResultScreen("Preview")
    }
}