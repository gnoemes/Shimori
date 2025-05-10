package com.gnoemes.shimori.source.model

data class SRelated(
    val targetId: Long,
    val targetType: SourceDataType,
    val relationType: String,
    val relationText: String,
    val anime: SAnime?,
    val manga: SManga?
)