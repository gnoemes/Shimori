package com.gnoemes.shikimori.mappers

import com.gnoemes.shikimori.entities.ImageResponse
import com.gnoemes.shimori.data_base.mappers.Mapper
import com.gnoemes.shimori.model.ShimoriConstants
import com.gnoemes.shimori.model.ShimoriImage
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
internal class ImageResponseMapper @Inject constructor() : Mapper<ImageResponse, ShimoriImage> {

    override suspend fun map(from: ImageResponse): ShimoriImage = ShimoriImage(
            from.original?.appendHostIfNeed(),
            from.preview?.appendHostIfNeed(),
            from.x96?.appendHostIfNeed(),
            from.x48?.appendHostIfNeed()
    )

    private fun String.appendHostIfNeed(host: String = ShimoriConstants.ShikimoriBaseUrl): String {
        return if (this.contains("http")) this else host + this
    }
}