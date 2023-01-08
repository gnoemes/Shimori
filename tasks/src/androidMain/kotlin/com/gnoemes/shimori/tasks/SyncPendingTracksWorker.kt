package com.gnoemes.shimori.tasks

import androidx.work.CoroutineWorker
import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.domain.interactors.SyncPendingTracks
import kotlin.system.measureTimeMillis

internal class SyncPendingTracksWorker(
    payload: WorkerPayload,
    private val syncPendingTracks: SyncPendingTracks,
    private val logger: Logger
) : CoroutineWorker(payload.context, payload.params) {

    companion object {
        const val TAG = "SyncPendingTracksWorker"
    }

    override suspend fun doWork(): Result {
        logger.d(
            message = "Worker is running",
            tag = TAG
        )

        val time = measureTimeMillis {
            syncPendingTracks.executeSync(Unit)
        }

        logger.d(
            message = "Work is done. Work time: $time mills",
            tag = TAG
        )

        return Result.success()
    }

}