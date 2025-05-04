package com.gnoemes.shimori.source.catalogue

import com.gnoemes.shimori.source.model.MalIdArgument
import com.gnoemes.shimori.source.model.SPerson
import com.gnoemes.shimori.source.model.SourceIdArgument


interface PersonDataSource {
    suspend fun get(id: MalIdArgument): SPerson
    suspend fun get(id: SourceIdArgument): SPerson
}