package com.hyms.feature.listing.domain.usecase

import com.hyms.feature.listing.domain.model.DraftListing
import com.hyms.feature.listing.domain.repository.DraftListingRepository
import kotlinx.coroutines.flow.Flow

class GetDraftListingUseCase(private val repository: DraftListingRepository) {
    operator fun invoke(userId: String? = null): Flow<DraftListing?> {
        return repository.getLatestDraftListingForUser(userId)
    }
}
