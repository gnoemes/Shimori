package com.gnoemes.shimori.data.source.mapper

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.base.utils.invoke
import com.gnoemes.shimori.data.common.RelatedInfo
import com.gnoemes.shimori.data.titles.manga.MangaInfo
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.source.model.SManga
import me.tatarka.inject.annotations.Inject

@Inject
class SourceMangaMapper(
    private val animeMapper: SourceAnimeEntityMapper,
    private val mangaMapper: SourceMangaEntityMapper,
    private val ranobeMapper: SourceRanobeEntityMapper,
    private val imageMapper: SourceImageMapper,
    private val trackMapper: SourceTrackMapper,
    private val genreMapper: SourceGenreMapper,
    private val characterMapper: SourceCharacterMapper,
    private val roleMapper: SourceCharacterRoleMapper,
    private val personMapper: SourcePersonMapper,
    private val personRoleMapper: SourcePersonRoleMapper,
    private val relatedMapper: SourceRelatedMapper,
) : Mapper<SManga, MangaInfo> {

    override fun map(from: SManga): MangaInfo {
        val entity = mangaMapper(from)
        val track = from.track?.let { trackMapper.map(it) }

        val genres = from.genres
            ?.map { genreMapper.map(it) }

        val characters = from.characters?.map { characterMapper(it) }
        val characterRoles = from.charactersRoles?.mapNotNull { roleMapper(it) }

        val persons = from.persons?.map { personMapper.map(it) }
        val personsRoles = from.personsRoles?.mapNotNull { personRoleMapper.map(it) }

        val related = from.related?.mapNotNull { related ->
            val relation = relatedMapper.map(related)
            val title = when (relation.relatedType) {
                TrackTargetType.ANIME -> related.anime?.let { animeMapper(it) }
                TrackTargetType.MANGA -> related.manga?.let { mangaMapper(it) }
                TrackTargetType.RANOBE -> related.manga?.let { ranobeMapper(it) }
            } ?: return@mapNotNull null

            RelatedInfo(
                titleId = entity.id,
                titleType = TrackTargetType.MANGA,
                relation = relation,
                title = title
            )
        }

        return MangaInfo(
            entity = entity,
            track = track,
            genres = genres,
            characters = characters,
            charactersRoles = characterRoles,
            persons = persons,
            personsRoles = personsRoles,
            related = related,
        )
    }
}