package com.hyms.feature.listing.data.repository

import com.hyms.core.database.dao.DraftListingDao
import com.hyms.core.database.entity.DraftListingEntity
import com.hyms.feature.listing.domain.model.DraftListing
import com.hyms.feature.listing.domain.repository.DraftListingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DraftListingRepositoryImpl @Inject constructor(
    private val dao: DraftListingDao
) : DraftListingRepository {

    override fun getLatestDraftListingForUser(userId: String?): Flow<DraftListing?> {
        return dao.getLatestDraftListingForUser(userId).map { it?.toDomainModel() }
    }

    override fun getDraftById(id: String): Flow<DraftListing?> {
        return dao.getDraftById(id).map { it?.toDomainModel() }
    }

    override suspend fun upsertDraftListing(draftListing: DraftListing) {
        dao.upsertDraftListing(draftListing.toEntity())
    }

    override suspend fun deleteDraftById(id: String) {
        dao.deleteDraftById(id)
    }

    override suspend fun clearAllDrafts() {
        dao.clearAllDrafts()
    }
}

// Mappers (could be in a separate file, but kept here for proximity to entity/domain model)
private fun DraftListingEntity.toDomainModel(): DraftListing {
    return DraftListing(
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
}

private fun DraftListing.toEntity(): DraftListingEntity {
    return DraftListingEntity(
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
}
