package com.gnoemes.shimori.source.model

data class SPersonRole(
    val id: Long = 0,
    val personId: Long,
    val targetId: Long,
    val targetType: SourceDataType,
    val role: String?,
    val roleRu: String?,
)