package com.gnoemes.shimori.tasks

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.gnoemes.shimori.base.core.tasks.RateTasks

internal class RateTasksImpl(
    private val workManager: WorkManager
) : RateTasks {
    override fun syncPendingRates() {
        val request = OneTimeWorkRequestBuilder<SyncPendingRatesWorker>()
            .addTag(SyncPendingRatesWorker.TAG)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        workManager.enqueue(request)
    }
}