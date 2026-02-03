package com.hyms.feature.listing.data.repository

import com.hyms.core.database.dao.DraftListingDao
import com.hyms.core.database.dao.DraftMediaDao
import com.hyms.core.database.entity.DraftListingEntity
import com.hyms.core.database.entity.DraftMediaEntity
import com.hyms.feature.listing.domain.model.DraftListing
import com.hyms.feature.listing.domain.repository.DraftListingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DraftListingRepositoryImpl @Inject constructor(
    private val listingDao: DraftListingDao,
    private val mediaDao: DraftMediaDao
) : DraftListingRepository {

    override fun getLatestDraftListingForUser(userId: String?): Flow<DraftListing?> =
        listingDao.getLatestDraftListingForUser(userId).map { it?.toDomainModel() }

    override fun getDraftById(id: String): Flow<DraftListing?> =
        listingDao.getDraftById(id).map { it?.toDomainModel() }

    override suspend fun upsertDraftListing(draftListing: DraftListing) {
        listingDao.upsertDraftListing(draftListing.toEntity())
    }

    override suspend fun deleteDraftById(id: String) {
        listingDao.deleteDraftById(id)
    }

    override suspend fun clearAllDrafts() {
        listingDao.clearAllDrafts()
    }

    override fun observeDraftMedia(draftId: String): Flow<List<String>> =
        mediaDao.observeByDraft(draftId).map { list -> list.map { it.localUri } }

    override suspend fun replaceDraftMedia(draftId: String, uris: List<String>) {
        mediaDao.clearForDraft(draftId)
        val now = System.currentTimeMillis()
        val entities = uris.mapIndexed { idx, uri ->
            DraftMediaEntity(
                id = UUID.randomUUID().toString(),
                draftId = draftId,
                localUri = uri,
                orderIndex = idx,
                updatedAt = now
            )
        }
        mediaDao.upsertAll(entities)
    }
}

private fun DraftListingEntity.toDomainModel(): DraftListing =
    DraftListing(
        id = id,
        userId = userId,
        title = title,
        category = category,
        genderSegment = genderSegment,
        sizeLabel = sizeLabel,
        priceMinor = priceMinor,
        currency = currency,
        condition = condition,
        negotiable = negotiable,
        updatedAt = updatedAt
    )

private fun DraftListing.toEntity(): DraftListingEntity =
    DraftListingEntity(
        id = id,
        userId = userId,
        title = title,
        category = category,
        genderSegment = genderSegment,
        sizeLabel = sizeLabel,
        priceMinor = priceMinor,
        currency = currency,
        condition = condition,
        negotiable = negotiable,
        updatedAt = updatedAt
    )