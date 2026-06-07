package org.example.project.domain.repository

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.model.Artwork

interface ArtworkRepository {
    suspend fun searchArtworks(query: String): Result<List<Artwork>>
    fun observeFavoriteArtworks(): Flow<List<Artwork>>
    suspend fun toggleFavorite(objectId: Int, isFavorite: Boolean)
}
