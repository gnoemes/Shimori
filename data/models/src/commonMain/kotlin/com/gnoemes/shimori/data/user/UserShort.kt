package com.gnoemes.shimori.data.user

import com.gnoemes.shimori.data.ShimoriEntity
import com.gnoemes.shimori.data.common.ShimoriImage

data class UserShort(
    override val id: Long = 0,
    val remoteId : Long,
    val sourceId: Long,
    val nickname: String = "",
    val image: ShimoriImage? = null,
    val isMe: Boolean = false,
) : ShimoriEntity {
    val isLocal get() = sourceId == -1L
}