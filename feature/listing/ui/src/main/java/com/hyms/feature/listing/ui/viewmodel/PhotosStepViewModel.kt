package com.hyms.feature.listing.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyms.feature.listing.domain.repository.DraftListingRepository
import com.hyms.feature.listing.domain.usecase.ObserveLatestDraftUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PhotosStepViewModel @Inject constructor(
    private val repository: DraftListingRepository,
    observeLatestDraftUseCase: ObserveLatestDraftUseCase
) : ViewModel() {

    // For Phase 1: single user -> userId = null
    private val currentUserId: String? = null

    val currentDraftId: StateFlow<String?> =
        observeLatestDraftUseCase(currentUserId)
            .map { it?.id }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val mediaUris: StateFlow<List<String>> =
        currentDraftId
            .filterNotNull()
            .flatMapLatest { draftId -> repository.observeDraftMedia(draftId) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun replaceDraftMedia(uris: List<String>) {
        val draftId = currentDraftId.value ?: return
        viewModelScope.launch { repository.replaceDraftMedia(draftId, uris) }
    }
}