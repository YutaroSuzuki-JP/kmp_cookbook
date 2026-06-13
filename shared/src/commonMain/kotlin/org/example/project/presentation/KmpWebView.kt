package org.example.project.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun KmpWebView(
    url: String,
    onMessageReceived: (String) -> Unit,
    modifier: Modifier = Modifier
)
