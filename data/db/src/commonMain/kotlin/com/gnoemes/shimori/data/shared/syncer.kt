package com.gnoemes.shimori.data.shared

import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.core.database.daos.SourceSyncEntityDao
import com.gnoemes.shimori.data.core.entities.ShimoriEntity

/**
 * @param NetworkType Network type
 * @param LocalType local entity type
 * @param Key Network ID type
 */
internal class ItemSyncer<LocalType : ShimoriEntity, NetworkType, Key>(
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

        logger.v("Current DB values size: ${currentDbEntities.size}", tag = TAG)

        for (networkEntity in networkValues) {
            logger.v("Syncing item from network: $networkEntity", tag = TAG)

            val remoteId = networkEntityToKey(sourceId, networkEntity)
            logger.v("Mapped to remote ID: $remoteId", tag = TAG)
            if (remoteId == null) {
                break
            }

            val dbEntityForId = currentDbEntities.find {
                localEntityToKey(sourceId, it) == remoteId
            }
            logger.v("Matched database entity for remote ID $remoteId : $dbEntityForId", tag = TAG)

            if (dbEntityForId != null) {
                val entity = networkEntityToLocalEntity(sourceId, networkEntity, dbEntityForId)
                logger.v("Mapped network entity to local entity: $entity", tag = TAG)
                if (dbEntityForId != entity) {
                    // This is currently in the DB, so lets merge it with the saved version
                    // and update it
                    updateEntity(sourceId, networkEntity, dbEntityForId)
                    logger.v("Updated entry with remote id: $remoteId", tag = TAG)
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
                logger.v("Deleted entry: $it", tag = TAG)
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