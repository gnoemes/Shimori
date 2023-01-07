package com.gnoemes.shikimori

import com.gnoemes.shikimori.mappers.AgeRatingMapper
import com.gnoemes.shikimori.mappers.GenreMapper
import com.gnoemes.shikimori.mappers.ImageResponseMapper
import com.gnoemes.shikimori.mappers.TitleStatusMapper
import com.gnoemes.shikimori.mappers.anime.*
import com.gnoemes.shikimori.mappers.manga.MangaDetailsMapper
import com.gnoemes.shikimori.mappers.manga.MangaResponseMapper
import com.gnoemes.shikimori.mappers.manga.MangaTypeMapper
import com.gnoemes.shikimori.mappers.manga.RateResponseToMangaWithRateMapper
import com.gnoemes.shikimori.mappers.ranobe.RanobeDetailsMapper
import com.gnoemes.shikimori.mappers.ranobe.RanobeResponseMapper
import com.gnoemes.shikimori.mappers.ranobe.RanobeTypeMapper
import com.gnoemes.shikimori.mappers.ranobe.RateResponseToRanobeWithRateMapper
import com.gnoemes.shikimori.mappers.rate.RateMapper
import com.gnoemes.shikimori.mappers.rate.RateResponseToRateMapper
import com.gnoemes.shikimori.mappers.rate.RateStatusMapper
import com.gnoemes.shikimori.mappers.rate.RateTargetTypeMapper
import com.gnoemes.shikimori.mappers.roles.CharacterDetailsResponseMapper
import com.gnoemes.shikimori.mappers.roles.CharacterResponseMapper
import com.gnoemes.shikimori.mappers.roles.RolesMapper
import com.gnoemes.shikimori.mappers.user.UserBriefMapper
import com.gnoemes.shikimori.mappers.user.UserResponseMapper
import com.gnoemes.shikimori.sources.*
import com.gnoemes.shimori.base.core.di.KodeinTag
import com.gnoemes.shimori.base.core.extensions.new
import com.gnoemes.shimori.data.core.sources.*
import org.kodein.di.*

val shikimoriModule = DI.Module("shikimori") {
    importOnce(mappers)

    bindEagerSingleton {
        Shikimori(
            instance(KodeinTag.shikimori),
            instance(),
            instance(),
            instance(),
        )
    }

    bindSingleton<RateDataSource> { new(::ShikimoriRateDataSource) }
    bindSingleton<UserDataSource> { new(::ShikimoriUserDataSource) }
    bindSingleton<AnimeDataSource> { new(::ShikimoriAnimeDataSource) }
    bindSingleton<MangaDataSource> { new(::ShikimoriMangaDataSource) }
    bindSingleton<RanobeDataSource> { new(::ShikimoriRanobeDataSource) }
    bindSingleton<CharacterDataSource> { new(::ShikimoriCharacterDataSource) }
}

private val mappers = DI.Module("shikimori-mappers") {
    bindProvider { RateTargetTypeMapper() }
    bindProvider { RateStatusMapper() }
    bindProvider { UserBriefMapper() }
    bindProvider { ImageResponseMapper() }
    bindProvider { UserResponseMapper() }
    bindProvider { TitleStatusMapper() }
    bindProvider { GenreMapper() }
    bindProvider { MangaTypeMapper() }
    bindProvider { AnimeTypeMapper() }
    bindProvider { RanobeTypeMapper() }
    bindProvider { AgeRatingMapper() }

    bindProvider { new(::RateMapper) }
    bindProvider { new(::RateResponseToRateMapper) }
    bindProvider { new(::CalendarMapper) }
    bindProvider { new(::AnimeResponseMapper) }
    bindProvider { new(::RateResponseToAnimeWithRateMapper) }
    bindProvider { new(::MangaResponseMapper) }
    bindProvider { new(::RateResponseToMangaWithRateMapper) }
    bindProvider { new(::RanobeResponseMapper) }
    bindProvider { new(::RateResponseToRanobeWithRateMapper) }

    bindProvider { new(::AnimeDetailsMapper) }
    bindProvider { new(::MangaDetailsMapper) }
    bindProvider { new(::RanobeDetailsMapper) }

    bindProvider { new(::CharacterResponseMapper) }
    bindProvider { new(::CharacterDetailsResponseMapper) }
    bindProvider { new(::RolesMapper) }
}