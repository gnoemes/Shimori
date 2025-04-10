package com.gnoemes.shimori.source.catalogue

import com.gnoemes.shimori.source.model.MalIdArgument
import com.gnoemes.shimori.source.model.SManga
import com.gnoemes.shimori.source.model.STrackStatus
import com.gnoemes.shimori.source.model.SourceIdArgument

interface RanobeDataSource {
    suspend fun getWithStatus(userId: SourceIdArgument, status: STrackStatus?): List<SManga>
    suspend fun get(id: MalIdArgument): SManga
    suspend fun get(id: SourceIdArgument): SManga
    suspend fun getCharacters(id : MalIdArgument): SManga
    suspend fun getCharacters(id : SourceIdArgument): SManga
}