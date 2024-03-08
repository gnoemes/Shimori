package com.gnoemes.shimori.tasks

import android.app.Application
import androidx.work.WorkManager
import com.gnoemes.shimori.base.inject.ApplicationScope
import com.gnoemes.shimori.base.tasks.TrackTasks
import me.tatarka.inject.annotations.Provides

actual interface TasksPlatformComponent {
    @ApplicationScope
    @Provides
    fun provideShowTasks(bind: AndroidTrackTasksImpl): TrackTasks = bind

    @ApplicationScope
    @Provides
    fun provideWorkManager(application: Application): WorkManager {
        return WorkManager.getInstance(application)
    }
}