package com.gnoemes.shimori.data.source.mapper

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.data.user.User
import com.gnoemes.shimori.source.model.SUserProfile
import me.tatarka.inject.annotations.Inject

@Inject
class SourceUserProfileMapper(
    private val imageMapper: SourceImageMapper
) : Mapper<SUserProfile, User> {

    override fun map(from: SUserProfile): User {
        return User(
            id = from.id,
            remoteId = from.remoteId,
            sourceId = from.sourceId,
            nickname = from.nickname,
            image = from.image?.let { imageMapper.map(it) },
            name = from.name,
            about = from.about,
            commonInfo = from.commonInfo,
            sex = from.sex,
            website = from.website,
            dateBirth = from.dateBirth,
            locale = from.locale,
            fullYears = from.fullYears,
            location = from.location,
            showComments = from.showComments,
            friend = from.friend,
            ignored = from.ignored,
            banned = from.banned,
            lastOnlineAt = from.lastOnlineAt,
            isMe = from.isMe
        )
    }
}