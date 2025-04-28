package com.gnoemes.shimori.data.daos

import com.gnoemes.shimori.ShimoriDB
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.characters.CharacterRole
import com.gnoemes.shimori.data.db.api.daos.CharacterRoleDao
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.data.util.characterRole
import com.gnoemes.shimori.logging.api.Logger
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class, boundType = CharacterRoleDao::class)
class CharacterRoleDaoImpl(
    override val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers
) : CharacterRoleDao(), SqlDelightEntityDao<CharacterRole> {
    override fun insert(entity: CharacterRole): Long {
        entity.let {
            db.characterRoleQueries.insert(
                it.characterId,
                it.targetId,
                it.targetType,
                it.role,
                it.roleRu
            )
        }

        return db.characterRoleQueries.selectLastInsertedRowId().executeAsOne()
    }

    override fun update(entity: CharacterRole) {
        entity.let {
            db.characterRoleQueries.update(
                it.id,
                it.characterId,
                it.targetId,
                it.targetType,
                it.role,
                it.roleRu
            )
        }
    }

    override fun delete(entity: CharacterRole) {
        return db.characterRoleQueries.deleteById(entity.id)
    }

    override fun queryByCharacterId(id: Long): List<CharacterRole> {
        return db.characterRoleQueries.queryByCharacterId(id, ::characterRole).executeAsList()
    }

    override fun queryByTitle(id: Long, type: TrackTargetType): List<CharacterRole> {
        return db.characterRoleQueries.queryByTitle(id, type, ::characterRole).executeAsList()
    }
}