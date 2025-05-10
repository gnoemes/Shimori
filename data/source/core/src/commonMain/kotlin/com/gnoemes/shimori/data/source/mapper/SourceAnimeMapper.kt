package com.gnoemes.shimori.data.source.mapper

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.base.utils.invoke
import com.gnoemes.shimori.data.common.RelatedInfo
import com.gnoemes.shimori.data.titles.anime.AnimeInfo
import com.gnoemes.shimori.data.titles.anime.AnimeScreenshot
import com.gnoemes.shimori.data.titles.anime.AnimeVideo
import com.gnoemes.shimori.data.titles.anime.AnimeVideoType
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.source.model.SAnime
import me.tatarka.inject.annotations.Inject

@Inject
class SourceAnimeMapper(
    private val animeMapper: SourceAnimeEntityMapper,
    private val mangaMapper: SourceMangaEntityMapper,
    private val ranobeMapper: SourceRanobeEntityMapper,
    private val imageMapper: SourceImageMapper,
    private val trackMapper: SourceTrackMapper,
    private val characterMapper: SourceCharacterMapper,
    private val roleMapper: SourceCharacterRoleMapper,
    private val genreMapper: SourceGenreMapper,
    private val studioMapper: SourceStudioMapper,
    private val personMapper: SourcePersonMapper,
    private val personRoleMapper: SourcePersonRoleMapper,
    private val relatedMapper: SourceRelatedMapper,
) : Mapper<SAnime, AnimeInfo> {

    override fun map(from: SAnime): AnimeInfo {
        val entity = animeMapper(from)

        val videos = from.videos?.map {
            AnimeVideo(
                id = it.id,
                titleId = it.titleId,
                name = it.name,
                url = it.url,
                imageUrl = it.imageUrl,
                type = AnimeVideoType.find(it.type)
            )
        }

        val screenshots = from.screenshots?.map {
            AnimeScreenshot(
                id = it.id,
                titleId = it.titleId,
                image = imageMapper.map(it.image)
            )
        }

        val characters = from.characters?.map { characterMapper(it) }
        val characterRoles = from.charactersRoles?.mapNotNull { roleMapper(it) }

        val genres = from.genres
            ?.map { genreMapper.map(it) }

        val studio = from.studio?.let { studioMapper.map(it) }

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
                titleType = TrackTargetType.ANIME,
                relation = relation,
                title = title
            )
        }

        return AnimeInfo(
            entity = entity,
            track = from.track?.let { trackMapper.map(it) },
            videos = videos,
            screenshots = screenshots,
            characters = characters,
            charactersRoles = characterRoles,
            genres = genres,
            studio = studio,
            persons = persons,
            personsRoles = personsRoles,
            related = related
        )
    }

}