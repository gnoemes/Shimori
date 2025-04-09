package com.gnoemes.shimori.source.catalogue

import com.gnoemes.shimori.source.model.MalIdArgument
import com.gnoemes.shimori.source.model.SCharacter
import com.gnoemes.shimori.source.model.SourceIdArgument


interface CharacterDataSource {
    suspend fun get(id: MalIdArgument): SCharacter
    suspend fun get(id: SourceIdArgument): SCharacter
}