package org.example.project.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.WKScriptMessage
import platform.WebKit.WKScriptMessageHandlerProtocol
import platform.WebKit.WKUserContentController
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration
import platform.darwin.NSObject

class JSBridgeMessageHandler(
    private val onMessageReceived: (String) -> Unit
) : NSObject(), WKScriptMessageHandlerProtocol {
    override fun userContentController(
        userContentController: WKUserContentController,
        didReceiveScriptMessage: WKScriptMessage
    ) {
        val body = didReceiveScriptMessage.body as? String ?: return
        onMessageReceived(body)
    }
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun KmpWebView(
    url: String,
    onMessageReceived: (String) -> Unit,
    modifier: Modifier
) {
    val messageHandler = remember { JSBridgeMessageHandler(onMessageReceived) }
    
    UIKitView(
        factory = {
            val config = WKWebViewConfiguration().apply {
                userContentController.addScriptMessageHandler(messageHandler, name = "KmpBridge")
            }
            WKWebView(frame = CGRectZero.readValue(), configuration = config).apply {
                allowsBackForwardNavigationGestures = true
            }
        },
        update = { webView ->
            val nsUrl = NSURL.URLWithString(url)
            if (nsUrl != null && webView.URL?.absoluteString != url) {
                val request = NSURLRequest.requestWithURL(nsUrl)
                webView.loadRequest(request)
            }
        },
        modifier = modifier
    )
}
