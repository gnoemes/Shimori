package com.gnoemes.shimori.title

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.base.core.extensions.combine
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.gnoemes.shimori.domain.interactors.CreateOrUpdateTrack
import com.gnoemes.shimori.domain.interactors.UpdateTitle
import com.gnoemes.shimori.domain.observers.ObserveTitleWithTrackEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class TitleDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val updateTitle: UpdateTitle,
    private val updateTrack: CreateOrUpdateTrack,
    observeTitle: ObserveTitleWithTrackEntity,
) : ViewModel() {
    private val id: Long = savedStateHandle["id"]!!
    private val type: TrackTargetType = savedStateHandle["type"]!!

    val state = combine(
        observeTitle.flow,
        ::TitleDetailsViewState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = TitleDetailsViewState()
    )

    init {
        viewModelScope.launch {
            updateTitle(UpdateTitle.Params.optionalUpdate(id, type)).collect {
                Log.i("DEVE", "$it")
            }
        }

        observeTitle(ObserveTitleWithTrackEntity.Params(id, type))
    }
}