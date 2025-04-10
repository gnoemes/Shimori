package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.eventbus.StateBus
import com.gnoemes.shimori.data.source.track.TrackManager
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.data.tracks.TrackRepository
import com.gnoemes.shimori.data.user.UserRepository
import com.gnoemes.shimori.domain.Interactor
import com.gnoemes.shimori.logging.api.Logger
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class UpdateTracks(
    private val trackManager: TrackManager,
    private val trackRepository: TrackRepository,
    private val userRepository: UserRepository,
    private val updateTitleTracks: UpdateTitleTracks,
    private val stateBus: StateBus,
    private val dispatchers: AppCoroutineDispatchers,
    private val logger: Logger,
) : Interactor<UpdateTracks.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val noLocalTracks = trackRepository.queryTracksCount() == 0
            //create event of first loading
            if (noLocalTracks) {
                stateBus.tracksLoading(true)
            }

            if (params.forceTitlesUpdate || params.optionalTitlesUpdate || noLocalTracks) {
                updateTitles(params.forceTitlesUpdate || noLocalTracks)
                stateBus.tracksLoading(false)
            } else {
                // sync only tracks
                trackManager.trackers.forEach { source ->
                    val user = userRepository.queryMeShort(source.id) ?: return@withContext
                    try {
                        val tracks = trackManager.track(source.id) {
                            getList(user)
                        }
                        trackRepository.trySync(tracks)
                        stateBus.tracksLoading(false)
                    } catch (e: Exception) {
                        //ignore for now
                        logger.e(throwable = e) { "Update tracks failed" }
                        stateBus.tracksLoading(false)
                    }
                }
            }
        }
    }

    //sync titles & tracks
    private suspend fun updateTitles(force: Boolean) {
        try {
            TrackTargetType.entries
                //currently, manga & ranobe have the single endpoint for update.
                //Avoid additional requests
                .filterNot { it.ranobe }
                .forEach { target ->
                    updateTitleTracks.invoke(
                        if (force) UpdateTitleTracks.Params.fullUpdate(target)
                        else UpdateTitleTracks.Params.optionalUpdate(target)
                    )
                }
        } catch (e: Exception) {
            logger.e(throwable = e) { "Update titles failed" }
            stateBus.tracksLoading(false)
        }
    }

    data class Params(
        val forceTitlesUpdate: Boolean,
        val optionalTitlesUpdate: Boolean,
    )
}