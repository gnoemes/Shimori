package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.app.SourceDataType
import com.gnoemes.shimori.data.app.SyncAction
import com.gnoemes.shimori.data.source.track.TrackManager
import com.gnoemes.shimori.data.tracks.TrackRepository
import com.gnoemes.shimori.domain.Interactor
import com.gnoemes.shimori.logging.api.Logger
import com.gnoemes.shimori.sources.SourceIds
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import me.tatarka.inject.annotations.Inject

@Inject
class SyncPendingTracks(
    private val trackRepository: TrackRepository,
    private val trackManager: TrackManager,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<Unit, Unit>() {

    private companion object {
        const val SYNC_TAG = "SyncPendingTracks"
    }

    override suspend fun doWork(params: Unit) {
        withContext(dispatchers.io + SupervisorJob()) {
            val ratesToSync = trackRepository.querySyncPendingTracks()

            logger.d(
                message = { "Pending tracks sync started. Rates to sync: ${ratesToSync.size}" },
                tag = SYNC_TAG
            )

            ratesToSync.forEachIndexed { index, toSync ->
                val track = trackRepository.queryById(toSync.trackId)

                logger.d(
                    message = { "#$index: Action: ${toSync.action}, attempts: ${toSync.attempts}, last attempt: ${toSync.lastAttempt}" },
                    tag = SYNC_TAG
                )

                if (track == null && toSync.action != SyncAction.DELETE) {
                    logger.d(
                        message = { "#$index: Track is missing. Deleting ..." },
                        tag = SYNC_TAG
                    )
                    trackRepository.deleteByTrackId(toSync)
                    return@forEachIndexed
                }

                trackManager
                    .trackers
                    .filter {
                        if (toSync.attemptSourceId != null) it.id == toSync.attemptSourceId
                        else true
                    }
                    .forEach { source ->
                        try {
                            trackManager.track(source.id, track) {
                                launch {
                                    when (toSync.action) {
                                        SyncAction.DELETE -> delete(
                                            trackManager.findRemoteId(
                                                source.id,
                                                toSync.trackId,
                                                SourceDataType.Track
                                            )
                                        )

                                        else -> {
                                            val remoteTrack =
                                                if (toSync.action == SyncAction.CREATE) create(it!!)
                                                else update(it!!)

                                            // Force update track on auto status changes:
                                            // planned, paused -> watching. If progress changed from 0 to x
                                            // watching -> completed. If progress == size
                                            if (
                                                remoteTrack.status != track!!.status
                                                && source.id == SourceIds.SHIKIMORI
                                            ) {
                                                //update with same progress but with another status updates state for what we need (on Shikimori)
                                                update(it)
                                            }
                                        }
                                    }
                                }.join()
                            }
                        } catch (e: Exception) {
                            logger.d(
                                message = { "#$index: Sync error with source ${source.name}. $e \ncause: ${e.cause}" },
                                tag = SYNC_TAG
                            )

                            val httpException =
                                if (e is ClientRequestException) e
                                else if (e.cause is ClientRequestException) e.cause as ClientRequestException
                                else null

                            if (httpException != null && httpException.response.status == HttpStatusCode.NotFound) {
                                trackRepository.delete(toSync)
                            } else {
                                trackRepository.upsert(
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
                                message = { "#$index: Fail! Source: ${source.name}" },
                                tag = SYNC_TAG
                            )

                            return@forEach
                        }
                    }


                logger.d(
                    message = { "#$index: Done!" },
                    tag = SYNC_TAG
                )
            }

            trackRepository.pendingTracksSynced()
        }
    }
}