package com.gnoemes.shimori.tasks

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import me.tatarka.inject.annotations.Inject

@Inject
class ShimoriWorkerFactory(
    private val syncPendingTracks: (Context, WorkerParameters) -> SyncPendingTracksWorker
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? = when (workerClassName) {
        name<SyncPendingTracksWorker>() -> syncPendingTracks(appContext, workerParameters)
        else -> null
    }

    private inline fun <reified C> name() = C::class.qualifiedName
}