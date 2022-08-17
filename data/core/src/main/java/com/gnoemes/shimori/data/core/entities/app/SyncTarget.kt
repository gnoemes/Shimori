package com.gnoemes.shimori.data.core.entities.app

@kotlinx.serialization.Serializable
data class SyncTarget(
    val api: SyncApi,
    val id: Long
)