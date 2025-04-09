package com.gnoemes.shimori.source.shikimori.mappers

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.source.shikimori.models.common.ContentStatus
import me.tatarka.inject.annotations.Inject

@Inject
class TitleStatusMapper : Mapper<ContentStatus?, String?> {

    override fun map(from: ContentStatus?): String? {
        if (from == null) return null
        return from.status
    }
}