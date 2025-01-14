package com.gnoemes.shimori.tasks

import com.gnoemes.shimori.base.inject.ApplicationScope
import com.gnoemes.shimori.base.tasks.TrackTasks
import me.tatarka.inject.annotations.Provides

actual interface TasksPlatformComponent {
    @ApplicationScope
    @Provides
    fun provideTrackTasks(): TrackTasks = EmptyTrackTasks
}


object EmptyTrackTasks : TrackTasks {
    override fun syncPendingTracks() = Unit
}