package com.gnoemes.shimori.source.model

class SPersonWork(
    val id: Long = 0,
    val targetId: Long,
    val targetType: SourceDataType,
    val role: String?,
    val anime: SAnime?,
    val manga: SManga?
)