package com.gnoemes.shimori.tasks

import androidx.work.ListenableWorker
import com.gnoemes.shimori.base.core.tasks.RateTasks
import org.kodein.di.*

actual val tasksModule = DI.Module("tasks-module") {
    bindWorker { payload -> SyncPendingRatesWorker(payload, instance(), instance()) }

    bindEagerSingleton { KodeinWorkerFactory(di) }

    bindSingleton<RateTasks> { new(::RateTasksImpl) }
}

private inline fun <reified T : ListenableWorker> DI.Builder.bindWorker(
    noinline creator: DirectDI.(WorkerPayload) -> T
) {
    return bindFactory(tag = T::class.java.canonicalName, creator = creator)
}