package com.hyms.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hyms.core.database.entity.DraftMediaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DraftMediaDao {

    @Query("SELECT * FROM draft_media WHERE draftId = :draftId ORDER BY orderIndex ASC")
    fun observeByDraft(draftId: String): Flow<List<DraftMediaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<DraftMediaEntity>)

    @Query("DELETE FROM draft_media WHERE draftId = :draftId")
    suspend fun clearForDraft(draftId: String)

    @Query("DELETE FROM draft_media WHERE id = :id")
    suspend fun deleteById(id: String)
}