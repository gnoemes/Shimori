package com.gnoemes.shikimori.mappers.roles

import com.gnoemes.shikimori.entities.common.RolesResponse
import com.gnoemes.shimori.data.core.entities.roles.RolesInfo
import com.gnoemes.shimori.data.core.mappers.Mapper

internal class RolesMapper(
    private val characterResponseMapper: CharacterResponseMapper,
) : Mapper<List<RolesResponse>, RolesInfo> {

    override suspend fun map(from: List<RolesResponse>): RolesInfo {
        return RolesInfo(
            characters = from.mapNotNull {
                it.character?.let { character ->
                    characterResponseMapper.map(character)
                }
            }
        )
    }
}