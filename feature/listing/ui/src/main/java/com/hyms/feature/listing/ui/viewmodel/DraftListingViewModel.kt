package com.hyms.feature.listing.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyms.feature.listing.domain.model.DraftListing
import com.hyms.feature.listing.domain.usecase.ObserveLatestDraftUseCase
import com.hyms.feature.listing.domain.usecase.UpsertDraftUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DraftListingViewModel @Inject constructor(
    private val observeLatestDraftUseCase: ObserveLatestDraftUseCase,
    private val upsertDraftUseCase: UpsertDraftUseCase,
) : ViewModel() {

    data class UiState(
        val draft: DraftListing,
        val priceText: String = "", // raw input like "12.50"
        val isHydrated: Boolean = false
    )

    private val _uiState = MutableStateFlow<UiState?>(null)
    val uiState: StateFlow<UiState?> = _uiState.asStateFlow()

    private var saveJob: Job? = null

    private val currentUserId: String? = null // wire later (Auth)

    init {
        hydrateOnce()
        startDebouncedSave()
    }

    private fun hydrateOnce() {
        viewModelScope.launch {
            observeLatestDraftUseCase(currentUserId).collect { fromDb ->
                // IMPORTANT: only hydrate if we haven't already (prevents overwriting edits)
                if (_uiState.value == null || _uiState.value?.isHydrated == false) {
                    val draft = fromDb ?: newDraft()
                    _uiState.value = UiState(
                        draft = draft,
                        priceText = draft.priceMinor?.let { minorToGbpText(it) } ?: "",
                        isHydrated = true
                    )
                }
            }
        }
    }

    private fun startDebouncedSave() {
        // Save only after hydration and only when changes happen
        uiState
            .filterNotNull()
            .distinctUntilChanged()
            .onEach { state ->
                if (!state.isHydrated) return@onEach

                saveJob?.cancel()
                saveJob = viewModelScope.launch {
                    kotlinx.coroutines.delay(500)

                    val priceMinor = gbpTextToMinor(state.priceText)

                    upsertDraftUseCase(
                        state.draft.copy(
                            priceMinor = priceMinor,
                            // Currency locked to GBP for UK v1
                            currency = "GBP",
                            updatedAt = System.currentTimeMillis()
                        )
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    // -------- UI intent handlers --------

    fun onTitleChanged(title: String) = updateDraft { it.copy(title = title) }

    fun onGenderChanged(gender: String?) = updateDraft { it.copy(genderSegment = gender) }

    fun onCategoryChanged(category: String?) = updateDraft { it.copy(category = category) }

    fun onSizeChanged(size: String?) = updateDraft { it.copy(sizeLabel = size) }

    fun onConditionChanged(condition: String?) = updateDraft { it.copy(condition = condition) }

    fun onNegotiableChanged(negotiable: Boolean) = updateDraft { it.copy(negotiable = negotiable) }

    fun onPriceTextChanged(text: String) {
        // Allow only digits and one dot; strip currency symbols/spaces
        val cleaned = text
            .trim()
            .replace("£", "")
            .replace(",", ".")
            .filter { it.isDigit() || it == '.' }

        // keep at most one dot
        val normalized = buildString {
            var dotSeen = false
            for (c in cleaned) {
                if (c == '.') {
                    if (!dotSeen) {
                        dotSeen = true
                        append(c)
                    }
                } else append(c)
            }
        }

        _uiState.update { state ->
            state?.copy(priceText = normalized)
        }
    }

    private fun updateDraft(transform: (DraftListing) -> DraftListing) {
        _uiState.update { state ->
            state?.copy(draft = transform(state.draft))
        }
    }

    private fun newDraft(): DraftListing =
        DraftListing(
            id = UUID.randomUUID().toString(),
            userId = currentUserId,
            currency = "GBP",
            updatedAt = System.currentTimeMillis()
        )

    // -------- Price helpers (GBP) --------

    // "12.50" -> 1250, "12" -> 1200
    private fun gbpTextToMinor(text: String): Long? {
        val t = text.trim()
        if (t.isEmpty()) return null
        return try {
            val parts = t.split('.', limit = 2)
            val pounds = parts[0].ifEmpty { "0" }.toLong()
            val pence = when {
                parts.size == 1 -> 0
                parts[1].isEmpty() -> 0
                parts[1].length == 1 -> (parts[1] + "0").toInt()
                else -> parts[1].take(2).toInt()
            }
            pounds * 100 + pence
        } catch (_: Exception) {
            null
        }
    }

    private fun minorToGbpText(minor: Long): String {
        val pounds = minor / 100
        val pence = (minor % 100).toString().padStart(2, '0')
        return "$pounds.$pence"
    }
}
