package org.example.project.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.model.Artwork
import org.example.project.domain.repository.ArtworkRepository

class GetFavoriteArtworksUseCase(private val repository: ArtworkRepository) {
    operator fun invoke(): Flow<List<Artwork>> {
        return repository.observeFavoriteArtworks()
    }
}
