package com.hyms.core.database.di

import android.content.Context
import androidx.room.Room
import com.hyms.core.database.HymsDatabase
import com.hyms.core.database.dao.DraftListingDao
import com.hyms.core.database.dao.RecentViewDao
import com.hyms.core.database.dao.DraftMediaDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideHymsDatabase(@ApplicationContext context: Context): HymsDatabase =
        Room.databaseBuilder(context, HymsDatabase::class.java, "hyms.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideDraftListingDao(db: HymsDatabase): DraftListingDao =
        db.draftListingDao()

    @Provides
    fun provideRecentViewDao(db: HymsDatabase): RecentViewDao = db.recentViewDao()

    @Provides
    fun provideDraftMediaDao(db: HymsDatabase): DraftMediaDao = db.draftMediaDao()
}
//         userId = userId,
//         title = title,
//         description = description,
//         price = price,
//         createdAt = createdAt,
//         updatedAt = updatedAt
//     )
//
// private fun DraftListing.toEntity(): DraftListingEntity =
//     DraftListingEntity(
//         id = id,
//         userId = userId,
//         title = title,
//         description = description,
//         price = price,
//         createdAt = createdAt,
//         updatedAt = updatedAt
//     )