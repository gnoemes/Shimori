package com.gnoemes.shimori.data.daos

import com.gnoemes.shimori.ShimoriDB
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.db.api.daos.PersonRoleDao
import com.gnoemes.shimori.data.person.PersonRole
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.logging.api.Logger
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class, boundType = PersonRoleDao::class)
class PersonRoleDaoImpl(
    override val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers
) : PersonRoleDao, SqlDelightEntityDao<PersonRole> {
    override fun insert(entity: PersonRole): Long {
        entity.let {
            db.personRoleQueries.insert(
                it.personId,
                it.targetId,
                it.targetType,
                it.role,
                it.roleRu
            )
        }

        return db.personRoleQueries.selectLastInsertedRowId().executeAsOne()
    }

    override fun update(entity: PersonRole) {
        entity.let {
            db.personRoleQueries.update(
                it.id,
                it.personId,
                it.targetId,
                it.targetType,
                it.role,
                it.roleRu
            )
        }
    }

    override fun delete(entity: PersonRole) {
        return db.personRoleQueries.deleteById(entity.id)
    }

    override fun queryByPersonId(id: Long): List<PersonRole> {
        return db.personRoleQueries.queryByPersonId(id, ::PersonRole).executeAsList()
    }

    override fun queryByTitle(id: Long, type: TrackTargetType): List<PersonRole> {
        return db.personRoleQueries.queryByTitle(id, type, ::PersonRole).executeAsList()
    }
}