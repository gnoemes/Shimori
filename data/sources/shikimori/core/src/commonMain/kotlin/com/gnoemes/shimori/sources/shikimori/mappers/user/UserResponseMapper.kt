package com.gnoemes.shimori.sources.shikimori.mappers.user

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.data.common.ShimoriImage
import com.gnoemes.shimori.data.user.User
import com.gnoemes.shimori.sources.shikimori.models.user.UserDetailsResponse
import me.tatarka.inject.annotations.Inject

@Inject
class UserResponseMapper constructor() : Mapper<UserDetailsResponse, User> {

    override fun map(from: UserDetailsResponse): User {
        val image = ShimoriImage(from.image.x160, from.image.x148, from.image.x80, from.image.x48)
        val commonInfo = convertCommonInfo(from.commonInfo)

        return User(
            id = 0,
            remoteId = from.id,
            sourceId = 1,
            nickname = from.nickname,
            image = image,
            name = from.name,
            about = from.about,
            commonInfo = commonInfo,
            sex = from.sex,
            website = from.website,
            dateBirth = from.dateBirth,
            locale = from.locale,
            fullYears = from.fullYears,
            location = from.location,
            showComments = from.isShowComments,
            friend = from.isFriend,
            ignored = from.isIgnored,
            banned = from.isBanned,
            lastOnlineAt = from.dateLastOnline
        )
    }

    private fun convertCommonInfo(commonInfo: List<String>): String {
        val DELIMITER = " / "
        val builder = StringBuilder()
        commonInfo.forEach { builder.append(it).append(DELIMITER) }

        builder.replaceRange(builder.lastIndexOf(DELIMITER), builder.length - 1, "")
        return builder.toString()
            .replace("<.[spn].+?>", "")
            .replace("(class.\".+?\" )", "")
    }
}