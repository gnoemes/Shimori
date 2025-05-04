package com.gnoemes.shimori.data.db.api.daos

import com.gnoemes.shimori.data.person.PersonRole
import com.gnoemes.shimori.data.track.TrackTargetType

interface PersonRoleDao : EntityDao<PersonRole> {
    fun queryByPersonId(id: Long): List<PersonRole>
    fun queryByTitle(id: Long, type: TrackTargetType): List<PersonRole>

}