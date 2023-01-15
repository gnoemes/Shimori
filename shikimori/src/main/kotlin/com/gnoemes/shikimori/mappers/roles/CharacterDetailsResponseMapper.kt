package com.gnoemes.shikimori.mappers.roles

import com.gnoemes.shikimori.appendHostIfNeed
import com.gnoemes.shikimori.entities.roles.CharacterDetailsResponse
import com.gnoemes.shikimori.mappers.ImageResponseMapper
import com.gnoemes.shikimori.mappers.anime.AnimeResponseMapper
import com.gnoemes.shikimori.mappers.manga.MangaResponseMapper
import com.gnoemes.shimori.data.core.entities.characters.Character
import com.gnoemes.shimori.data.core.entities.characters.CharacterInfo
import com.gnoemes.shimori.data.core.mappers.Mapper

internal class CharacterDetailsResponseMapper(
    private val imageMapper: ImageResponseMapper,
    private val animeResponseMapper: AnimeResponseMapper,
    private val mangaResponseMapper: MangaResponseMapper,
) : Mapper<CharacterDetailsResponse, CharacterInfo> {
    override suspend fun map(from: CharacterDetailsResponse): CharacterInfo {
        val character = Character(
            id = from.id,
            name = from.name,
            nameRu = from.nameRu,
            nameEn = from.name,
            image = imageMapper.map(from.image),
            url = from.url.appendHostIfNeed(),
            description = from.description,
            descriptionSourceUrl = from.descriptionSource,
        )

        return CharacterInfo(
            character = character,
            animes = from.animes.map { animeResponseMapper.map(it) },
            mangas = from.mangas.map { mangaResponseMapper.map(it) },
        )
    }
}