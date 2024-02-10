package com.gnoemes.shimori

import android.app.Activity
import com.gnoemes.shimori.app.core.inject.SharedActivityComponent
import com.gnoemes.shimori.base.inject.ActivityScope
import com.gnoemes.shimori.home.ShimoriContent
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@ActivityScope
@Component
abstract class AndroidActivityComponent(
    @get:Provides override val activity: Activity,
    @Component val applicationComponent: AndroidApplicationComponent
) : SharedActivityComponent, DefaultUiComponent {
    abstract val shimoriContent: ShimoriContent

    companion object
}