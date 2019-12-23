package com.gnoemes.common.epoxy

import android.app.Application
import android.content.Context
import android.view.Gravity
import androidx.recyclerview.widget.SnapHelper
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.EpoxyAsyncUtil
import com.airbnb.epoxy.EpoxyController
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.gnoemes.shimori.base.appinitializers.AppInitializer
import javax.inject.Inject

class EpoxyInitializer @Inject constructor() : AppInitializer {
    override fun init(app: Application) {
        // Make EpoxyController diffing and model building async by default
        val asyncHandler = EpoxyAsyncUtil.getAsyncBackgroundHandler()
        EpoxyController.defaultDiffingHandler = asyncHandler
        EpoxyController.defaultModelBuildingHandler = asyncHandler

        // Also setup Carousel to use a more sane snapping behavior
        Carousel.setDefaultGlobalSnapHelperFactory(object : Carousel.SnapHelperFactory() {
            override fun buildSnapHelper(context: Context): SnapHelper {
                return GravitySnapHelper(Gravity.START)
            }
        })
    }
}