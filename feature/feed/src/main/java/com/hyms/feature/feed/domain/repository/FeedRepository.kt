package com.hyms.feature.feed.domain.repository

import com.hyms.feature.feed.domain.model.FeedItem
import kotlinx.coroutines.flow.Flow

interface FeedRepository {
    fun observeHomeFeed(): Flow<List<FeedItem>>
}