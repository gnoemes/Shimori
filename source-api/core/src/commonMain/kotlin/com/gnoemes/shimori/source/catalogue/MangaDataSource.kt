package com.gnoemes.shimori.source.catalogue

import com.gnoemes.shimori.source.model.MalIdArgument
import com.gnoemes.shimori.source.model.SManga
import com.gnoemes.shimori.source.model.STrackStatus
import com.gnoemes.shimori.source.model.SourceIdArgument

interface MangaDataSource {
    suspend fun getWithStatus(userId: SourceIdArgument, status: STrackStatus?): List<SManga>
    suspend fun get(id: MalIdArgument): SManga
    suspend fun get(id: SourceIdArgument): SManga
    suspend fun getCharacters(id : MalIdArgument): SManga
    suspend fun getCharacters(id : SourceIdArgument): SManga

    suspend fun getPersons(id : MalIdArgument): SManga
    suspend fun getPersons(id : SourceIdArgument): SManga

    suspend fun getRelated(id: MalIdArgument): SManga
    suspend fun getRelated(id: SourceIdArgument): SManga
}