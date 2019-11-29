package com.gnoemes.shimori.search

import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyModel
import com.gnoemes.common.textcreators.AnimeTextCreator
import com.gnoemes.shimori.base.extensions.observable
import com.gnoemes.shimori.model.ContentType

import javax.inject.Inject

class SearchEpoxyController @Inject constructor(
    private val textCreator: AnimeTextCreator
) : EpoxyController() {
    var callbacks: Callbacks? by observable(null, ::requestModelBuild)
    var viewState by observable(SearchViewState(), ::requestModelBuild)

    interface Callbacks {
        fun onItemClicked(id: Long, type: ContentType)
    }

    override fun buildModels() {
        val items = viewState.resuls

        items?.forEach { item ->
            searchGrid {
                id(item.id)
                item(item)
                image(item.image)
                textCreator(textCreator)
                clickListener { _ -> callbacks?.onItemClicked(item.id, item.contentType) }
                spanSizeOverride(SearchSpanOverride)
            }
        }
    }

    fun clear() {
        callbacks = null
    }

    object SearchSpanOverride : EpoxyModel.SpanSizeOverrideCallback {
        override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int) = 1
    }
}
