package com.gnoemes.shikimori.mappers.user

import com.gnoemes.shikimori.entities.user.UserDetailsResponse
import com.gnoemes.shimori.data_base.mappers.Mapper
import com.gnoemes.shimori.model.common.ShimoriImage
import com.gnoemes.shimori.model.user.User
import javax.inject.Inject

internal class UserResponseMapper @Inject constructor() : Mapper<UserDetailsResponse, User> {

    override suspend fun map(from: UserDetailsResponse): User {
        val image = ShimoriImage(from.image.x160, from.image.x148, from.image.x80, from.image.x48)
        val commonInfo = convertCommonInfo(from.commonInfo)

        return User(
                shikimoriId = from.id,
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

        builder.replace(builder.lastIndexOf(DELIMITER), builder.length - 1, "")
        return builder.toString()
            .replace("<.[spn].+?>", "")
            .replace("(class.\".+?\" )", "")

    }
}