package org.example.project.presentation

import android.annotation.SuppressLint
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun KmpWebView(
    url: String,
    onMessageReceived: (String) -> Unit,
    modifier: Modifier
) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
                addJavascriptInterface(object {
                    @JavascriptInterface
                    fun postMessage(message: String) {
                        onMessageReceived(message)
                    }
                }, "KmpBridge")
            }
        },
        update = { webView ->
            if (webView.url != url) {
                webView.loadUrl(url)
            }
        },
        modifier = modifier
    )
}
