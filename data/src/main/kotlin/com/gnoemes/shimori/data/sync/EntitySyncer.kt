package com.gnoemes.shimori.data.sync

import android.util.Log
import com.gnoemes.shimori.data.daos.EntityDao
import com.gnoemes.shimori.model.ShimoriEntity

/**
 * @param NT Network type
 * @param ET local entity type
 * @param NID Network ID type
 */
class EntitySyncer<ET : ShimoriEntity, NT, NID>(
    private val insertFunc: suspend (ET) -> Long,
    private val updateFunc: suspend (ET) -> Unit,
    private val deleteFunc: suspend (ET) -> Int,
    private val etToIdFunc: suspend (ET) -> NID?,
    private val ntToIdFunc: suspend (NT) -> NID,
    private val ntToEtFunc: suspend (NT, Long?) -> ET
) {

    suspend fun sync(
        currentValues: Collection<ET>,
        networkValues: Collection<NT>,
        removeNotMatched: Boolean = true
    ): EntitySyncerResult<ET> {
        val localValues = currentValues.toMutableList()

        val added = mutableListOf<ET>()
        val updated = mutableListOf<ET>()
        val removed = mutableListOf<ET>()

        for (networkEntity in networkValues) {
            val remoteId = ntToIdFunc(networkEntity) ?: break
            val localEntity = localValues.find { etToIdFunc(it) == remoteId }

            if (localEntity != null) {
                val entity = ntToEtFunc(networkEntity, localEntity.id)
                if (localEntity != entity) {
                    Log.i("Syncer", "updating: $localEntity \n $entity")
                    updateFunc(entity)
                    updated += entity
                }
                localValues -= localEntity
            } else {
                added += ntToEtFunc(networkEntity, null)
            }
        }

        if (removeNotMatched) {
            localValues.forEach {
                deleteFunc(it)
                removed += it
            }
        }

        added.forEach {
            insertFunc(it)
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

fun <ET : ShimoriEntity, NID> syncerForEntity(
    dao: EntityDao<ET>,
    etToIdFunc: suspend (ET) -> NID?,
    mapper: suspend (ET, Long?) -> ET
) = EntitySyncer(
        dao::insert,
        dao::update,
        dao::delete,
        etToIdFunc,
        etToIdFunc,
        mapper
)