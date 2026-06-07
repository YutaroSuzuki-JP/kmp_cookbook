package org.example.project.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.example.project.domain.model.Artwork
import org.example.project.domain.repository.ArtworkRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ToggleFavoriteArtworkUseCaseTest {

    private class FakeArtworkRepository : ArtworkRepository {
        var toggleCallCount = 0
        var lastToggleObjectId = 0
        var lastToggleIsFavorite = false

        override suspend fun searchArtworks(query: String): Result<List<Artwork>> = Result.success(emptyList())

        override fun observeFavoriteArtworks(): Flow<List<Artwork>> = flowOf(emptyList())

        override suspend fun toggleFavorite(objectId: Int, isFavorite: Boolean) {
            toggleCallCount++
            lastToggleObjectId = objectId
            lastToggleIsFavorite = isFavorite
        }
    }

    @Test
    fun testToggleFavoriteArtworkUseCase() = runBlocking {
        val repo = FakeArtworkRepository()
        val useCase = ToggleFavoriteArtworkUseCase(repo)

        useCase(456, true)
        assertEquals(1, repo.toggleCallCount)
        assertEquals(456, repo.lastToggleObjectId)
        assertTrue(repo.lastToggleIsFavorite)
    }
}
