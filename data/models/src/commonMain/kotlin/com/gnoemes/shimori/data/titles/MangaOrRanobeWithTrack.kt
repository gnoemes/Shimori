package com.gnoemes.shimori.data.titles

import com.gnoemes.shimori.data.ShimoriTitleEntity
import com.gnoemes.shimori.data.track.Track

//shikimori and MAL has no difference between Manga & Ranobe, but we want to differ them
//we can decrease count of requests by mapping all data and store in different tables at once
//example: shikimori/api/user_rates/mangas returns mangas AND ranobes
data class MangaOrRanobeWithTrack(
    val entity: ShimoriTitleEntity,
    val track: Track?
) {
    val type get() = entity.type
}