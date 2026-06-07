package org.example.project.domain.model

data class Artwork(
    val objectId: Int,
    val title: String,
    val artistDisplayName: String,
    val primaryImage: String,
    val primaryImageSmall: String,
    val department: String,
    val objectDate: String,
    val isFavorite: Boolean,
    val lastUpdated: String
)
