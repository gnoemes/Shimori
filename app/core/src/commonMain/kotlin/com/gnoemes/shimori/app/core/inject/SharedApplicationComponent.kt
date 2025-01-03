package com.gnoemes.shimori.app.core.inject

import com.gnoemes.shimori.base.inject.ApplicationCoroutineScope
import com.gnoemes.shimori.base.inject.ApplicationScope
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.common.imageloading.ImageLoadingComponent
import com.gnoemes.shimori.data.SqlDelightDatabaseComponent
import com.gnoemes.shimori.data.source.SourcesComponent
import com.gnoemes.shimori.logging.impl.LoggerComponent
import com.gnoemes.shimori.preferences.PreferencesComponent
import com.gnoemes.shimori.tasks.TasksComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import me.tatarka.inject.annotations.Provides

expect interface SharedPlatformApplicationComponent

interface SharedApplicationComponent :
    SharedPlatformApplicationComponent,
    PreferencesComponent,
    ImageLoadingComponent,
    LoggerComponent,
    SqlDelightDatabaseComponent,
    SourcesComponent,
    TasksComponent {

    @ApplicationScope
    @Provides
    fun provideCoroutineDispatchers(): AppCoroutineDispatchers = AppCoroutineDispatchers(
        io = Dispatchers.IO,
        databaseWrite = Dispatchers.IO.limitedParallelism(1),
        databaseRead = Dispatchers.IO.limitedParallelism(4),
        computation = Dispatchers.Default,
        main = Dispatchers.Main,
    )

    @ApplicationScope
    @Provides
    fun provideApplicationCoroutineScope(
        dispatchers: AppCoroutineDispatchers,
    ): ApplicationCoroutineScope = CoroutineScope(dispatchers.main + SupervisorJob())
}