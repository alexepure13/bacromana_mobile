package com.nxt64software.bacromana

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.*
import com.nxt64software.bacromana.ui.theme.BacRomanaTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var rateAppManager: RateAppManager

    private fun hasInternet(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val caps = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    @Composable
    private fun ErrorBanner(message: String?) {
        if (message.isNullOrBlank()) return
        val dark = isSystemInDarkTheme()
        val background = if (dark) MaterialTheme.colorScheme.primaryContainer else Color.White
        val contentColor = if (dark) MaterialTheme.colorScheme.onPrimaryContainer else Color.Black
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = background,
            tonalElevation = 0.dp
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = contentColor,
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            )
        }
    }

    @Composable
    private fun NoInternetDialog(
        hasInternetCheck: () -> Boolean,
        onRetry: () -> Unit,
        onDismiss: () -> Unit
    ) {
        val isDark = isSystemInDarkTheme()
        AlertDialog(
            onDismissRequest = { /* block auto-dismiss */ },
            title = { Text("Fără internet") },
            text = { Text("Nu există conexiune la internet. Verifică conexiunea sau reîncearcă.") },
            confirmButton = {
                ElevatedButton(
                    onClick = {
                        if (hasInternetCheck()) {
                            onRetry()
                        }
                    },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = MaterialTheme.shapes.medium,
                    elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 6.dp)
                ) {
                    Text(stringResource(id = R.string.retry))
                }
            },
            dismissButton = {
                ElevatedButton(
                    onClick = { onDismiss() },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = if (isDark) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = MaterialTheme.shapes.medium,
                    elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 4.dp)
                ) {
                    Text("OK")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 16.dp,
            shape = MaterialTheme.shapes.large
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize rating manager and increment launch count
        rateAppManager = RateAppManager(this)
        rateAppManager.incrementLaunchCount()

        setContent {
            BacRomanaTheme {
                val context = this@MainActivity
                val navController = rememberNavController()
                val coroutineScope = rememberCoroutineScope()

                var selectedRoute by rememberSaveable { mutableStateOf(Screen.Home.route) }
                val snackbarHostState = remember { SnackbarHostState() }
                val errorLogs = remember { mutableStateListOf<String>() }

                var showNoNetworkDialog by remember { mutableStateOf(false) }
                var currentError by remember { mutableStateOf<String?>(null) }
                var showRateAppDialog by remember { mutableStateOf(false) }

                // callback to trigger reload from app bar
                var reloadWebView: () -> Unit by remember { mutableStateOf({}) }

                // initial connectivity check
                LaunchedEffect(Unit) {
                    if (!hasInternet(context)) {
                        currentError = "A apărut o eroare la încărcarea paginii."
                        showNoNetworkDialog = true
                    }
                }

                // check if rating dialog should be shown
                LaunchedEffect(Unit) {
                    if (rateAppManager.shouldShowRatingDialog()) {
                        showRateAppDialog = true
                    }
                }

                fun navigateSafely(route: String) {
                    if (!hasInternet(context)) {
                        currentError = "A apărut o eroare la încărcarea paginii."
                        showNoNetworkDialog = true
                        return
                    }
                    if (route != selectedRoute) {
                        selectedRoute = route
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        currentError = null
                    }
                }

                fun forceReloadCurrent() {
                    reloadWebView()
                }

                Scaffold(
                    topBar = {
                        Column {
                            TopAppBarWithMenu(
                                navigateTo = { route -> navigateSafely(route) },
                                onReload = { forceReloadCurrent() }
                            )
                            ErrorBanner(message = currentError)
                        }
                    },
                    bottomBar = {
                        BottomNavigationBar(
                            currentRoute = selectedRoute,
                            onSelect = { screen -> navigateSafely(screen.route) }
                        )
                    },
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        if (showNoNetworkDialog) {
                            NoInternetDialog(
                                hasInternetCheck = { hasInternet(context) },
                                onRetry = {
                                    if (hasInternet(context)) {
                                        showNoNetworkDialog = false
                                        currentError = null
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Conexiune restabilită. Reîncarc pagina...")
                                        }
                                        reloadWebView()
                                    } else {
                                        currentError = "A apărut o eroare la încărcarea paginii."
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar("Încă nu ai internet.")
                                        }
                                    }
                                },
                                onDismiss = {
                                    showNoNetworkDialog = false
                                }
                            )
                        }

                        if (showRateAppDialog) {
                            RateAppDialog(
                                onDismiss = { showRateAppDialog = false },
                                onRateNow = { rating ->
                                    showRateAppDialog = false
                                    rateAppManager.markAsRated()
                                    openPlayStoreForRating(context)
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Mulțumim pentru evaluarea cu $rating stele!")
                                    }
                                },
                                onRateLater = {
                                    showRateAppDialog = false
                                    // Will show again after more launches
                                },
                                onNeverShowAgain = {
                                    showRateAppDialog = false
                                    rateAppManager.neverShowAgain()
                                }
                            )
                        }

                        NavHost(
                            navController = navController,
                            startDestination = Screen.Home.route,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            listOf(
                                Screen.Home to "/",
                                Screen.Sub1 to "/subiectul-1",
                                Screen.Sub2 to "/subiectul-2",
                                Screen.Sub3 to "/subiectul-3",
                                Screen.Quiz to "/quizuri"
                            ).forEach { (screen, path) ->
                                composable(screen.route) {
                                    WebViewScreen(
                                        path = path,
                                        onPathChanged = { mapped ->
                                            if (mapped != selectedRoute) selectedRoute = mapped
                                            if (hasInternet(context)) {
                                                currentError = null
                                            }
                                        },
                                        onNoNetwork = { failedUrl ->
                                            currentError = "A apărut o eroare la încărcarea paginii."
                                            showNoNetworkDialog = true
                                            errorLogs.add("No network / navigation failed: $failedUrl")
                                        },
                                        onError = { msg ->
                                            currentError = "A apărut o eroare la încărcarea paginii."
                                            errorLogs.add(msg)
                                        },
                                        errorLogContainer = errorLogs,
                                        provideReload = { reloadLambda ->
                                            reloadWebView = reloadLambda
                                        }
                                    )
                                }
                            }
                            composable(Screen.About.route) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(MaterialTheme.colorScheme.surface) // același background ca și conținutul
                                        .padding(horizontal = 16.dp)
                                ) {
                                    StaticPage(
                                        title = stringResource(id = R.string.despre_titlu),
                                        body = stringResource(id = R.string.despre_text)
                                    )
                                }
                            }

                            composable(Screen.Privacy.route) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(MaterialTheme.colorScheme.surface)
                                        .padding(horizontal = 16.dp)
                                ) {
                                    StaticPage(
                                        title = stringResource(id = R.string.politica_titlu),
                                        body = stringResource(id = R.string.politica_text)
                                    )
                                }
                            }
                            composable(Screen.Terms.route) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(MaterialTheme.colorScheme.surface)
                                        .padding(horizontal = 16.dp)
                                ) {
                                    StaticPage(
                                        title = stringResource(id = R.string.termeni_titlu),
                                        body = stringResource(id = R.string.termeni_text)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
