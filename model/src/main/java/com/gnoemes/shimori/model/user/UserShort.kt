package com.gnoemes.shimori.model.user

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.gnoemes.shimori.model.ShikimoriEntity
import com.gnoemes.shimori.model.ShimoriEntity
import com.gnoemes.shimori.model.common.ShimoriImage

data class UserShort(
    override val id: Long,
    @ColumnInfo(name = "shikimori_id") override val shikimoriId: Long?,
    val nickname: String,
    @Embedded(prefix = "image_") val image: ShimoriImage?
) : ShimoriEntity, ShikimoriEntity {

    val exists: Boolean
        get() = shikimoriId?.let { true } ?: false
}

fun UserShort?.exists(): Boolean = this != null && exists