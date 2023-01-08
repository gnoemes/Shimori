package com.gnoemes.shimori.tasks

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.gnoemes.shimori.base.core.tasks.TrackTasks

internal class TrackTasksImpl(
    private val workManager: WorkManager
) : TrackTasks {
    override fun syncPendingTracks() {
        val request = OneTimeWorkRequestBuilder<SyncPendingTracksWorker>()
            .addTag(SyncPendingTracksWorker.TAG)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        workManager.enqueue(request)
    }
}