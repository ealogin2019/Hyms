package com.hyms.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hyms.core.database.entity.DraftListingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DraftListingDao {

    @Query("SELECT * FROM draft_listing ORDER BY updatedAt DESC LIMIT 1")
    fun getLatestDraftListing(): Flow<DraftListingEntity?>

    @Query("""
        SELECT * FROM draft_listing
        WHERE (:userId IS NULL AND userId IS NULL) OR (userId = :userId)
        ORDER BY updatedAt DESC
        LIMIT 1
    """)
    fun getLatestDraftListingForUser(userId: String?): Flow<DraftListingEntity?>

    @Query("SELECT * FROM draft_listing WHERE id = :id LIMIT 1")
    fun getDraftById(id: String): Flow<DraftListingEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDraftListing(draftListing: DraftListingEntity)

    @Query("DELETE FROM draft_listing WHERE id = :id")
    suspend fun deleteDraftById(id: String)

    @Query("DELETE FROM draft_listing")
    suspend fun clearAllDrafts()
}