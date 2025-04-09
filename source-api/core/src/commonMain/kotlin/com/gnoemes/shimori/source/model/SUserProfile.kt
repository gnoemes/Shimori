package com.gnoemes.shimori.source.model

import kotlinx.datetime.Instant

data class SUserProfile(
    val id: Long = 0,
    val remoteId: Long,
    val sourceId: Long,
    val nickname: String = "",
    val image: SImage? = null,
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
)