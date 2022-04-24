package com.gnoemes.shimori.data.base.entities.user

import com.gnoemes.shimori.data.base.entities.ShikimoriEntity
import com.gnoemes.shimori.data.base.entities.ShimoriEntity
import com.gnoemes.shimori.data.base.entities.common.ShimoriImage
import kotlinx.datetime.Instant

data class User(
    override val id: Long = 0,
    override val shikimoriId: Long = 0,
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
) : ShimoriEntity, ShikimoriEntity {
    fun isSameShikimoriUser(other: User) =
        shikimoriId != 0L && shikimoriId == other.shikimoriId
}