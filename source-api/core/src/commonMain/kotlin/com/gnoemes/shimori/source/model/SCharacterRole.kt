package com.gnoemes.shimori.source.model

class SCharacterRole(
    val id: Long = 0,
    val characterId: Long,
    val targetId: Long,
    val targetType: SourceDataType,
    val role : String?,
    val roleRu : String?,
)