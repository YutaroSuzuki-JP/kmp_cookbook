package org.example.project.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(viewModel: MainViewModel = koinViewModel()) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.CatalogList) }

    AnimatedContent(
        targetState = currentScreen,
        transitionSpec = {
            if (targetState == Screen.CatalogList) {
                // Moving back to the list: slide left-to-right (slide in from left, slide out to right)
                (slideInHorizontally(animationSpec = tween(350)) { -it } + fadeIn(animationSpec = tween(350)))
                    .togetherWith(slideOutHorizontally(animationSpec = tween(350)) { it } + fadeOut(animationSpec = tween(350)))
            } else {
                // Moving into a showcase: slide right-to-left (slide in from right, slide out to left)
                (slideInHorizontally(animationSpec = tween(350)) { it } + fadeIn(animationSpec = tween(350)))
                    .togetherWith(slideOutHorizontally(animationSpec = tween(350)) { -it } + fadeOut(animationSpec = tween(350)))
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { screen ->
        when (screen) {
            Screen.CatalogList -> CatalogListScreen(
                onNavigate = { currentScreen = it }
            )
            Screen.MuseumExplorer -> MuseumExplorerScreen(
                viewModel = viewModel,
                onBack = { currentScreen = Screen.CatalogList }
            )
            Screen.AnimationShowcase -> AnimationShowcaseScreen(
                onBack = { currentScreen = Screen.CatalogList }
            )
            Screen.CanvasShowcase -> CanvasShowcaseScreen(
                onBack = { currentScreen = Screen.CatalogList }
            )
            Screen.ComponentShowcase -> ComponentShowcaseScreen(
                onBack = { currentScreen = Screen.CatalogList }
            )
            Screen.LayoutShowcase -> LayoutShowcaseScreen(
                onBack = { currentScreen = Screen.CatalogList }
            )
        }
    }
}
