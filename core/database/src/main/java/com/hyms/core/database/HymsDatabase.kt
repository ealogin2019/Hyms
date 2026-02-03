package com.hyms.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hyms.core.database.dao.DraftListingDao
import com.hyms.core.database.dao.RecentViewDao
import com.hyms.core.database.dao.DraftMediaDao
import com.hyms.core.database.entity.DraftListingEntity
import com.hyms.core.database.entity.RecentViewEntity
import com.hyms.core.database.entity.DraftMediaEntity

@Database(
    entities = [DraftListingEntity::class, RecentViewEntity::class, DraftMediaEntity::class],
    version = 2,
    exportSchema = true
)
abstract class HymsDatabase : RoomDatabase() {

    abstract fun draftListingDao(): DraftListingDao
    abstract fun recentViewDao(): RecentViewDao
    abstract fun draftMediaDao(): DraftMediaDao
}