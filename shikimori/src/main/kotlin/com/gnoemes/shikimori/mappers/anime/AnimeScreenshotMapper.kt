package com.gnoemes.shikimori.mappers.anime

import com.gnoemes.shikimori.entities.anime.AnimeScreenshotResponse
import com.gnoemes.shimori.data.core.entities.common.ShimoriImage
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeScreenshot
import com.gnoemes.shimori.data.core.mappers.Mapper

internal class AnimeScreenshotMapper : Mapper<AnimeScreenshotResponse, AnimeScreenshot> {

    override suspend fun map(from: AnimeScreenshotResponse): AnimeScreenshot {
        return AnimeScreenshot(
            id = 0,
            image = ShimoriImage(
                original = from.original,
                preview = from.preview
            )
        )
    }
}