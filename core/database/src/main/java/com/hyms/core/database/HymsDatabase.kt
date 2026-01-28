package com.hyms.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hyms.core.database.dao.DraftListingDao
import com.hyms.core.database.entity.DraftListingEntity

@Database(
    entities = [DraftListingEntity::class],
    version = 1,
    exportSchema = true
)
abstract class HymsDatabase : RoomDatabase() {

    abstract fun draftListingDao(): DraftListingDao

    companion object {
        // Add migration strategies here
    }
}
