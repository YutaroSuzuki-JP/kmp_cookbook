package org.example.project.presentation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import org.example.project.domain.model.Artwork
import org.example.project.domain.usecase.GetFavoriteArtworksUseCase
import org.example.project.domain.usecase.SearchArtworksUseCase
import org.example.project.domain.usecase.ToggleFavoriteArtworkUseCase
import org.example.project.domain.repository.ArtworkRepository
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private class FakeArtworkRepository : ArtworkRepository {
        var searchResult: Result<List<Artwork>> = Result.success(emptyList())
        val favoritesFlow = MutableStateFlow<List<Artwork>>(emptyList())
        var toggleCallCount = 0
        var lastToggleObjectId = 0
        var lastToggleIsFavorite = false

        override suspend fun searchArtworks(query: String): Result<List<Artwork>> {
            return searchResult
        }

        override fun observeFavoriteArtworks(): Flow<List<Artwork>> {
            return favoritesFlow
        }

        override suspend fun toggleFavorite(objectId: Int, isFavorite: Boolean) {
            toggleCallCount++
            lastToggleObjectId = objectId
            lastToggleIsFavorite = isFavorite
        }
    }

    @Test
    fun testInitialSearchAndFavoritesObservation() = runBlocking {
        val repo = FakeArtworkRepository()
        val searchArtworksUseCase = SearchArtworksUseCase(repo)
        val getFavoriteArtworksUseCase = GetFavoriteArtworksUseCase(repo)
        val toggleFavoriteArtworkUseCase = ToggleFavoriteArtworkUseCase(repo)

        val initialArtworks = listOf(
            Artwork(1, "Sunflowers", "Van Gogh", "", "", "Dept", "1888", false, "2026/06/08")
        )
        repo.searchResult = Result.success(initialArtworks)

        val favArtworks = listOf(
            Artwork(2, "Monet Painting", "Claude Monet", "", "", "Dept", "1890", true, "2026/06/08")
        )
        repo.favoritesFlow.value = favArtworks

        val viewModel = MainViewModel(
            searchArtworksUseCase,
            getFavoriteArtworksUseCase,
            toggleFavoriteArtworkUseCase
        )

        assertEquals(initialArtworks, viewModel.uiState.value.searchResults)
        assertEquals(favArtworks, viewModel.favoriteArtworks.value)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun testSearchSuccess() = runBlocking {
        val repo = FakeArtworkRepository()
        val viewModel = MainViewModel(
            SearchArtworksUseCase(repo),
            GetFavoriteArtworksUseCase(repo),
            ToggleFavoriteArtworkUseCase(repo)
        )

        val resultArtworks = listOf(
            Artwork(10, "Title", "Artist", "", "", "Dept", "1900", false, "2026/06/08")
        )
        repo.searchResult = Result.success(resultArtworks)

        viewModel.search("picasso")
        assertEquals(resultArtworks, viewModel.uiState.value.searchResults)
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(null, viewModel.uiState.value.errorMessage)
    }

    @Test
    fun testSearchFailure() = runBlocking {
        val repo = FakeArtworkRepository()
        val viewModel = MainViewModel(
            SearchArtworksUseCase(repo),
            GetFavoriteArtworksUseCase(repo),
            ToggleFavoriteArtworkUseCase(repo)
        )

        repo.searchResult = Result.failure(RuntimeException("Network error"))

        viewModel.search("error")
        assertTrue(viewModel.uiState.value.searchResults.isEmpty())
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals("検索に失敗しました。オフラインキャッシュを確認しています。", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun testToggleFavoriteAndQueryChanges() = runBlocking {
        val repo = FakeArtworkRepository()
        val viewModel = MainViewModel(
            SearchArtworksUseCase(repo),
            GetFavoriteArtworksUseCase(repo),
            ToggleFavoriteArtworkUseCase(repo)
        )

        viewModel.onQueryChanged("new_query")
        assertEquals("new_query", viewModel.uiState.value.query)

        viewModel.onTabSelected(1)
        assertEquals(1, viewModel.uiState.value.selectedTab)

        val selected = Artwork(15, "Fav", "Artist", "", "", "", "", false, "")
        viewModel.selectArtwork(selected)
        assertEquals(selected, viewModel.uiState.value.selectedArtwork)

        viewModel.toggleFavorite(15, true)
        assertEquals(1, repo.toggleCallCount)
        assertEquals(15, repo.lastToggleObjectId)
        assertTrue(repo.lastToggleIsFavorite)
        assertTrue(viewModel.uiState.value.selectedArtwork?.isFavorite == true)
    }
}
