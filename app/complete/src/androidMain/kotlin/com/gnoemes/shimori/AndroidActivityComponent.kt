package com.gnoemes.shimori

import android.app.Activity
import com.gnoemes.shimori.app.core.inject.SharedActivityComponent
import com.gnoemes.shimori.base.inject.ActivityScope
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@ActivityScope
@Component
abstract class AndroidActivityComponent(
    @get:Provides override val activity: Activity,
    @Component val applicationComponent: AndroidApplicationComponent
) : SharedActivityComponent {

    companion object
}