package com.gnoemes.shimori.sources.shikimori.mappers.user

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.data.common.ShimoriImage
import com.gnoemes.shimori.data.user.User
import com.gnoemes.shimori.sources.shikimori.models.user.UserBriefResponse
import me.tatarka.inject.annotations.Inject

@Inject
class UserBriefMapper : Mapper<UserBriefResponse, User> {

    override fun map(from: UserBriefResponse): User {
        val image = ShimoriImage(from.image.x160, from.image.x148, from.image.x80, from.image.x48)

        return User(
            id = 0,
            sourceId = 1,
            remoteId = from.id,
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