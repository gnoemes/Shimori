package com.gnoemes.shimori.data.core.entities.app

@kotlinx.serialization.Serializable
data class SyncTarget(
    //remote api
    val api: SyncApi,
    //remote content id
    val id: Long
)