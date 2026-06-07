package org.example.project.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.domain.model.Artwork
import org.example.project.domain.usecase.GetFavoriteArtworksUseCase
import org.example.project.domain.usecase.SearchArtworksUseCase
import org.example.project.domain.usecase.ToggleFavoriteArtworkUseCase

class MainViewModel(
    private val searchArtworksUseCase: SearchArtworksUseCase,
    private val getFavoriteArtworksUseCase: GetFavoriteArtworksUseCase,
    private val toggleFavoriteArtworkUseCase: ToggleFavoriteArtworkUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _favoriteArtworks = MutableStateFlow<List<Artwork>>(emptyList())
    val favoriteArtworks: StateFlow<List<Artwork>> = _favoriteArtworks.asStateFlow()

    init {
        viewModelScope.launch {
            getFavoriteArtworksUseCase().collect { favorites ->
                _favoriteArtworks.value = favorites
            }
        }
        search(_uiState.value.query)
    }

    fun onQueryChanged(newQuery: String) {
        _uiState.update { it.copy(query = newQuery) }
    }

    fun onTabSelected(tabIndex: Int) {
        _uiState.update { it.copy(selectedTab = tabIndex) }
    }

    fun selectArtwork(artwork: Artwork?) {
        _uiState.update { it.copy(selectedArtwork = artwork) }
    }

    fun search(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            searchArtworksUseCase(query)
                .onSuccess { results ->
                    _uiState.update { it.copy(searchResults = results, isLoading = false) }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            errorMessage = "検索に失敗しました。オフラインキャッシュを確認しています。",
                            searchResults = emptyList(),
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun toggleFavorite(objectId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            toggleFavoriteArtworkUseCase(objectId, isFavorite)
            _uiState.update { state ->
                if (state.selectedArtwork?.objectId == objectId) {
                    state.copy(selectedArtwork = state.selectedArtwork.copy(isFavorite = isFavorite))
                } else {
                    state
                }
            }
        }
    }
}
