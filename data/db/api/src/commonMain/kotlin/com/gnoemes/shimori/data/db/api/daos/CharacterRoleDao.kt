package com.gnoemes.shimori.data.db.api.daos

import com.gnoemes.shimori.data.characters.CharacterRole
import com.gnoemes.shimori.data.track.TrackTargetType

abstract class CharacterRoleDao : EntityDao<CharacterRole> {
    abstract fun queryByCharacterId(id: Long): List<CharacterRole>
    abstract fun queryByTitle(id: Long, type : TrackTargetType): List<CharacterRole>

}