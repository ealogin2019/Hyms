package com.hyms.core.database.di

import android.content.Context
import androidx.room.Room
import com.hyms.core.database.HymsDatabase
import com.hyms.core.database.dao.DraftListingDao
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
        Room.databaseBuilder(context, HymsDatabase::class.java, "hyms.db").build()

    @Provides
    fun provideDraftListingDao(db: HymsDatabase): DraftListingDao =
        db.draftListingDao()
}
