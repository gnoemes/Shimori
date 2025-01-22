package com.gnoemes.shimori.domain.interactors.tracks

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.ShimoriTitleEntity
import com.gnoemes.shimori.data.anime.AnimeRepository
import com.gnoemes.shimori.data.manga.MangaRepository
import com.gnoemes.shimori.data.ranobe.RanobeRepository
import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class CalculateTrackProgress(
    private val animeRepository: AnimeRepository,
    private val mangaRepository: MangaRepository,
    private val ranobeRepository: RanobeRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<CalculateTrackProgress.Params, Int>() {

    override suspend fun doWork(params: Params): Int {
        return withContext(dispatchers.io) {
            val track = params.track
            val title = params.title ?: when (track.targetType) {
                TrackTargetType.ANIME -> animeRepository.queryById(track.targetId)
                TrackTargetType.MANGA -> mangaRepository.queryById(track.targetId)
                TrackTargetType.RANOBE -> ranobeRepository.queryById(track.targetId)
            }

            return@withContext (track.progress + params.changeBy)
                .let {
                    if (it < 0) 0
                    else if (title?.size != null && it > title.size!!) title.size!!
                    else it
                }
        }
    }

    data class Params(
        val changeBy: Int,
        val track: Track,
        val title: ShimoriTitleEntity?
    )
}