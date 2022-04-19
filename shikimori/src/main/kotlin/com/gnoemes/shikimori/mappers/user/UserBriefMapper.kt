package com.gnoemes.shikimori.mappers.user

import com.gnoemes.shikimori.entities.user.UserBriefResponse
import com.gnoemes.shimori.data.base.entities.common.ShimoriImage
import com.gnoemes.shimori.data.base.entities.user.User
import com.gnoemes.shimori.data.base.mappers.Mapper

internal class UserBriefMapper : Mapper<UserBriefResponse, User> {

    override suspend fun map(from: UserBriefResponse): User {
        val image = ShimoriImage(from.image.x160, from.image.x148, from.image.x80, from.image.x48)

        return User(
            shikimoriId = from.id,
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