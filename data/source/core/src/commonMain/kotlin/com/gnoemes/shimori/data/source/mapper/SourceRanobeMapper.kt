package com.gnoemes.shimori.data.source.mapper

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.base.utils.invoke
import com.gnoemes.shimori.data.common.AgeRating
import com.gnoemes.shimori.data.common.TitleStatus
import com.gnoemes.shimori.data.titles.manga.MangaInfo
import com.gnoemes.shimori.data.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.titles.ranobe.RanobeType
import com.gnoemes.shimori.source.model.SManga
import me.tatarka.inject.annotations.Inject

@Inject
class SourceRanobeMapper(
    private val imageMapper: SourceImageMapper,
    private val trackMapper: SourceTrackMapper,
    private val genreMapper: SourceGenreMapper,
    private val characterMapper: SourceCharacterMapper,
    private val roleMapper: SourceCharacterRoleMapper,
    private val personMapper: SourcePersonMapper,
    private val personRoleMapper: SourcePersonRoleMapper
) : Mapper<SManga, MangaInfo> {

    override fun map(from: SManga): MangaInfo {
        val ranobe = Ranobe(
            id = from.id,
            name = from.name,
            nameRu = from.nameRu,
            nameEn = from.nameEn,
            image = from.image?.let { imageMapper.map(it) },
            url = from.url,
            ranobeType = RanobeType.find(from.mangaType),
            rating = from.rating,
            status = TitleStatus.find(from.status),
            volumes = from.volumes,
            chapters = from.chapters,
            dateAired = from.dateAired,
            dateReleased = from.dateReleased,
            ageRating = AgeRating.find(from.ageRating),
            description = from.description,
            descriptionHtml = from.descriptionHtml,
            franchise = from.franchise,
            favorite = from.favorite,
            topicId = from.topicId,
        )
        val track = from.track?.let { trackMapper.map(it) }

        val genres = from.genres
            ?.map { genreMapper.map(it) }

        val characters = from.characters?.map { characterMapper(it) }
        val characterRoles = from.charactersRoles?.mapNotNull { roleMapper(it) }

        val persons = from.persons?.map { personMapper.map(it) }
        val personsRoles = from.personsRoles?.mapNotNull { personRoleMapper.map(it) }

        return MangaInfo(
            entity = ranobe,
            track = track,
            genres = genres,
            characters = characters,
            charactersRoles = characterRoles,
            persons = persons,
            personsRoles = personsRoles
        )
    }
}