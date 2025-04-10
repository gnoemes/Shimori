package com.gnoemes.shimori.data.source.mapper

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.data.characters.CharacterRole
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.source.model.SCharacterRole
import me.tatarka.inject.annotations.Inject

@Inject
class SourceCharacterRoleMapper : Mapper<SCharacterRole?, CharacterRole?> {

    override fun map(from: SCharacterRole?): CharacterRole? {
        if (from == null) return null

        return TrackTargetType.find(from.targetType)?.let {
            CharacterRole(
                id = from.id,
                characterId = from.characterId,
                targetType = it,
                targetId = from.targetId
            )

        }
    }
}