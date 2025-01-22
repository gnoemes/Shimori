package com.gnoemes.shimori.data.syncer

import com.gnoemes.shimori.data.ShimoriEntity
import com.gnoemes.shimori.data.app.SourceDataType
import com.gnoemes.shimori.data.db.api.daos.EntityDao
import com.gnoemes.shimori.data.db.api.daos.SourceIdsSyncDao
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncer
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncer.Companion.TAG
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncerResult
import com.gnoemes.shimori.logging.api.Logger


/**
 * @param NetworkType Network type
 * @param LocalType local entity type
 * @param Key Network ID type
 */
class SourceItemSyncer<LocalType : ShimoriEntity, NetworkType, Key>(
    private val syncDao: SourceIdsSyncDao,
    private val sourceDataType: SourceDataType,
    private val sourceId: Long,
    private val insertEntity: (LocalType) -> Long,
    private val updateEntity: (LocalType) -> Unit,
    private val deleteEntity: (LocalType) -> Unit,
    private val localEntityToKey: (Long, LocalType) -> Key?,
    private val networkEntityToKey: (Long, NetworkType) -> Key,
    private val networkToId: (NetworkType) -> Long,
    private val networkEntityToLocalEntity: (Long, NetworkType, LocalType?) -> LocalType,
    private val logger: Logger
) : ItemSyncer<LocalType, NetworkType, Key> {

    override fun sync(
        currentValues: Collection<LocalType>,
        networkValues: Collection<NetworkType>,
        removeNotMatched: Boolean
    ): ItemSyncerResult<LocalType> {
        val currentDbEntities = ArrayList(currentValues)

        val removed = ArrayList<LocalType>()
        val addedNetwork = ArrayList<NetworkType>()
        val added = ArrayList<LocalType>()
        val updated = ArrayList<LocalType>()

        logger.v(tag = TAG) { "Current DB values size: ${currentDbEntities.size}" }
        logger.v(tag = TAG) { "Network values to sync: ${networkValues.size}" }

        for (networkEntity in networkValues) {
            logger.v(tag = TAG) { "Syncing item from network: $networkEntity" }

            val remoteId = networkEntityToKey(sourceId, networkEntity)
            logger.v(tag = TAG) { "Mapped to remote ID: $remoteId" }
            if (remoteId == null) {
                break
            }

            val dbEntityForId = currentDbEntities.find {
                localEntityToKey(sourceId, it) == remoteId
            }
            logger.v(tag = TAG) { "Matched database entity for remote ID $remoteId : $dbEntityForId" }

            if (dbEntityForId != null) {
                val entity = networkEntityToLocalEntity(sourceId, networkEntity, dbEntityForId)
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
                addedNetwork += networkEntity
            }
        }

        if (removeNotMatched) {
            // Anything left in the set needs to be deleted from the database
            currentDbEntities.forEach {
                deleteEntity(it)
                logger.v(tag = TAG) { "Deleted entry: $it" }
                removed += it
                syncDao.deleteByLocalId(sourceId, it.id, sourceDataType)
            }
        }

        // Finally we can insert all of the new entities
        addedNetwork.forEach {
            val localEntity = networkEntityToLocalEntity(sourceId, it, null)
            val id = insertEntity(localEntity)
            logger.v(tag = TAG) { "Added entry: $localEntity" }
            syncDao.syncRemoteIds(
                sourceId,
                id,
                networkToId(it),
                sourceDataType
            )
            added += localEntity
        }

        return ItemSyncerResult(added, removed, updated)
    }

    override fun sync(
        localEntity: LocalType?,
        networkEntity: NetworkType
    ): ItemSyncerResult<LocalType> {
        val removed = ArrayList<LocalType>()
        val addedNetwork = ArrayList<NetworkType>()
        val added = ArrayList<LocalType>()
        val updated = ArrayList<LocalType>()

        logger.v(tag = TAG) { "Syncing item from network: $networkEntity" }

        val remoteId = networkEntityToKey(sourceId, networkEntity)
        logger.v(tag = TAG) { "Mapped to remote ID: $remoteId" }
        if (remoteId != null) {
            val dbEntityForId =
                if (localEntity == null) null
                else if (localEntityToKey(sourceId, localEntity) == remoteId) localEntity
                else null
            logger.v(tag = TAG) { "Matched database entity for remote ID $remoteId : $dbEntityForId" }

            if (dbEntityForId != null) {
                val entity = networkEntityToLocalEntity(sourceId, networkEntity, dbEntityForId)
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
                addedNetwork += networkEntity
            }
        }

        // Finally we can insert all of the new entities
        addedNetwork.forEach {
            val localEntity = networkEntityToLocalEntity(sourceId, it, null)
            val id = insertEntity(localEntity)
            syncDao.syncRemoteIds(
                sourceId,
                id,
                networkToId(it),
                sourceDataType
            )
            added += localEntity
        }

        return ItemSyncerResult(added, removed, updated)
    }
}

fun <Type : ShimoriEntity, Key> syncerForEntity(
    syncDao: SourceIdsSyncDao,
    sourceDataType: SourceDataType,
    sourceId: Long,
    entityDao: EntityDao<Type>,
    entityToKey: (Long, Type) -> Key?,
    networkEntityToKey: (Long, Type) -> Key?,
    networkToId: (Type) -> Long,
    mapper: (Long, Type, Type?) -> Type,
    logger: Logger
) = SourceItemSyncer(
    syncDao,
    sourceDataType,
    sourceId,
    entityDao::insert,
    entityDao::update,
    entityDao::delete,
    entityToKey,
    networkEntityToKey,
    networkToId,
    mapper,
    logger
)