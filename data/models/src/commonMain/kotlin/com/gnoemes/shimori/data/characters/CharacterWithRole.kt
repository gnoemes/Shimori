package com.gnoemes.shimori.data.characters

import com.gnoemes.shimori.data.PaginatedEntity
import com.gnoemes.shimori.data.ShimoriEntity

@kotlinx.serialization.Serializable
data class CharacterWithRole(
    val entity: Character,
    val role: CharacterRole,
) : ShimoriEntity, PaginatedEntity {
    override val id: Long = entity.id
}