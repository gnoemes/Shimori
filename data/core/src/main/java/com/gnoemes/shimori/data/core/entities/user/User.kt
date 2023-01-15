package com.gnoemes.shimori.data.core.entities.user

import com.gnoemes.shimori.data.core.entities.ShimoriEntity
import com.gnoemes.shimori.data.core.entities.common.ShimoriImage
import kotlinx.datetime.Instant

data class User(
    override val id: Long = 0,
    val remoteId: Long,
    val sourceId: Long,
    val nickname: String = "",
    val image: ShimoriImage? = null,
    val name: String? = null,
    val about: String? = null,
    val commonInfo: String? = null,
    val sex: String? = null,
    val website: String? = null,
    val dateBirth: String? = null,
    val locale: String? = null,
    val fullYears: Int? = null,
    val location: String? = null,
    val showComments: Boolean = false,
    val friend: Boolean = false,
    val ignored: Boolean = false,
    val banned: Boolean = false,
    val lastOnlineAt: Instant? = null,
    val isMe: Boolean = false
) : ShimoriEntity {
    val isLocal get() = sourceId == -1L

    fun User.toUserShort() = UserShort(
        id = id,
        remoteId = remoteId,
        sourceId = sourceId,
        nickname = nickname,
        image = image,
        isMe = isMe
    )

    companion object {
        fun createLocalUser() = User(sourceId = -1, isMe = true, remoteId = -1)
    }
}