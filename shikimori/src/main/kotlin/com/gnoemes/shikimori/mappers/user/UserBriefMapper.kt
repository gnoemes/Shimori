package com.gnoemes.shikimori.mappers.user

import com.gnoemes.shikimori.ShikimoriSource
import com.gnoemes.shikimori.entities.user.UserBriefResponse
import com.gnoemes.shimori.data.core.entities.common.ShimoriImage
import com.gnoemes.shimori.data.core.entities.user.User
import com.gnoemes.shimori.data.core.mappers.Mapper

internal class UserBriefMapper : Mapper<UserBriefResponse, User> {

    override suspend fun map(from: UserBriefResponse): User {
        val image = ShimoriImage(from.image.x160, from.image.x148, from.image.x80, from.image.x48)

        return User(
            id = 0,
            remoteId = from.id,
            sourceId = ShikimoriSource.ID,
            nickname = from.nickname,
            image = image,
            name = from.name,
            sex = from.sex,
            website = from.website,
            dateBirth = from.dateBirth,
            locale = from.locale,
            lastOnlineAt = from.dateLastOnline
        )
    }
}