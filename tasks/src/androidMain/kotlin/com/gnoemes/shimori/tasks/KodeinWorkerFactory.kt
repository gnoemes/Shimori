package com.gnoemes.shimori.tasks

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import org.kodein.di.DI
import org.kodein.di.instanceOrNull

class KodeinWorkerFactory(
    private val di: DI
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        val worker by di.instanceOrNull<WorkerPayload, ListenableWorker>(
            tag = workerClassName,
            arg = WorkerPayload(appContext, workerParameters),
        )
        return worker
    }
}