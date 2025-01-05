package com.gnoemes.shimori.tasks

import com.gnoemes.shimori.base.tasks.TrackTasks
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

actual interface TasksPlatformComponent {
    @SingleIn(AppScope::class)
    @Provides
    fun provideTrackTasks(): TrackTasks = EmptyTrackTasks
}


object EmptyTrackTasks : TrackTasks {
    override fun syncPendingTracks() = Unit
}