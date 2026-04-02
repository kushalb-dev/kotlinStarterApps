package com.starterkotlin.catchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.DensityMedium
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.starterkotlin.catchat.ui.theme.CatChatTheme
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {

    /**
     * Serializable used by the navController
     * to navigate between the screens
     */
    @Serializable object Home
    @Serializable object Settings
    @Serializable object About

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CatChatTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val mailListViewModel: MailListViewModel = viewModel()


                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        /**
                         * Drawer Content for items inside
                         * the navigationDrawer
                         */
                        ModalDrawerSheet {
                            Text(
                                "App Menu",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.titleLarge,
                                )
                            HorizontalDivider()
                            NavigationDrawerItem (
                                icon = {
                                    Icon(Icons.Default.Home, contentDescription = getString(R.string.home))
                                },
                                label = { Text(getString(R.string.home)) },
                                selected = false,
                                onClick = {
                                    navController.navigate(Home)
                                    scope.launch { drawerState.close() }
                                }
                            )
                            NavigationDrawerItem (
                                icon = {
                                    Icon(Icons.Default.Info, contentDescription = getString(R.string.about))
                                },
                                label = { Text(getString(R.string.about)) },
                                selected = false,
                                onClick = {
                                    navController.navigate(About)
                                    scope.launch { drawerState.close() }
                                }
                            )
                            NavigationDrawerItem (
                                icon = {
                                    Icon(Icons.Default.Settings, contentDescription = getString(R.string.settings))
                                },
                                label = { Text(getString(R.string.settings)) },
                                selected = false,
                                onClick = {
                                    navController.navigate(Settings)
                                    scope.launch { drawerState.close() }
                                }
                            )
                        }
                    }
                ) {
                    /**
                     * Scaffold containing the
                     * TopBar & BottomBar
                     */
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = { TopAppBar(
                            title = { Text(getString(R.string.app_name))},
                            colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    titleContentColor = MaterialTheme.colorScheme.primary
                            ),
                            navigationIcon = {
                                IconButton(
                                    onClick = { scope.launch { drawerState.open() } }
                                ) {
                                    Icon(Icons.Default.DensityMedium, contentDescription = stringResource(R.string.settings))
                                }
                            },
                            actions = {
                                IconButton(
                                    onClick = { navController.navigate(Settings) }
                                ) {
                                    Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.settings))
                                }
                            }
                        )},
                        bottomBar = {
                            NavigationBar {
                                val isHomeSelected = currentDestination?.hasRoute<Home>() == true
                                NavigationBarItem(
                                    selected = isHomeSelected,
                                    icon = {
                                        Icon(if (isHomeSelected) Icons.Default.AccessTimeFilled else  Icons.Default
                                            .AccessTime,
                                            contentDescription =
                                            stringResource(R.string
                                            .home))
                                    },
                                    label = { Text(stringResource(R.string.home))},
                                    onClick = { navController.navigate(Home) }
                                )
                                val isAboutSelected = currentDestination?.hasRoute<About>() == true
                                NavigationBarItem(
                                    selected = isAboutSelected,
                                    icon = {
                                        Icon(if (isAboutSelected) Icons.Default.PlayCircleFilled else Icons.Default
                                            .PlayCircle,
                                            contentDescription =
                                            stringResource(R
                                            .string.home))
                                    },
                                    label = { Text(stringResource(R.string.about))},
                                    onClick = { navController.navigate(About) }
                                )
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = Home,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable<Home> {
                                HomeScreen(
                                    mailList = mailListViewModel.mailList,
                                    onToggle = { mail ->
                                        mailListViewModel.toggleReadStatus(mail)
                                    }
                                )
                            }
                            composable<About> { Text("About Screen") }
                            composable<Settings> { Text("Settings Screen") }
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun HomeScreen(
    mailList: List<Mail>,
    onToggle: (Mail) -> Unit,
    modifier: Modifier = Modifier
) {
    RecyclerView(
        mailList,
        onToggle,
        modifier = modifier,
    )
}

/**
 * Recycler View to display
 * @param mailList in [HomeScreen]
 */
@Composable
fun RecyclerView(
    mailList: List<Mail>,
    onToggle: (Mail) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn (
        modifier = modifier
    ) {
        items (mailList) { item ->
            MailCard(
                item,
                onToggleRead = onToggle,
                modifier = Modifier
                    .padding(3.dp)
            )
        }
    }
}

/**
 * Card that displays [Mail],
 * also hoists the [Checkbox]'s onCheckedChange
 * function into the [onToggleRead] function
 */
@Composable
fun MailCard(
    mail: Mail,
    onToggleRead: (Mail) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (mail.isRead) MaterialTheme.colorScheme.surfaceContainerLow
                            else MaterialTheme.colorScheme.secondaryContainer
        ),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = mail.text,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(start = 4.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = mail.date,
                    fontSize = 10.sp,
                    color = Color.Gray,
                )
                Checkbox(
                    checked = mail.isRead,
                    onCheckedChange = { onToggleRead(mail) },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MailCardPreview() {
    MailCard(Mail("123", "1/1/2001", false), {})
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        mailList = listOf(
            Mail("123", "1/1/2001", false),
            Mail("123", "1/1/2001", false),
        ),
        onToggle = {},
    )
}