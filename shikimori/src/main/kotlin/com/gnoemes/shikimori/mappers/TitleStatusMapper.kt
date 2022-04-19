package com.gnoemes.shikimori.mappers

import com.gnoemes.shikimori.entities.common.ContentStatus
import com.gnoemes.shimori.data.base.entities.common.TitleStatus
import com.gnoemes.shimori.data.base.mappers.Mapper

class TitleStatusMapper : Mapper<ContentStatus?, TitleStatus?> {

    override suspend fun map(from: ContentStatus?): TitleStatus? {
        if (from == null) return null
        return when (from) {
            ContentStatus.ANONS -> TitleStatus.ANONS
            ContentStatus.ONGOING -> TitleStatus.ONGOING
            ContentStatus.RELEASED -> TitleStatus.RELEASED
        }
    }
}