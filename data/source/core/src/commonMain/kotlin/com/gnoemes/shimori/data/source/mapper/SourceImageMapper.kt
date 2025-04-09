package com.gnoemes.shimori.data.source.mapper

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.data.common.ShimoriImage
import com.gnoemes.shimori.source.model.SImage
import me.tatarka.inject.annotations.Inject

@Inject
class SourceImageMapper : Mapper<SImage, ShimoriImage> {

    override fun map(from: SImage): ShimoriImage {
        return ShimoriImage(
            original = from.original,
            preview = from.preview,
            x96 = from.x96,
            x48 = from.x48
        )
    }

}