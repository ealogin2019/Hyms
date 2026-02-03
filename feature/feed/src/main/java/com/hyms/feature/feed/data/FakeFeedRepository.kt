package com.hyms.feature.feed.data

import com.hyms.feature.feed.domain.model.FeedItem
import com.hyms.feature.feed.domain.repository.FeedRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class FakeFeedRepository @Inject constructor() : FeedRepository {
    override fun observeHomeFeed(): Flow<List<FeedItem>> = flowOf(
        List(30) { idx ->
            FeedItem(
                id = "item_$idx",
                imageUrl = "https://picsum.photos/id/${1000 + idx}/900/1200", // Changed image URL
                title = "Garment $idx",
                brand = listOf("COS", "ARKET", "Zara", "Uniqlo", "Nike").random(),
                sizeLabel = listOf("XS", "S", "M", "L", "UK 10", "UK 12").random(),
                priceMinor = Random.nextInt(800, 8500).toLong(),
                currency = "GBP",
                condition = listOf("New", "Like New", "Good").random()
            )
        }
    )
}