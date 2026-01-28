package com.hyms.feature.listing.domain.usecase

import com.hyms.feature.listing.domain.model.DraftListing
import com.hyms.feature.listing.domain.repository.DraftListingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveLatestDraftUseCase @Inject constructor(private val repository: DraftListingRepository) {
    operator fun invoke(userId: String?): Flow<DraftListing?> {
        return repository.getLatestDraftListingForUser(userId)
    }
}
