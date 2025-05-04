package com.gnoemes.shimori.data.source.mapper

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.data.person.PersonRole
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.source.model.SPersonRole
import me.tatarka.inject.annotations.Inject

@Inject
class SourcePersonRoleMapper : Mapper<SPersonRole?, PersonRole?> {

    override fun map(from: SPersonRole?): PersonRole? {
        if (from == null) return null

        return TrackTargetType.find(from.targetType)?.let {
            PersonRole(
                id = from.id,
                personId = from.personId,
                targetType = it,
                targetId = from.targetId,
                role = from.role,
                roleRu = from.roleRu,
            )

        }
    }
}