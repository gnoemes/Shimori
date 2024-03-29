package com.gnoemes.shikimori.mappers

import com.gnoemes.shikimori.appendHostIfNeed
import com.gnoemes.shikimori.entities.common.ImageResponse
import com.gnoemes.shimori.data.core.entities.common.ShimoriImage
import com.gnoemes.shimori.data.core.mappers.Mapper


internal class ImageResponseMapper : Mapper<ImageResponse, ShimoriImage> {

    override suspend fun map(from: ImageResponse): ShimoriImage =
        ShimoriImage(
            from.original?.appendHostIfNeed(),
            from.preview?.appendHostIfNeed(),
            from.x96?.appendHostIfNeed(),
            from.x48?.appendHostIfNeed()
        )
}