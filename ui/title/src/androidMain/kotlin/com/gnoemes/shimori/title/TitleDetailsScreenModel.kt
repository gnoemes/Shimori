package com.gnoemes.shimori.title

import android.util.Log
import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.base.core.extensions.instantCombine
import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.common.ui.api.OptionalContent
import com.gnoemes.shimori.common.ui.navigation.FeatureScreen
import com.gnoemes.shimori.common.ui.navigation.StateScreenModel
import com.gnoemes.shimori.data.core.entities.TitleWithTrackEntity
import com.gnoemes.shimori.data.core.entities.characters.Character
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeScreenshot
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeVideo
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.gnoemes.shimori.domain.interactors.CreateOrUpdateTrack
import com.gnoemes.shimori.domain.interactors.UpdateTitle
import com.gnoemes.shimori.domain.observers.ObserveAnimeScreenshots
import com.gnoemes.shimori.domain.observers.ObserveAnimeVideos
import com.gnoemes.shimori.domain.observers.ObserveCharacters
import com.gnoemes.shimori.domain.observers.ObserveTitleWithTrackEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class TitleDetailsScreenModel(
    args: FeatureScreen.TitleDetails,
    private val updateTitle: UpdateTitle,
    private val updateTrack: CreateOrUpdateTrack,
    observeTitle: ObserveTitleWithTrackEntity,
    observeCharacters: ObserveCharacters,
    observeAnimeVideos: ObserveAnimeVideos,
    observeAnimeScreenshots: ObserveAnimeScreenshots,
    dispatchers: AppCoroutineDispatchers,
) : StateScreenModel<TitleDetailsScreenState>(TitleDetailsScreenState(), dispatchers) {
    private val id: Long = args.id
    private val type: TrackTargetType = args.type

    private val updated = MutableStateFlow(false)

    init {
        ioCoroutineScope.launch {
            instantCombine(
                observeTitle.flow,
                updated,
                observeCharacters.flow,
                observeAnimeVideos.flow,
                observeAnimeScreenshots.flow
            ) { title, updated, characters, videos, screenshots ->
                TitleDetailsScreenState(
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
            }.collectLatest { state ->
                mutableState.update { state }
            }
        }

        ioCoroutineScope.launch {
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

@Immutable
internal data class TitleDetailsScreenState(
    val title: TitleWithTrackEntity? = null,
    val characters: OptionalContent<List<Character>?> = OptionalContent(false, null),
    val videos: OptionalContent<List<AnimeVideo>?> = OptionalContent(false, null),
    val screenshots: OptionalContent<List<AnimeScreenshot>?> = OptionalContent(false, null),
)