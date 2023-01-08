package com.gnoemes.shimori.tasks

import androidx.work.ListenableWorker
import com.gnoemes.shimori.base.core.tasks.TrackTasks
import org.kodein.di.*

actual val tasksModule = DI.Module("tasks-module") {
    bindWorker { payload -> SyncPendingTracksWorker(payload, instance(), instance()) }

    bindEagerSingleton { KodeinWorkerFactory(di) }

    bindSingleton<TrackTasks> { new(::TrackTasksImpl) }
}

private inline fun <reified T : ListenableWorker> DI.Builder.bindWorker(
    noinline creator: DirectDI.(WorkerPayload) -> T
) {
    return bindFactory(tag = T::class.java.canonicalName, creator = creator)
}