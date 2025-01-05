package com.gnoemes.shimori

import android.app.Application
import com.gnoemes.shimori.app.core.inject.SharedApplicationComponent
import com.gnoemes.shimori.base.appinitializers.AppInitializer
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.settings.ShimoriSettings
import com.gnoemes.shimori.tasks.ShimoriWorkerFactory
import me.tatarka.inject.annotations.Provides
import org.publicvalue.multiplatform.oidc.appsupport.CodeAuthFlowFactory
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@MergeComponent(AppScope::class)
@SingleIn(AppScope::class)
abstract class AndroidApplicationComponent(
    @get:Provides val application: Application,
) : SharedApplicationComponent, DefaultApplicationComponent {

    abstract val initializers: Set<AppInitializer>
    abstract val dispatchers: AppCoroutineDispatchers
    abstract val workerFactory: ShimoriWorkerFactory
    abstract val codeAuthFlowFactory: CodeAuthFlowFactory
    abstract val settings: ShimoriSettings

    abstract val activityComponentFactory: AndroidActivityComponent.Factory

    companion object
}