package org.example.project.domain.usecase

import org.example.project.domain.model.Artwork
import org.example.project.domain.repository.ArtworkRepository

class SearchArtworksUseCase(private val repository: ArtworkRepository) {
    suspend operator fun invoke(query: String): Result<List<Artwork>> {
        if (query.isBlank()) return Result.success(emptyList())
        return repository.searchArtworks(query)
    }
}
