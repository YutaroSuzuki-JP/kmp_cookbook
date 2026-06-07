package org.example.project.domain.usecase

import org.example.project.domain.repository.ArtworkRepository

class ToggleFavoriteArtworkUseCase(private val repository: ArtworkRepository) {
    suspend operator fun invoke(objectId: Int, isFavorite: Boolean): Result<Unit> = runCatching {
        repository.toggleFavorite(objectId, isFavorite)
    }
}
