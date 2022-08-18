package com.gnoemes.shimori.tasks

import androidx.work.CoroutineWorker
import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.domain.interactors.SyncPendingRates
import kotlin.system.measureTimeMillis

internal class SyncPendingRatesWorker(
    payload: WorkerPayload,
    private val syncPendingRates: SyncPendingRates,
    private val logger: Logger
) : CoroutineWorker(payload.context, payload.params) {

    companion object {
        const val TAG = "SyncPendingRatesWorker"
    }

    override suspend fun doWork(): Result {
        logger.d(
            message = "Worker is running",
            tag = TAG
        )

        val time = measureTimeMillis {
            syncPendingRates.executeSync(Unit)
        }

        logger.d(
            message = "Work is done. Work time: $time mills",
            tag = TAG
        )

        return Result.success()
    }

}