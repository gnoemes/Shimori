package com.gnoemes.shimori.data.db.api.daos

import com.gnoemes.shimori.data.characters.CharacterRole

abstract class CharacterRoleDao : EntityDao<CharacterRole> {
    abstract fun queryByCharacterId(id: Long): List<CharacterRole>

}