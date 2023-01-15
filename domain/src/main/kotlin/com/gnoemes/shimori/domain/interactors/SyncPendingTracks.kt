package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.core.entities.app.SyncAction
import com.gnoemes.shimori.data.repositories.source.SourceRepository
import com.gnoemes.shimori.data.repositories.track.SyncPendingTracksLastRequestStore
import com.gnoemes.shimori.data.repositories.track.TrackRepository
import com.gnoemes.shimori.domain.Interactor
import io.ktor.client.plugins.*
import io.ktor.http.*
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class SyncPendingTracks(
    private val trackRepository: TrackRepository,
    private val sourceRepository: SourceRepository,
    private val logger: Logger,
    private val syncPendingTracksLastRequest: SyncPendingTracksLastRequestStore,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<Unit>() {

    private companion object {
        const val SYNC_TAG = "SyncPendingTracks"
    }

    override suspend fun doWork(params: Unit) {
        withContext(dispatchers.io + SupervisorJob()) {
            val ratesToSync = trackRepository.querySyncPendingTracks()

            logger.d(
                message = "Pending tracks sync started. Rates to sync: ${ratesToSync.size}",
                tag = SYNC_TAG
            )

            ratesToSync.forEachIndexed { index, toSync ->
                val track = trackRepository.queryById(toSync.trackId)

                logger.d(
                    message = "#$index: Action: ${toSync.action}, attempts: ${toSync.attempts}, last attempt: ${toSync.lastAttempt}",
                    tag = SYNC_TAG
                )

                if (track == null && toSync.action != SyncAction.DELETE) {
                    logger.d(
                        message = "#$index: Track is missing. Deleting ...",
                        tag = SYNC_TAG
                    )
                    trackRepository.deleteByTrackId(toSync)
                    return@forEachIndexed
                }


                sourceRepository
                    .trackSources
                    .filter {
                        if (toSync.attemptSourceId != null) it.id == toSync.attemptSourceId
                        else true
                    }
                    .forEach { source ->
                        try {
                            launch {
                                when (toSync.action) {
                                    SyncAction.DELETE -> sourceRepository.deleteTrack(
                                        source.id,
                                        toSync.trackId
                                    )
                                    else -> {
                                        sourceRepository.createOrUpdateTrack(source.id, track!!)
                                    }
                                }
                            }.join()

                            trackRepository.delete(toSync)

                        } catch (e: Exception) {
                            logger.d(
                                message = "#$index: Sync error with source ${source.name}. $e \ncause: ${e.cause}",
                                tag = SYNC_TAG
                            )

                            val httpException =
                                if (e is ClientRequestException) e
                                else if (e.cause is ClientRequestException) e.cause as ClientRequestException
                                else null

                            if (httpException != null && httpException.response.status == HttpStatusCode.NotFound) {
                                trackRepository.delete(toSync)
                            } else {
                                trackRepository.createOrUpdate(
                                    toSync.copy(
                                        //create new record if it failed first time
                                        id = if (toSync.attemptSourceId == null) 0 else toSync.id,
                                        attempts = toSync.attempts + 1,
                                        lastAttempt = Clock.System.now(),
                                        attemptSourceId = source.id
                                    )
                                )
                            }

                            logger.d(
                                message = "#$index: Fail! Source: ${source.name}",
                                tag = SYNC_TAG
                            )

                            return@forEach
                        }
                    }

                logger.d(
                    message = "#$index: Success!",
                    tag = SYNC_TAG
                )
            }

            syncPendingTracksLastRequest.updateLastRequest()
        }
    }
}