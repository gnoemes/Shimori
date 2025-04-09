package com.gnoemes.shimori.source.shikimori.mappers.user

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.source.model.SImage
import com.gnoemes.shimori.source.model.SUserProfile
import com.gnoemes.shimori.source.shikimori.models.user.UserBriefResponse
import me.tatarka.inject.annotations.Inject

@Inject
class UserBriefMapper(

) : Mapper<UserBriefResponse, SUserProfile> {

    override fun map(from: UserBriefResponse): SUserProfile {
        val image = SImage(from.image.x160, from.image.x148, from.image.x80, from.image.x48)

        return SUserProfile(
            id = 0,
            //TODO pass with di
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