package com.nxt64software.bacromana

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.webkit.JavascriptInterface
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebViewScreen(
    path: String,
    onPathChanged: (String) -> Unit,
    onNoNetwork: (String) -> Unit,
    onError: (String) -> Unit,
    errorLogContainer: MutableList<String>? = null,
    provideReload: ( () -> Unit ) -> Unit
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }

    // mapping (could be shared)
    val pathToScreen = mapOf(
        "mijloace-de-caracterizare-a-personajelor" to Screen.Sub2.route,
        "textul-argumentativ" to Screen.Sub1.route,
        "model-de-text-argumentativ" to Screen.Sub1.route,
        "curente-literare" to Screen.Sub2.route,
        "procedee-artistice-expresive" to Screen.Sub2.route,
        "genuri-literare" to Screen.Sub2.route,
        "expresivitatea-verbului" to Screen.Sub2.route,
        "valorile-stilistice-ale-timpurilor-verbale" to Screen.Sub2.route,
        "moduri-de-expunere" to Screen.Sub2.route,
        "baltagul" to Screen.Sub3.route,
        "enigma-otiliei" to Screen.Sub3.route,
        "ion" to Screen.Sub3.route,
        "ultima-noapte-de-dragoste" to Screen.Sub3.route,
        "aci-sosi-pe-vremuri" to Screen.Sub3.route,
        "morometii" to Screen.Sub3.route,
        "iona" to Screen.Sub3.route,
        "moara" to Screen.Sub3.route,
        "o-scrisoare-pierduta" to Screen.Sub3.route,
        "povestea-lui-harap-alb" to Screen.Sub3.route,
        "alexandru-lapusneanul" to Screen.Sub3.route,
        "patul-lui-procust" to Screen.Sub3.route,
        "zmeura-de-campie" to Screen.Sub3.route,
        "luceafarul" to Screen.Sub3.route,
        "lacustra" to Screen.Sub3.route,
        "leoaica" to Screen.Sub3.route,
        "plumb" to Screen.Sub3.route,
        "testament" to Screen.Sub3.route,
        "riga-crypto" to Screen.Sub3.route,
        "eu-nu-strivesc-corola" to Screen.Sub2.route,
        "floare-albastră" to Screen.Sub3.route,
        "flori-de-mucigai" to Screen.Sub3.route,
        "in-gradina-ghetsemani" to Screen.Sub3.route,
        "caracterizare-ghita" to Screen.Sub3.route,
        "caracterizare-harap-alb" to Screen.Sub3.route,
        "caracterizare-stefan-tipatescu" to Screen.Sub3.route,
        "caracterizare-vitoria-lipan" to Screen.Sub3.route,
        "caracterizare-costache-giurgiuveanu" to Screen.Sub3.route,
        "caracterizare-ilie-moromete" to Screen.Sub3.route,
        "caracterizare-ion" to Screen.Sub3.route,
        "caracterizare-iona" to Screen.Sub3.route
    )

    fun deriveRouteFromUrl(actualUrl: String): String {
        val fragment = actualUrl.substringAfter("#/", "")
        val segment = fragment.substringBefore('?').substringBefore('/')
        return pathToScreen[segment]
            ?: when {
                actualUrl.contains("/subiectul-1") -> Screen.Sub1.route
                actualUrl.contains("/subiectul-2") -> Screen.Sub2.route
                actualUrl.contains("/subiectul-3") -> Screen.Sub3.route
                actualUrl.contains("/quizuri") -> Screen.Quiz.route
                else -> Screen.Home.route
            }
    }

    key(path) {} // forțează recrearea când se schimbă path

    Box(modifier = Modifier.fillMaxSize()) {
        var webViewRef: WebView? = null

        AndroidView(factory = {
            WebView(it).apply {
                webViewRef = this
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    cacheMode = WebSettings.LOAD_DEFAULT
                    allowFileAccess = true
                    allowContentAccess = true
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        allowFileAccessFromFileURLs = true
                        allowUniversalAccessFromFileURLs = true
                    }
                }

                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        if (!hasInternet(context)) {
                            val failed = request?.url?.toString() ?: "unknown"
                            onNoNetwork(failed)
                            onError("A apărut o eroare la încărcarea paginii.")
                            return true
                        }
                        return false
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        isLoading = false
                        url?.let {
                            onPathChanged(deriveRouteFromUrl(it))
                        }
                        view?.evaluateJavascript(
                            """
                            (function(){
                                function notifyRoute(){ window.AndroidBridge.routeChanged(window.location.href); }
                                window.addEventListener('hashchange', notifyRoute);
                                notifyRoute();
                                document.addEventListener('click', function(){
                                    setTimeout(function(){
                                        fetch(window.location.origin + '/favicon.ico', {method:'HEAD', cache:'no-store', mode:'no-cors'})
                                            .catch(function(){ window.AndroidBridge.noNetworkDetected(window.location.href); });
                                    }, 100);
                                }, true);
                            })();
                            """.trimIndent(),
                            null
                        )
                    }

                    override fun onReceivedError(
                        view: WebView?,
                        errorCode: Int,
                        description: String?,
                        failingUrl: String?
                    ) {
                        super.onReceivedError(view, errorCode, description, failingUrl)
                        val msg = "Eroare la încărcare: ${description ?: "necunoscut"}"
                        errorLogContainer?.add(msg)
                        onError("A apărut o eroare la încărcarea paginii.")
                        if (!hasInternet(context)) {
                            failingUrl?.let { onNoNetwork(it) }
                        }
                    }

                    override fun onReceivedError(
                        view: WebView?,
                        request: WebResourceRequest?,
                        error: WebResourceError?
                    ) {
                        super.onReceivedError(view, request, error)
                        val urlStr = request?.url?.toString() ?: "unknown"
                        val desc = error?.description?.toString() ?: "necunoscut"
                        val msg = "Eroare modernă: $urlStr desc=$desc"
                        errorLogContainer?.add(msg)
                        onError("A apărut o eroare la încărcarea paginii.")
                        if (!hasInternet(context)) {
                            onNoNetwork(urlStr)
                        }
                    }
                }

                val base = "https://www.bacromana.ro/#"
                val urlToLoad = if (path == "/") "$base/" else "$base$path"
                loadUrl(urlToLoad)
            }
        }, modifier = Modifier.fillMaxSize())

        // expune reload
        LaunchedEffect(webViewRef) {
            webViewRef?.let { webview ->
                provideReload {
                    webview.reload()
                }
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

// helper pentru network check
private fun hasInternet(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val caps = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
    return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}
