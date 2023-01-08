package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.core.entities.TitleWithTrackEntity
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.gnoemes.shimori.data.repositories.anime.AnimeRepository
import com.gnoemes.shimori.data.repositories.manga.MangaRepository
import com.gnoemes.shimori.data.repositories.ranobe.RanobeRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow

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