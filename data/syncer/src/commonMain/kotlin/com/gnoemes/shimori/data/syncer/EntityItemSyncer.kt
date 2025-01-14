package com.gnoemes.shimori.data.syncer

import com.gnoemes.shimori.data.ShimoriEntity
import com.gnoemes.shimori.data.db.api.daos.EntityDao
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncer
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncer.Companion.TAG
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncerResult
import com.gnoemes.shimori.logging.api.Logger

/**
 * @param NetworkType Network type
 * @param LocalType local entity type
 * @param Key Network ID type
 */
class EntityItemSyncer<LocalType : ShimoriEntity, NetworkType, Key>(
    private val insertEntity: (LocalType) -> Unit,
    private val updateEntity: (LocalType) -> Unit,
    private val deleteEntity: (LocalType) -> Unit,
    private val localEntityToKey: (LocalType) -> Key?,
    private val networkEntityToKey: (NetworkType) -> Key,
    private val networkEntityToLocalEntity: (NetworkType, LocalType?) -> LocalType,
    private val logger: Logger
) : ItemSyncer<LocalType, NetworkType, Key> {

    override fun sync(
        currentValues: Collection<LocalType>,
        networkValues: Collection<NetworkType>,
        removeNotMatched: Boolean
    ): ItemSyncerResult<LocalType> {
        val currentDbEntities = ArrayList(currentValues)

        val removed = ArrayList<LocalType>()
        val added = ArrayList<LocalType>()
        val updated = ArrayList<LocalType>()

        logger.v(tag = TAG) { "Current DB values size: ${currentDbEntities.size}" }

        for (networkEntity in networkValues) {
            logger.v(tag = TAG) { "Syncing item from network: $networkEntity" }

            val remoteId = networkEntityToKey(networkEntity)
            logger.v(tag = TAG) { "Mapped to remote ID: $remoteId" }
            if (remoteId == null) {
                break
            }

            val dbEntityForId = currentDbEntities.find {
                localEntityToKey(it) == remoteId
            }
            logger.v(tag = TAG) { "Matched database entity for remote ID $remoteId : $dbEntityForId" }

            if (dbEntityForId != null) {
                val entity = networkEntityToLocalEntity(networkEntity, dbEntityForId)
                logger.v(tag = TAG) { "Mapped network entity to local entity: $entity" }
                if (dbEntityForId != entity) {
                    // This is currently in the DB, so lets merge it with the saved version
                    // and update it
                    updateEntity(entity)
                    logger.v(tag = TAG) { "Updated entry with remote id: $remoteId" }
                    updated += entity
                }
                // Remove it from the list so that it is not deleted
                currentDbEntities.remove(dbEntityForId)
            } else {
                // Not currently in the DB, so lets insert
                added += networkEntityToLocalEntity(networkEntity, null)
            }
        }

        if (removeNotMatched) {
            // Anything left in the set needs to be deleted from the database
            currentDbEntities.forEach {
                deleteEntity(it)
                logger.v(tag = TAG) { "Deleted entry: $it" }
                removed += it
            }
        }

        // Finally we can insert all of the new entities
        added.forEach {
            insertEntity(it)
        }

        return ItemSyncerResult(added, removed, updated)
    }

    override fun sync(
        localEntity: LocalType?,
        networkEntity: NetworkType
    ): ItemSyncerResult<LocalType> {
        val removed = ArrayList<LocalType>()
        val added = ArrayList<LocalType>()
        val updated = ArrayList<LocalType>()

        logger.v(tag = TAG) { "Syncing item from network: $networkEntity" }

        val remoteId = networkEntityToKey(networkEntity)
        logger.v(tag = TAG) { "Mapped to remote ID: $remoteId" }
        if (remoteId != null) {
            val dbEntityForId =
                if (localEntity == null) null
                else if (localEntityToKey(localEntity) == remoteId) localEntity
                else null
            logger.v(tag = TAG) { "Matched database entity for remote ID $remoteId : $dbEntityForId" }

            if (dbEntityForId != null) {
                val entity = networkEntityToLocalEntity(networkEntity, dbEntityForId)
                logger.v(tag = TAG) { "Mapped network entity to local entity: $entity" }
                if (dbEntityForId != entity) {
                    // This is currently in the DB, so lets merge it with the saved version
                    // and update it
                    updateEntity(entity)
                    logger.v(tag = TAG) { "Updated entry with remote id: $remoteId" }
                    updated += entity
                }
            } else {
                // Not currently in the DB, so lets insert
                added += networkEntityToLocalEntity(networkEntity, null)
            }
        }

        // Finally we can insert all of the new entities
        added.forEach {
            insertEntity(it)
        }

        return ItemSyncerResult(added, removed, updated)
    }
}

fun <Type : ShimoriEntity, Key> syncerForEntity(
    entityDao: EntityDao<Type>,
    entityToKey: (Type) -> Key?,
    mapper: (Type, Type?) -> Type,
    logger: Logger,
    networkEntityToKey: (Type) -> Key? = entityToKey
) = EntityItemSyncer(
    entityDao::insert,
    entityDao::update,
    entityDao::delete,
    entityToKey,
    networkEntityToKey,
    mapper,
    logger
)
