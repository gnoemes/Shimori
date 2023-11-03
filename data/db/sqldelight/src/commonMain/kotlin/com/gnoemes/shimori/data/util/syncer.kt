package com.gnoemes.shimori.data.util

import com.gnoemes.shimori.data.ShimoriEntity
import com.gnoemes.shimori.data.db.api.daos.EntityDao
import com.gnoemes.shimori.data.db.api.daos.SourceSyncEntityDao
import com.gnoemes.shimori.logging.api.Logger

/**
 * @param NetworkType Network type
 * @param LocalType local entity type
 * @param Key Network ID type
 */
internal class ItemSyncer<LocalType : ShimoriEntity, NetworkType, Key>(
    private val insertEntity: suspend (LocalType) -> Unit,
    private val updateEntity: suspend (LocalType) -> Unit,
    private val deleteEntity: suspend (LocalType) -> Unit,
    private val localEntityToKey: suspend (LocalType) -> Key?,
    private val networkEntityToKey: suspend (NetworkType) -> Key,
    private val networkEntityToLocalEntity: suspend (NetworkType, LocalType?) -> LocalType,
    private val logger: Logger
) {

    private companion object {
        const val TAG = "ItemSyncer"
    }

    suspend fun sync(
        currentValues: Collection<LocalType>,
        networkValues: Collection<NetworkType>,
        removeNotMatched: Boolean = true
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
}

/**
 * @param NetworkType Network type
 * @param LocalType local entity type
 * @param Key Network ID type
 */
internal class SourceItemSyncer<LocalType : ShimoriEntity, NetworkType, Key>(
    private val insertEntity: suspend (Long, LocalType) -> Unit,
    private val updateEntity: suspend (Long, NetworkType, LocalType) -> Unit,
    private val deleteEntity: suspend (Long, LocalType) -> Unit,
    private val localEntityToKey: suspend (Long, LocalType) -> Key?,
    private val networkEntityToKey: suspend (Long, NetworkType) -> Key,
    private val networkEntityToLocalEntity: suspend (Long, NetworkType, LocalType?) -> LocalType,
    private val logger: Logger
) {

    private companion object {
        const val TAG = "ItemSyncer"
    }

    suspend fun sync(
        sourceId: Long,
        currentValues: Collection<LocalType>,
        networkValues: Collection<NetworkType>,
        removeNotMatched: Boolean = true
    ): ItemSyncerResult<LocalType> {
        val currentDbEntities = ArrayList(currentValues)

        val removed = ArrayList<LocalType>()
        val added = ArrayList<LocalType>()
        val updated = ArrayList<LocalType>()

        logger.v(tag = TAG) { "Current DB values size: ${currentDbEntities.size}" }

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
                    updateEntity(sourceId, networkEntity, dbEntityForId)
                    logger.v(tag = TAG) { "Updated entry with remote id: $remoteId" }
                    updated += entity
                }
                // Remove it from the list so that it is not deleted
                currentDbEntities.remove(dbEntityForId)
            } else {
                // Not currently in the DB, so lets insert
                added += networkEntityToLocalEntity(sourceId, networkEntity, null)
            }
        }

        if (removeNotMatched) {
            // Anything left in the set needs to be deleted from the database
            currentDbEntities.forEach {
                deleteEntity(sourceId, it)
                logger.v(tag = TAG) { "Deleted entry: $it" }
                removed += it
            }
        }

        // Finally we can insert all of the new entities
        added.forEach {
            insertEntity(sourceId, it)
        }

        return ItemSyncerResult(added, removed, updated)
    }
}

data class ItemSyncerResult<ET : ShimoriEntity>(
    val added: List<ET> = emptyList(),
    val deleted: List<ET> = emptyList(),
    val updated: List<ET> = emptyList()
)

internal fun <Type : ShimoriEntity, Key> syncerForEntity(
    entityDao: SourceSyncEntityDao<Type>,
    entityToKey: suspend (Long, Type) -> Key?,
    mapper: suspend (Long, Type, Type?) -> Type,
    logger: Logger,
    networkEntityToKey: suspend (Long, Type) -> Key? = entityToKey
) = SourceItemSyncer(
    entityDao::insert,
    entityDao::update,
    entityDao::delete,
    entityToKey,
    networkEntityToKey,
    mapper,
    logger
)

internal fun <Type : ShimoriEntity, Key> syncerForEntity(
    entityDao: EntityDao<Type>,
    entityToKey: suspend (Type) -> Key?,
    mapper: suspend (Type, Type?) -> Type,
    logger: Logger,
    networkEntityToKey: suspend (Type) -> Key? = entityToKey
) = ItemSyncer(
    entityDao::insert,
    entityDao::update,
    entityDao::delete,
    entityToKey,
    networkEntityToKey,
    mapper,
    logger
)

const val SYNCER_RESULT_TAG = "SyncResult"