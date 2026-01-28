package com.hyms.feature.listing.domain.usecase

import com.hyms.feature.listing.domain.model.DraftListing
import com.hyms.feature.listing.domain.repository.DraftListingRepository
import javax.inject.Inject

class UpsertDraftUseCase @Inject constructor(private val repository: DraftListingRepository) {
    suspend operator fun invoke(draftListing: DraftListing) {
        repository.upsertDraftListing(draftListing)
    }
}
