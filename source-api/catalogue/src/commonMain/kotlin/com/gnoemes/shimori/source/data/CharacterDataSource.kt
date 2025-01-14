package com.gnoemes.shimori.source.data

import com.gnoemes.shimori.data.characters.Character
import com.gnoemes.shimori.data.characters.CharacterInfo


interface CharacterDataSource {
    suspend fun get(character: Character): CharacterInfo
}