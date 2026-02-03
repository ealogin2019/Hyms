package com.hyms.feature.feed.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyms.feature.feed.data.RecentViewTracker
import com.hyms.feature.feed.domain.repository.FeedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFeedViewModel @Inject constructor(
    repository: FeedRepository,
    private val recentViewTracker: RecentViewTracker
) : ViewModel() {
    val items = repository.observeHomeFeed()
    val recentViewed = recentViewTracker.observeRecent()

    fun onItemOpened(id: String) {
        viewModelScope.launch { recentViewTracker.markViewed(id) }
    }
}