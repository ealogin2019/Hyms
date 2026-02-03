package com.hyms.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recent_view")
data class RecentViewEntity(
    @PrimaryKey val listingId: String,
    val viewedAt: Long
)