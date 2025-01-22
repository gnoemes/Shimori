package com.gnoemes.shimori.domain.interactors.tracks

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.ShimoriTitleEntity
import com.gnoemes.shimori.data.anime.AnimeRepository
import com.gnoemes.shimori.data.manga.MangaRepository
import com.gnoemes.shimori.data.ranobe.RanobeRepository
import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.domain.Interactor
import com.gnoemes.shimori.settings.OnProgressCompleteAction
import com.gnoemes.shimori.settings.OnProgressNotInWatchingAction
import com.gnoemes.shimori.settings.ShimoriSettings
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
/**
 * checks
 * [OnProgressCompleteAction]
 * [OnProgressNotInWatchingAction]
 * settings
 */
class CalculateTrackStatus(
    private val animeRepository: AnimeRepository,
    private val mangaRepository: MangaRepository,
    private val ranobeRepository: RanobeRepository,
    private val settings: ShimoriSettings,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<CalculateTrackStatus.Params, CalculateTrackStatus.CalculatedTrackStatus>() {

    override suspend fun doWork(params: Params): CalculatedTrackStatus {
        return withContext(dispatchers.io) {
            val track = params.track
            val title = params.title ?: when (track.targetType) {
                TrackTargetType.ANIME -> animeRepository.queryById(track.targetId)
                TrackTargetType.MANGA -> mangaRepository.queryById(track.targetId)
                TrackTargetType.RANOBE -> ranobeRepository.queryById(track.targetId)
            }

            if (title == null) {
                //TODO sync title?
                return@withContext default(params.track.status)
            }

            if (track.status == TrackStatus.WATCHING && track.progress == title.size) {
                val onProgressCompleteAction = settings.onProgressCompleteAction.get()

                return@withContext when (onProgressCompleteAction) {
                    OnProgressCompleteAction.MoveToComplete -> default(TrackStatus.COMPLETED)
                    OnProgressCompleteAction.OpenTrackEdit -> openEdit(TrackStatus.COMPLETED)
                    else -> default(TrackStatus.COMPLETED)
                }
            }

            val onProgressNotInWatchingAction = settings.onProgressNotInWatchingAction.get()
            if (track.status != TrackStatus.WATCHING
                && onProgressNotInWatchingAction != OnProgressNotInWatchingAction.Nothing
            ) {
                return@withContext when (onProgressNotInWatchingAction) {
                    OnProgressNotInWatchingAction.MoveToWatching -> default(TrackStatus.WATCHING)
                    OnProgressNotInWatchingAction.OpenTrackEdit -> openEdit(TrackStatus.WATCHING)
                    else -> default(track.status)
                }
            }

            return@withContext default(track.status)
        }
    }

    private fun default(status: TrackStatus) = CalculatedTrackStatus(openEdit = false, status)
    private fun openEdit(status: TrackStatus) = CalculatedTrackStatus(openEdit = true, status)

    data class Params(
        val track: Track,
        val title: ShimoriTitleEntity? = null
    )

    data class CalculatedTrackStatus(
        val openEdit: Boolean,
        val status: TrackStatus,
    )
}