package com.hyms.feature.feed.data.di

import com.hyms.feature.feed.data.FakeFeedRepository
import com.hyms.feature.feed.domain.repository.FeedRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FeedDataModule {
    @Binds
    @Singleton
    abstract fun bindFeedRepository(impl: FakeFeedRepository): FeedRepository
}