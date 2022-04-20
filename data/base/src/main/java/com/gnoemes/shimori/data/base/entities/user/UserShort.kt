package com.gnoemes.shimori.data.base.entities.user

import com.gnoemes.shimori.data.base.entities.ShikimoriEntity
import com.gnoemes.shimori.data.base.entities.ShimoriEntity
import com.gnoemes.shimori.data.base.entities.common.ShimoriImage

data class UserShort(
    override val id: Long = 0,
    override val shikimoriId: Long = 0,
    val nickname: String = "",
    val image : ShimoriImage? = null,
    val isMe : Boolean = false,
) : ShimoriEntity, ShikimoriEntity