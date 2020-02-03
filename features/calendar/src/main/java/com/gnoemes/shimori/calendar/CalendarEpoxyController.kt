package com.gnoemes.shimori.calendar

import android.content.Context
import com.airbnb.epoxy.Carousel
import com.gnoemes.common.epoxy.ShimoriEpoxyController
import com.gnoemes.common.epoxy.withModelsFrom
import com.gnoemes.common.extensions.dimen
import com.gnoemes.common.searchEmpty
import com.gnoemes.common.textcreators.AnimeTextCreator
import com.gnoemes.common.ui.widgets.shimoriCarousel
import com.gnoemes.common.vertSpacerNormal
import com.gnoemes.shimori.base.di.PerActivity
import com.gnoemes.shimori.base.extensions.observable
import com.gnoemes.shimori.model.common.ContentType
import javax.inject.Inject

internal class CalendarEpoxyController @Inject constructor(
    @PerActivity private val context: Context,
    private val calendarTextCreator: CalendarTextCreator,
    private val animeTextCreator: AnimeTextCreator
) : ShimoriEpoxyController<CalendarViewState>() {
    var callbacks: Callbacks? by observable(null, { state })

    interface Callbacks {
        fun onItemClicked(id: Long, type: ContentType)
    }

    override fun buildModels(state: CalendarViewState) {
        val items = state.results

        if (items.isNullOrEmpty()) {
            searchEmpty {
                id("calendar_empty")
                title(context.getString(R.string.calendar_empty_title))
                description(context.getString(R.string.calendar_empty_description))
            }
            return
        }

        items.forEach { item ->
            calendarHeader {
                id("calendar_${item.date}")
                item(item)
                textCreator(calendarTextCreator)
            }

            shimoriCarousel {
                id("carousel_${item.date}")

                val normalSpacing = context.dimen(R.dimen.spacing_normal)
                val itemSpacing = context.dimen(R.dimen.spacing_small)

                padding(Carousel.Padding(normalSpacing, normalSpacing, normalSpacing, normalSpacing, itemSpacing))

                withModelsFrom(item.animes) { animeWithRate ->
                    val anime = animeWithRate.entity
                    val rate = animeWithRate.rate

                    CalendarAnimeBindingModel_()
                        .id("anime_${anime.shikimoriId}")
                        .item(anime)
                        .image(anime.image)
                        .rate(rate)
                        .textCreator(animeTextCreator)
                        .calendarTextCreator(calendarTextCreator)
                        .clickListener { _ -> callbacks?.onItemClicked(anime.shikimoriId!!, anime.contentType) }
                }
            }

            vertSpacerNormal {
                //must be unique too
                id("calendar_bottom_spacer_${item.date}")
            }
        }
    }

    fun clear() {
        callbacks = null
    }


}