package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.TitleWithTrackEntity
import com.gnoemes.shimori.data.anime.AnimeRepository
import com.gnoemes.shimori.data.manga.MangaRepository
import com.gnoemes.shimori.data.ranobe.RanobeRepository
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveTitleWithTrackEntity(
    private val animeRepository: AnimeRepository,
    private val mangaRepository: MangaRepository,
    private val ranobeRepository: RanobeRepository,
) : SubjectInteractor<ObserveTitleWithTrackEntity.Params, TitleWithTrackEntity?>() {

    override fun create(params: Params): Flow<TitleWithTrackEntity?> = when (params.type) {
        TrackTargetType.ANIME -> animeRepository.observeById(params.id)
        TrackTargetType.MANGA -> mangaRepository.observeById(params.id)
        TrackTargetType.RANOBE -> ranobeRepository.observeById(params.id)
    }

    data class Params(
        val id: Long,
        val type: TrackTargetType
    )
}