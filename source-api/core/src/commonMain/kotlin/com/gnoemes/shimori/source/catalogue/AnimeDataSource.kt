package com.gnoemes.shimori.source.catalogue

import com.gnoemes.shimori.source.model.MalIdArgument
import com.gnoemes.shimori.source.model.SAnime
import com.gnoemes.shimori.source.model.STrackStatus
import com.gnoemes.shimori.source.model.SourceIdArgument

interface AnimeDataSource {
    suspend fun getWithStatus(userId : SourceIdArgument, status: STrackStatus?): List<SAnime>
    suspend fun get(id : MalIdArgument): SAnime
    suspend fun get(id : SourceIdArgument): SAnime
    suspend fun getCharacters(id : MalIdArgument): SAnime
    suspend fun getCharacters(id : SourceIdArgument): SAnime

    suspend fun getPersons(id : MalIdArgument): SAnime
    suspend fun getPersons(id : SourceIdArgument): SAnime
}