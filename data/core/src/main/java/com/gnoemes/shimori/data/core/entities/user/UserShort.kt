package com.gnoemes.shimori.data.core.entities.user

import com.gnoemes.shimori.data.core.entities.ShikimoriEntity
import com.gnoemes.shimori.data.core.entities.ShimoriEntity
import com.gnoemes.shimori.data.core.entities.common.ShimoriImage

data class UserShort(
    override val id: Long = 0,
    override val shikimoriId: Long = 0,
    val nickname: String = "",
    val image : ShimoriImage? = null,
    val isMe : Boolean = false,
) : ShimoriEntity, ShikimoriEntity