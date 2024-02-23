package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.source.track.TrackManager
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.data.tracks.TrackRepository
import com.gnoemes.shimori.data.user.UserRepository
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class UpdateTracks(
    private val trackManager: TrackManager,
    private val trackRepository: TrackRepository,
    private val userRepository: UserRepository,
    private val updateTitleTracks: UpdateTitleTracks,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<UpdateTracks.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            if (params.withTitles) {
                //sync titles & tracks
                updateTitleTracks.invoke(
                    UpdateTitleTracks.Params.optionalUpdate(
                        TrackTargetType.ANIME
                    )
                )

                updateTitleTracks.invoke(
                    UpdateTitleTracks.Params.optionalUpdate(
                        TrackTargetType.MANGA
                    )
                )

                updateTitleTracks.invoke(
                    UpdateTitleTracks.Params.optionalUpdate(
                        TrackTargetType.RANOBE
                    )
                )
            } else {
                // sync only tracks
                trackManager.trackers.forEach { source ->
                    val user = userRepository.queryMeShort(source.id) ?: return@withContext
                    try {
                        val tracks = trackManager.track(source.id, user) {
                            getList(it)
                        }
                        trackRepository.trySync(tracks)
                    } catch (e: Exception) {
                        //ignore for now
                    }
                }
            }
        }
    }

    data class Params(
        val withTitles: Boolean
    )
}