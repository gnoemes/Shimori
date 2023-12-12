package com.gnoemes.shimori.data.shikimori.mappers

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.data.common.ShimoriImage
import com.gnoemes.shimori.data.shikimori.Shikimori.Companion.appendHostIfNeed
import com.gnoemes.shimori.data.shikimori.ShikimoriValues
import com.gnoemes.shimori.data.shikimori.models.common.ImageResponse
import me.tatarka.inject.annotations.Inject


@Inject
class ImageResponseMapper(
    private val values: ShikimoriValues,
) : Mapper<ImageResponse, ShimoriImage> {

    override fun map(from: ImageResponse): ShimoriImage =
        ShimoriImage(
            from.original?.appendHostIfNeed(values),
            from.preview?.appendHostIfNeed(values),
            from.x96?.appendHostIfNeed(values),
            from.x48?.appendHostIfNeed(values)
        )
}