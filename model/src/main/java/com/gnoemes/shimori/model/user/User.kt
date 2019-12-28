package com.gnoemes.shimori.model.user

import androidx.room.*
import com.gnoemes.shimori.model.ShikimoriEntity
import com.gnoemes.shimori.model.ShimoriEntity
import com.gnoemes.shimori.model.common.ShimoriImage
import org.joda.time.DateTime

@Entity(tableName = "users",
        indices = [
            Index(value = ["shikimori_id"], unique = true),
            Index(value = ["is_me"])
        ])
data class User(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo(name = "shikimori_id") override val shikimoriId: Long? = null,
    val nickname: String = "",
    @Embedded(prefix = "image_") val image: ShimoriImage? = null,
    val name: String? = null,
    val about: String? = null,
    @ColumnInfo(name = "common_info") val commonInfo: String? = null,
    val sex: String? = null,
    val website: String? = null,
    @ColumnInfo(name = "date_birth") val dateBirth: DateTime? = null,
    val locale: String? = null,
    @ColumnInfo(name = "full_years") val fullYears: Int? = null,
    val location: String? = null,
    @ColumnInfo(name = "show_comments") val showComments: Boolean = false,
    val friend: Boolean = false,
    val ignored: Boolean = false,
    val banned: Boolean = false,
    @ColumnInfo(name = "last_online") val lastOnlineAt: DateTime? = null,
    @ColumnInfo(name = "is_me") val isMe: Boolean = false
) : ShimoriEntity, ShikimoriEntity