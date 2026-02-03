package com.hyms.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hyms.core.database.entity.RecentViewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentViewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: RecentViewEntity)

    @Query("SELECT * FROM recent_view ORDER BY viewedAt DESC LIMIT :limit")
    fun observeRecent(limit: Int): Flow<List<RecentViewEntity>>

    @Query("""
        DELETE FROM recent_view
        WHERE listingId NOT IN (
          SELECT listingId FROM recent_view ORDER BY viewedAt DESC LIMIT :limit
        )
    """)
    suspend fun trimTo(limit: Int)
}