package com.hyms.feature.feed.data

import com.hyms.core.database.dao.RecentViewDao
import com.hyms.core.database.entity.RecentViewEntity
import com.hyms.feature.feed.domain.model.RecentView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecentViewTracker @Inject constructor(
    private val dao: RecentViewDao
) {
    suspend fun markViewed(listingId: String) {
        dao.upsert(RecentViewEntity(listingId = listingId, viewedAt = System.currentTimeMillis()))
        dao.trimTo(200)
    }

    fun observeRecent(limit: Int = 20): Flow<List<RecentView>> =
        dao.observeRecent(limit).map { list ->
            list.map { RecentView(it.listingId, it.viewedAt) }
        }
}