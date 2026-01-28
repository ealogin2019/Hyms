package com.hyms.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "draft_listing",
    indices = [Index(value = ["updatedAt"])]
)
data class DraftListingEntity(
    @PrimaryKey val id: String,
    val userId: String? = null, // For future multi-account support
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
