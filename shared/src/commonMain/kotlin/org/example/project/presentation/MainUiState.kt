package org.example.project.presentation

import org.example.project.domain.model.Artwork

data class MainUiState(
    val query: String = "sunflowers",
    val searchResults: List<Artwork> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedArtwork: Artwork? = null,
    val selectedTab: Int = 0 // 0 = Search Gallery, 1 = Favorites
)
