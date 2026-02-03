package com.hyms.feature.listing.domain.repository

import com.hyms.feature.listing.domain.model.DraftListing
import kotlinx.coroutines.flow.Flow

interface DraftListingRepository {
    fun getLatestDraftListingForUser(userId: String?): Flow<DraftListing?>
    fun getDraftById(id: String): Flow<DraftListing?>
    suspend fun upsertDraftListing(draftListing: DraftListing)
    suspend fun deleteDraftById(id: String)
    suspend fun clearAllDrafts()
    fun observeDraftMedia(draftId: String): kotlinx.coroutines.flow.Flow<List<String>>
    suspend fun replaceDraftMedia(draftId: String, uris: List<String>)
}