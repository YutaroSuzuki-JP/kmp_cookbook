package org.example.project.ads

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun AdBanner(
    adUnitId: String,
    modifier: Modifier = Modifier
)
