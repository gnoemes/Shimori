package com.gnoemes.shimori.data.person

import com.gnoemes.shimori.data.ShimoriEntity

@kotlinx.serialization.Serializable
data class PersonSeyuRole(
    override val id: Long = 0,
    val personId: Long,
    val characterId: Long,
) : ShimoriEntity