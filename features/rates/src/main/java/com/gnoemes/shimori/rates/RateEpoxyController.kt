package com.gnoemes.shimori.rates

import android.content.Context
import com.airbnb.epoxy.IdUtils
import com.airbnb.mvrx.Success
import com.gnoemes.common.epoxy.ShimoriEpoxyController
import com.gnoemes.common.textcreators.CompoundTextCreator
import com.gnoemes.common.utils.RateUtils
import com.gnoemes.shimori.base.extensions.observable
import com.gnoemes.shimori.model.ShikimoriContentEntity
import com.gnoemes.shimori.model.ShimoriEntity
import com.gnoemes.shimori.model.anime.Anime
import com.gnoemes.shimori.model.common.ContentType
import javax.inject.Inject

internal class RateEpoxyController @Inject constructor(
    private val context: Context,
    private val compoundTextCreator: CompoundTextCreator,
    private val rateSortTextCreator: RateSortTextCreator
) : ShimoriEpoxyController<RateViewState>() {
    var callbacks: Callbacks? by observable(null, { state })

    interface Callbacks {
        fun onItemClicked(id: Long, type: ContentType)

        fun onSortClicked()

        fun onOrderChangeClicked()
    }

    override fun buildModels(state: RateViewState) {
        if (state.rates !is Success) return

        val items = state.rates()

        if (items.isNullOrEmpty()) return

        rateSort {
            id("rate_sort")
            type(state.type)
            state.selectedCategory?.let {
                rateName(context.getString(RateUtils.getName(state.type, it)))
            }
            sort(state.sort)
            textCreator(rateSortTextCreator)
            rateCallbacks(callbacks)
        }

        items.forEach { entityWithRate ->
            val entity = entityWithRate.entity
            val rate = entityWithRate.rate

            rate {
                id(generateRateItemId(entity))
                rate(rate)
                entity(entity)
                textCreator(compoundTextCreator)
                //TODO
                if (entity is ShikimoriContentEntity) {
                    image(entity.image)
                    clickListener { _ -> callbacks?.onItemClicked(entity.shikimoriId!!, entity.contentType!!) }
                }
            }
        }
    }

    fun clear() {
        callbacks = null
    }

    private fun generateRateItemId(entity: ShimoriEntity): Long {
        val contentId = when (entity) {
            is Anime -> entity.shikimoriId
            else -> null
        }

        return IdUtils.hashString64Bit("rate_$contentId")
    }
}