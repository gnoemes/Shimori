package com.gnoemes.shimori.tasks

import android.app.Application
import androidx.work.WorkManager
import com.gnoemes.shimori.base.tasks.TrackTasks
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

actual interface TasksPlatformComponent {
    @SingleIn(AppScope::class)
    @Provides
    fun provideShowTasks(bind: AndroidTrackTasksImpl): TrackTasks = bind

    @SingleIn(AppScope::class)
    @Provides
    fun provideWorkManager(application: Application): WorkManager {
        return WorkManager.getInstance(application)
    }
}