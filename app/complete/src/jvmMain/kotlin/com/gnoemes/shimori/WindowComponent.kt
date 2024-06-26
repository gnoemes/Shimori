package com.gnoemes.shimori

import com.gnoemes.shimori.base.inject.ActivityScope
import com.gnoemes.shimori.home.ShimoriContent
import me.tatarka.inject.annotations.Component

@ActivityScope
@Component
abstract class WindowComponent(
    @Component val applicationComponent: DesktopApplicationComponent,
) : DefaultUiComponent {
    abstract val shimoriContent: ShimoriContent
    companion object
}