package com.gnoemes.shimori.data.sync

import android.util.Log
import com.gnoemes.shimori.data.daos.EntityDao
import com.gnoemes.shimori.model.ShimoriEntity

/**
 * @param NT Network type
 * @param LocalType local entity type
 * @param NID Network ID type
 */
class EntitySyncer<LocalType : ShimoriEntity, NetworkType, Key>(
    private val insertEntity: suspend (LocalType) -> Long,
    private val updateEntity: suspend (LocalType) -> Unit,
    private val deleteEntity: suspend (LocalType) -> Int,
    private val localEntityToKey: suspend (LocalType) -> Key?,
    private val networkEntityToKey: suspend (NetworkType) -> Key,
    private val networkEntityToLocalEntity: suspend (NetworkType, LocalType?) -> LocalType,
) {

    suspend fun sync(
        currentValues: Collection<LocalType>,
        networkValues: Collection<NetworkType>,
        removeNotMatched: Boolean = true
    ): EntitySyncerResult<LocalType> {
        val localValues = currentValues.toMutableList()

        val added = mutableListOf<LocalType>()
        val updated = mutableListOf<LocalType>()
        val removed = mutableListOf<LocalType>()

        for (networkEntity in networkValues) {
            val remoteId = networkEntityToKey(networkEntity) ?: break
            val localEntity = localValues.find { localEntityToKey(it) == remoteId }

            if (localEntity != null) {
                val entity = networkEntityToLocalEntity(networkEntity, localEntity)
                if (localEntity != entity) {
                    Log.i("Syncer", "updating: $localEntity \n $entity")
                    updateEntity(entity)
                    updated += entity
                }
                localValues -= localEntity
            } else {
                added += networkEntityToLocalEntity(networkEntity, null)
            }
        }

        if (removeNotMatched) {
            localValues.forEach {
                deleteEntity(it)
                removed += it
            }
        }

        added.forEach {
            insertEntity(it)
        }

        Log.i("Syncer", "added: $added")
        Log.i("Syncer", "updated: $updated")
        Log.i("Syncer", "removed: $removed")
        return EntitySyncerResult(added, updated, removed)
    }

    data class EntitySyncerResult<ET : ShimoriEntity>(
        val added: List<ET>,
        val updated: List<ET>,
        val deleted: List<ET>
    )
}

fun <Type : ShimoriEntity, Key> syncerForEntity(
    dao: EntityDao<Type>,
    etToIdFunc: suspend (Type) -> Key?,
    mapper: suspend (Type, Type?) -> Type
) = EntitySyncer(
        dao::insert,
        dao::update,
        dao::delete,
        etToIdFunc,
        etToIdFunc,
        mapper
)