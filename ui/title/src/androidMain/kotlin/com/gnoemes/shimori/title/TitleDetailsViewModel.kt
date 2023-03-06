package com.gnoemes.shimori.title

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.base.core.extensions.instantCombine
import com.gnoemes.shimori.common.ui.api.OptionalContent
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.gnoemes.shimori.domain.interactors.CreateOrUpdateTrack
import com.gnoemes.shimori.domain.interactors.UpdateTitle
import com.gnoemes.shimori.domain.observers.ObserveAnimeScreenshots
import com.gnoemes.shimori.domain.observers.ObserveAnimeVideos
import com.gnoemes.shimori.domain.observers.ObserveCharacters
import com.gnoemes.shimori.domain.observers.ObserveTitleWithTrackEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class TitleDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val updateTitle: UpdateTitle,
    private val updateTrack: CreateOrUpdateTrack,
    observeTitle: ObserveTitleWithTrackEntity,
    observeCharacters: ObserveCharacters,
    observeAnimeVideos: ObserveAnimeVideos,
    observeAnimeScreenshots: ObserveAnimeScreenshots,
) : ViewModel() {
    private val id: Long = savedStateHandle["id"]!!
    private val type: TrackTargetType = savedStateHandle["type"]!!

    private val updated = MutableStateFlow(false)

    val state = instantCombine(
        observeTitle.flow,
        updated,
        observeCharacters.flow,
        observeAnimeVideos.flow,
        observeAnimeScreenshots.flow
    ) { title, updated, characters, videos, screenshots ->
        TitleDetailsViewState(
            title = title,
            characters = OptionalContent(
                loaded = !characters.isNullOrEmpty() || updated == true,
                content = characters
            ),
            videos = OptionalContent(
                loaded = !type.anime || updated == true || !videos.isNullOrEmpty(),
                content = if (type.anime) videos else null
            ),
            screenshots = OptionalContent(
                loaded = !type.anime || updated == true || !screenshots.isNullOrEmpty(),
                content = if (type.anime) screenshots else null
            ),


        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = TitleDetailsViewState()
    )

    init {
        viewModelScope.launch {
            updateTitle(UpdateTitle.Params.optionalUpdate(id, type)).collect {
                updated.value = it.isSuccess || it.isError
                Log.i("DEVE", "$it")
            }
        }

        observeTitle(ObserveTitleWithTrackEntity.Params(id, type))
        observeCharacters(ObserveCharacters.Params(id, type))

        if (type.anime) observeAnimeVideos(ObserveAnimeVideos.Params(id))
        if (type.anime) observeAnimeScreenshots(ObserveAnimeScreenshots.Params(id))
    }
}