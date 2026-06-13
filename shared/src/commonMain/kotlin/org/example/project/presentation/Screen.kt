package org.example.project.presentation

sealed interface Screen {
    data object CatalogList : Screen
    data object MuseumExplorer : Screen
    data object AnimationShowcase : Screen
    data object CanvasShowcase : Screen
    data object ComponentShowcase : Screen
    data object LayoutShowcase : Screen
    data object KmpAdvancedShowcase : Screen
}
