package org.example.project.infra.repository

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.example.project.infra.database.ArtworkLocalEntity
import org.example.project.infra.network.MetObjectResponse

class ArtworkMapperTest {

    @Test
    fun testToDomain() {
        val entity = ArtworkLocalEntity(
            objectId = 1,
            title = "Title",
            artistDisplayName = "Artist",
            primaryImage = "image_url",
            primaryImageSmall = "image_small_url",
            department = "Dept",
            objectDate = "1900",
            isFavorite = true,
            lastUpdated = "2026/06/08"
        )
        val domain = ArtworkMapper.toDomain(entity)
        assertEquals(1, domain.objectId)
        assertEquals("Title", domain.title)
        assertEquals("Artist", domain.artistDisplayName)
        assertEquals("image_url", domain.primaryImage)
        assertEquals("image_small_url", domain.primaryImageSmall)
        assertEquals("Dept", domain.department)
        assertEquals("1900", domain.objectDate)
        assertTrue(domain.isFavorite)
        assertEquals("2026/06/08", domain.lastUpdated)
    }

    @Test
    fun testToLocalEntity() {
        val detail = MetObjectResponse(
            objectID = 1,
            title = "Title",
            artistDisplayName = "Artist",
            primaryImage = "image_url",
            primaryImageSmall = "image_small_url",
            department = "Dept",
            objectDate = "1900"
        )
        val entity = ArtworkMapper.toLocalEntity(1, detail, isFavorite = false, lastUpdated = "2026/06/08")
        assertEquals(1, entity.objectId)
        assertEquals("Title", entity.title)
        assertEquals("Artist", entity.artistDisplayName)
        assertEquals("image_url", entity.primaryImage)
        assertEquals("image_small_url", entity.primaryImageSmall)
        assertEquals("Dept", entity.department)
        assertEquals("1900", entity.objectDate)
        assertFalse(entity.isFavorite)
        assertEquals("2026/06/08", entity.lastUpdated)
    }

    @Test
    fun testToLocalEntityWithNulls() {
        val detail = MetObjectResponse(objectID = 1)
        val entity = ArtworkMapper.toLocalEntity(1, detail, isFavorite = true, lastUpdated = "2026/06/08")
        assertEquals("Unknown", entity.title)
        assertEquals("Unknown", entity.artistDisplayName)
        assertEquals("", entity.primaryImage)
        assertEquals("", entity.primaryImageSmall)
        assertEquals("Unknown", entity.department)
        assertEquals("Unknown", entity.objectDate)
        assertTrue(entity.isFavorite)
    }
}
