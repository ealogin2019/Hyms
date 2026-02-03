package com.hyms.feature.feed.domain.model

data class FeedItem(
    val id: String,
    val imageUrl: String? = null,
    val title: String,
    val brand: String?,
    val sizeLabel: String?,
    val priceMinor: Long,
    val currency: String = "GBP",
    val condition: String? = null
)