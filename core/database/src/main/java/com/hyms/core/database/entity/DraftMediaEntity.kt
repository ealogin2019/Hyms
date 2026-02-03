package com.hyms.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "draft_media",
    indices = [Index(value = ["draftId"])]
)
data class DraftMediaEntity(
    @PrimaryKey val id: String,
    val draftId: String,
    val localUri: String,
    val orderIndex: Int,
    val uploadState: String = "PENDING",
    val remoteUrl: String? = null,
    val updatedAt: Long
)