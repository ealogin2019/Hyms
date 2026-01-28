package com.hyms.feature.listing.data.di

import com.hyms.feature.listing.data.repository.DraftListingRepositoryImpl
import com.hyms.feature.listing.domain.repository.DraftListingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ListingDataModule {

    @Binds
    @Singleton
    abstract fun bindDraftListingRepository(
        impl: DraftListingRepositoryImpl
    ): DraftListingRepository
}
