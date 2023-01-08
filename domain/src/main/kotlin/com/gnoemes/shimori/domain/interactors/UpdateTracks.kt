package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.anime.AnimeRepository
import com.gnoemes.shimori.data.repositories.manga.MangaRepository
import com.gnoemes.shimori.data.repositories.ranobe.RanobeRepository
import com.gnoemes.shimori.data.repositories.track.TrackRepository
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext

class UpdateTracks(
    private val animeRepository: AnimeRepository,
    private val mangaRepository: MangaRepository,
    private val ranobeRepository: RanobeRepository,
    private val trackRepository: TrackRepository,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<UpdateTracks.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            if (params.full) {
                //sync titles & tracks
                animeRepository.updateMyTitlesByStatus(null)
                mangaRepository.updateMyTitlesByStatus(null)
                ranobeRepository.updateMyTitlesByStatus(null)
            } else {
                // sync only tracks
                trackRepository.sync()
            }
        }
    }

    data class Params(
        val full: Boolean
    )
}