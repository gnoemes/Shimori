package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.gnoemes.shimori.data.repositories.source.SourceRepository
import com.gnoemes.shimori.data.repositories.track.TrackRepository
import com.gnoemes.shimori.data.repositories.user.UserRepository
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext

class UpdateTracks(
    private val sourceRepository: SourceRepository,
    private val trackRepository: TrackRepository,
    private val userRepository: UserRepository,
    private val updateTitleTracks: UpdateTitleTracks,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<UpdateTracks.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            if (params.withTitles) {
                //sync titles & tracks
                updateTitleTracks.executeSync(
                    UpdateTitleTracks.Params.optionalUpdate(
                        TrackTargetType.ANIME
                    )
                )

                updateTitleTracks.executeSync(
                    UpdateTitleTracks.Params.optionalUpdate(
                        TrackTargetType.MANGA
                    )
                )

                updateTitleTracks.executeSync(
                    UpdateTitleTracks.Params.optionalUpdate(
                        TrackTargetType.RANOBE
                    )
                )
            } else {
                // sync only tracks
                sourceRepository.trackSources.forEach { source ->
                    val user = userRepository.queryMeShort(source.id) ?: return@withContext
                    try {
                        val (sourceId, remote) = sourceRepository.getTracks(source.id, user)
                        trackRepository.sync(sourceId, remote)
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