package com.gnoemes.shimori.sources.shikimori.mappers

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.data.common.TitleStatus
import com.gnoemes.shimori.sources.shikimori.models.common.ContentStatus
import me.tatarka.inject.annotations.Inject

@Inject
class TitleStatusMapper : Mapper<ContentStatus?, TitleStatus?> {

    override fun map(from: ContentStatus?): TitleStatus? {
        if (from == null) return null
        return when (from) {
            ContentStatus.ANONS -> TitleStatus.ANONS
            ContentStatus.ONGOING -> TitleStatus.ONGOING
            ContentStatus.RELEASED -> TitleStatus.RELEASED
            ContentStatus.PAUSED -> TitleStatus.PAUSED
            ContentStatus.DISCONTINUED -> TitleStatus.DISCONTINUED
        }
    }
}