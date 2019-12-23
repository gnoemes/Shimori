package com.gnoemes.common.epoxy

import com.airbnb.epoxy.EpoxyModel
import com.gnoemes.common.ui.widgets.ShimoriCarouselModelBuilder

inline fun <T> ShimoriCarouselModelBuilder.withModelsFrom(
    items: List<T>,
    modelBuilder: (T) -> EpoxyModel<*>
) {
    models(items.map(modelBuilder))
}