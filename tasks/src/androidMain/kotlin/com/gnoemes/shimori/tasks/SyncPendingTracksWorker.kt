package com.gnoemes.shimori.tasks

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.gnoemes.shimori.domain.interactors.tracks.SyncPendingTracks
import com.gnoemes.shimori.logging.api.Logger
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import kotlin.system.measureTimeMillis

@Inject
class SyncPendingTracksWorker(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val syncPendingTracks: Lazy<SyncPendingTracks>,
    private val logger: Logger
) : CoroutineWorker(context, params) {

    companion object {
        const val TAG = "SyncPendingTracksWorker"
    }

    override suspend fun doWork(): Result {
        logger.d(
            message = { "Worker is running" },
            tag = TAG
        )

        val time = measureTimeMillis {
            syncPendingTracks.value.invoke(Unit)
        }

        logger.d(
            message = { "Work is done. Work time: $time mills" },
            tag = TAG
        )

        return Result.success()
    }

}