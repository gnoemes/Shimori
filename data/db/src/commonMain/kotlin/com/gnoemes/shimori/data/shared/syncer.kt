package com.gnoemes.shimori.data.shared

import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.core.database.daos.EntityDao
import com.gnoemes.shimori.data.core.entities.ShimoriEntity

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

        logger.v("Current DB values size: ${currentDbEntities.size}", tag = TAG)

        for (networkEntity in networkValues) {
            logger.v("Syncing item from network: $networkEntity", tag = TAG)

            val remoteId = networkEntityToKey(networkEntity)
            logger.v("Mapped to remote ID: $remoteId", tag = TAG)
            if (remoteId == null) {
                break
            }

            val dbEntityForId = currentDbEntities.find {
                localEntityToKey(it) == remoteId
            }
            logger.v("Matched database entity for remote ID $remoteId : $dbEntityForId", tag = TAG)

            if (dbEntityForId != null) {
                val entity = networkEntityToLocalEntity(networkEntity, dbEntityForId)
                logger.v("Mapped network entity to local entity: $entity", tag = TAG)
                if (dbEntityForId != entity) {
                    // This is currently in the DB, so lets merge it with the saved version
                    // and update it
                    updateEntity(entity)
                    logger.v("Updated entry with remote id: $remoteId", tag = TAG)
                }
                // Remove it from the list so that it is not deleted
                currentDbEntities.remove(dbEntityForId)
                updated += entity
            } else {
                // Not currently in the DB, so lets insert
                added += networkEntityToLocalEntity(networkEntity, null)
            }
        }

        if (removeNotMatched) {
            // Anything left in the set needs to be deleted from the database
            currentDbEntities.forEach {
                deleteEntity(it)
                logger.v("Deleted entry: $it", tag = TAG)
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

data class ItemSyncerResult<ET : ShimoriEntity>(
    val added: List<ET> = emptyList(),
    val deleted: List<ET> = emptyList(),
    val updated: List<ET> = emptyList()
)

internal fun <Type : ShimoriEntity, Key> syncerForEntity(
    entityDao: EntityDao<Type>,
    entityToKey: suspend (Type) -> Key?,
    mapper: suspend (Type, Type?) -> Type,
    logger: Logger
) = ItemSyncer(
    entityDao::insert,
    entityDao::update,
    entityDao::delete,
    entityToKey,
    entityToKey,
    mapper,
    logger
)