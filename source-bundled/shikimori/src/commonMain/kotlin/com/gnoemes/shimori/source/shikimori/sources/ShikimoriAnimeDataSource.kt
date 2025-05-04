package com.gnoemes.shimori.source.shikimori.sources


import com.apollographql.apollo.api.Optional
import com.gnoemes.shimori.base.utils.forLists
import com.gnoemes.shimori.source.catalogue.AnimeDataSource
import com.gnoemes.shimori.source.model.MalIdArgument
import com.gnoemes.shimori.source.model.SAnime
import com.gnoemes.shimori.source.model.STrackStatus
import com.gnoemes.shimori.source.model.SourceIdArgument
import com.gnoemes.shimori.source.shikimori.AnimeCharactersQuery
import com.gnoemes.shimori.source.shikimori.AnimeDetailsQuery
import com.gnoemes.shimori.source.shikimori.AnimePersonsQuery
import com.gnoemes.shimori.source.shikimori.AnimeTracksQuery
import com.gnoemes.shimori.source.shikimori.Shikimori
import com.gnoemes.shimori.source.shikimori.ShikimoriApi
import com.gnoemes.shimori.source.shikimori.mappers.anime.AnimeCharactersMapper
import com.gnoemes.shimori.source.shikimori.mappers.anime.AnimeDetailsToAnimeInfoMapper
import com.gnoemes.shimori.source.shikimori.mappers.anime.AnimePersonsMapper
import com.gnoemes.shimori.source.shikimori.mappers.anime.AnimeTracksQueryToAnimeWithTrack
import com.gnoemes.shimori.source.shikimori.mappers.anime.CalendarMapper
import com.gnoemes.shimori.source.shikimori.mappers.from
import com.gnoemes.shimori.source.shikimori.type.UserRateStatusEnum
import me.tatarka.inject.annotations.Inject

@Inject
class ShikimoriAnimeDataSource(
    private val api: ShikimoriApi,
    private val calendarMapper: CalendarMapper,
    private val animeTracksQueryToAnimeWithTrack: AnimeTracksQueryToAnimeWithTrack,
    private val animeDetailsToAnimeInfoMapper: AnimeDetailsToAnimeInfoMapper,
    private val charactersMapper: AnimeCharactersMapper,
    private val personsMapper: AnimePersonsMapper,
) : AnimeDataSource {

    override suspend fun get(id: MalIdArgument) = get(SourceIdArgument(id))
    override suspend fun get(id: SourceIdArgument): SAnime {
        return api.apollo.query(
            AnimeDetailsQuery(
                ids = Optional.present(id.id.toString())
            )
        ).dataAssertNoErrors
            .let {
                animeDetailsToAnimeInfoMapper.map(it.animes.first())
            }
    }


    override suspend fun getWithStatus(
        userId: SourceIdArgument,
        status: STrackStatus?
    ): List<SAnime> {
        return api.apollo.query(
            AnimeTracksQuery(
                limit = Optional.present(Shikimori.MAX_PAGE_SIZE),
                userId = Optional.present(userId.id.toString()),
                status = Optional.presentIfNotNull(UserRateStatusEnum.from(status))
            )
        ).dataAssertNoErrors
            .let {
                animeTracksQueryToAnimeWithTrack.forLists().invoke(it.userRates)
            }
    }

    override suspend fun getCharacters(id: MalIdArgument) = getCharacters(SourceIdArgument(id))
    override suspend fun getCharacters(id: SourceIdArgument): SAnime {
        return api.apollo.query(
            AnimeCharactersQuery(
                ids = Optional.present(id.id.toString())
            )
        ).dataAssertNoErrors
            .let {
                charactersMapper.map(it.animes.first())
            }
    }

    override suspend fun getPersons(id: MalIdArgument) = getPersons(SourceIdArgument(id))

    override suspend fun getPersons(id: SourceIdArgument): SAnime {
        return api.apollo.query(
            AnimePersonsQuery(
                ids = Optional.present(id.id.toString())
            )
        ).dataAssertNoErrors
            .let {
                personsMapper.map(it.animes.first())
            }
    }
}


