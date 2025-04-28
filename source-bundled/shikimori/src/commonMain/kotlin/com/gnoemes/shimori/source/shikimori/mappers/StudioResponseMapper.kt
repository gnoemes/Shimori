package com.gnoemes.shimori.source.shikimori.mappers

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.source.model.SStudio
import com.gnoemes.shimori.source.shikimori.Shikimori.Companion.appendHostIfNeed
import com.gnoemes.shimori.source.shikimori.ShikimoriValues
import com.gnoemes.shimori.source.shikimori.fragment.Studio
import com.gnoemes.shimori.source.shikimori.models.anime.StudioResponse
import me.tatarka.inject.annotations.Inject

@Inject
class StudioResponseMapper(
    private val values: ShikimoriValues
) : Mapper<StudioResponse, SStudio> {
    
    override fun map(from: StudioResponse): SStudio {
        return SStudio(
            id = from.id,
            name = from.name,
            imageUrl = from.imageUrl?.appendHostIfNeed(values)
        )
    }
}

@Inject
class StudioFragmentMapper : Mapper<Studio, SStudio> {
    
    override fun map(from: Studio): SStudio {
        return SStudio(
            id = from.id.toLong(),
            name = from.name,
            imageUrl = from.imageUrl
        )
    }
}