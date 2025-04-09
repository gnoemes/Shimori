package com.gnoemes.shimori.source.shikimori.mappers

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.source.model.SImage
import com.gnoemes.shimori.source.shikimori.Shikimori.Companion.appendHostIfNeed
import com.gnoemes.shimori.source.shikimori.ShikimoriValues
import com.gnoemes.shimori.source.shikimori.models.common.ImageResponse
import me.tatarka.inject.annotations.Inject


@Inject
class ImageResponseMapper(
    private val values: ShikimoriValues,
) : Mapper<ImageResponse, SImage> {

    override fun map(from: ImageResponse): SImage =
        SImage(
            from.original?.appendHostIfNeed(values),
            from.preview?.appendHostIfNeed(values),
            from.x96?.appendHostIfNeed(values),
            from.x48?.appendHostIfNeed(values)
        )
}