package com.gnoemes.shimori.data.db.api.syncer

import com.gnoemes.shimori.data.ShimoriEntity

interface ItemSyncer<LocalType : ShimoriEntity, NetworkType, Key> {
    companion object {
        const val TAG = "ItemSyncer"
        const val RESULT_TAG = "SyncResult"
    }

    fun sync(
        currentValues: Collection<LocalType>,
        networkValues: Collection<NetworkType>,
        removeNotMatched: Boolean = true
    ): ItemSyncerResult<LocalType>

    fun sync(
        localEntity: LocalType?,
        networkEntity: NetworkType,
    ): ItemSyncerResult<LocalType>

}

data class ItemSyncerResult<LocalType : ShimoriEntity>(
    val added: List<LocalType> = emptyList(),
    val deleted: List<LocalType> = emptyList(),
    val updated: List<LocalType> = emptyList()
)
