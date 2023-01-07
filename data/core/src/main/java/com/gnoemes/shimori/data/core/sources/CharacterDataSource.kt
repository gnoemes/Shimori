package com.gnoemes.shimori.data.core.sources

import com.gnoemes.shimori.data.core.entities.characters.Character
import com.gnoemes.shimori.data.core.entities.characters.CharacterInfo


interface CharacterDataSource {
    suspend fun get(character: Character): CharacterInfo
}