package com.gnoemes.shimori.data.base.entities.user

import com.gnoemes.shimori.data.base.entities.ShikimoriEntity
import com.gnoemes.shimori.data.base.entities.ShimoriEntity
import com.gnoemes.shimori.data.base.entities.common.ShimoriImage

data class UserShort(
    override val id: Long = 0,
    override val shikimoriId: Long = 0,
    val nickname: String = "",
    private val imageOriginal: String? = null,
    private val imagePreview: String? = null
) : ShimoriEntity, ShikimoriEntity {
    val image = ShimoriImage(
        original = imageOriginal,
        preview = imagePreview
    )
}