package com.gnoemes.shimori.data.source.mapper

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.data.common.Studio
import com.gnoemes.shimori.source.model.SStudio
import me.tatarka.inject.annotations.Inject

@Inject
class SourceStudioMapper : Mapper<SStudio, Studio> {
    override fun map(from: SStudio): Studio {
        return Studio(
            id = from.id,
            sourceId = -1,
            name = from.name,
            imageUrl = from.imageUrl
        )
    }
}