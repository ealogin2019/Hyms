package com.hyms.feature.listing.domain.model

data class DraftListing(
    val id: String,
    val userId: String? = null,
    val title: String = "",
    val category: String? = null,
    val genderSegment: String? = null,
    val sizeLabel: String? = null,
    val priceMinor: Long? = null,
    val currency: String = "GBP", // Default to GBP for UK region
    val condition: String? = null,
    val negotiable: Boolean = false,
    val updatedAt: Long
)
