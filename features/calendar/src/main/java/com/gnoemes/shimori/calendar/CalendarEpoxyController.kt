package com.gnoemes.shimori.calendar

import android.content.Context
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.EpoxyController
import com.gnoemes.common.epoxy.withModelsFrom
import com.gnoemes.common.extensions.dimen
import com.gnoemes.common.extensions.dp
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
) : EpoxyController() {
    var callbacks: Callbacks? by observable(null, ::requestModelBuild)
    var state by observable(CalendarViewState(), ::requestModelBuild)

    interface Callbacks {
        fun onItemClicked(id: Long, type: ContentType)
    }

    override fun buildModels() {
        val items = state.results

        if (items.isNullOrEmpty()) {
            //TODO

            return
        }

        items.forEach { item ->
            if (item.animes.isEmpty()) return

            calendarHeader {
                id("calendar_${item.date.millis}")
                item(item)
                textCreator(calendarTextCreator)
            }

            shimoriCarousel {
                id("carousel_${item.date.millis}")
                hasFixedSize(false)

                val normalSpacing = context.dimen(R.dimen.spacing_normal).toInt()
                val itemSpacing = context.dp(8)

                padding(Carousel.Padding(normalSpacing, normalSpacing, normalSpacing, normalSpacing, itemSpacing))

                withModelsFrom(item.animes) { animeWithRate ->
                    val anime = animeWithRate.anime!!
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
                id("calendar_bottom_spacer")
            }
        }
    }

    fun clear() {
        callbacks = null
    }


}