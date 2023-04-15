package com.gnoemes.shikimori

import com.gnoemes.shikimori.mappers.AgeRatingMapper
import com.gnoemes.shikimori.mappers.GenreMapper
import com.gnoemes.shikimori.mappers.ImageResponseMapper
import com.gnoemes.shikimori.mappers.TitleStatusMapper
import com.gnoemes.shikimori.mappers.anime.AnimeDetailsMapper
import com.gnoemes.shikimori.mappers.anime.AnimeResponseMapper
import com.gnoemes.shikimori.mappers.anime.AnimeScreenshotMapper
import com.gnoemes.shikimori.mappers.anime.AnimeTypeMapper
import com.gnoemes.shikimori.mappers.anime.AnimeVideoMapper
import com.gnoemes.shikimori.mappers.anime.AnimeVideoTypeMapper
import com.gnoemes.shikimori.mappers.anime.CalendarMapper
import com.gnoemes.shikimori.mappers.anime.RateResponseToAnimeWithRateMapper
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
import com.gnoemes.shikimori.sources.ShikimoriAnimeDataSource
import com.gnoemes.shikimori.sources.ShikimoriCharacterDataSource
import com.gnoemes.shikimori.sources.ShikimoriMangaDataSource
import com.gnoemes.shikimori.sources.ShikimoriRanobeDataSource
import com.gnoemes.shikimori.sources.ShikimoriTrackDataSource
import com.gnoemes.shikimori.sources.ShikimoriUserDataSource
import com.gnoemes.shimori.base.core.di.KodeinTag
import com.gnoemes.shimori.base.core.entities.SourcePlatformValues
import com.gnoemes.shimori.base.core.extensions.new
import com.gnoemes.shimori.data.core.sources.AnimeDataSource
import com.gnoemes.shimori.data.core.sources.CharacterDataSource
import com.gnoemes.shimori.data.core.sources.MangaDataSource
import com.gnoemes.shimori.data.core.sources.RanobeDataSource
import com.gnoemes.shimori.data.core.sources.TrackDataSource
import com.gnoemes.shimori.data.core.sources.UserDataSource
import com.gnoemes.shimori.source.CatalogueSource
import com.gnoemes.shimori.source.TrackSource
import org.kodein.di.DI
import org.kodein.di.bindEagerSingleton
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.inBindSet
import org.kodein.di.instance
import org.kodein.di.singleton

val shikimoriModule = DI.Module("shikimori") {
    importOnce(mappers)

    bindSingleton(KodeinTag.Shikimori.tag) {
        val url = instance<String>(KodeinTag.Shikimori.url)
        SourcePlatformValues(
            url = url,
            clientId = instance(KodeinTag.Shikimori.clientId),
            secretKey = instance(KodeinTag.Shikimori.clientSecret),
            userAgent = instance(KodeinTag.userAgent),
            oauthRedirect = instance(KodeinTag.Shikimori.oAuthRedirect),
            signInUrl = "$url/users/sign_in",
            signUpUrl = "$url/users/sign_up",
            oAuthUrl = "$url/oauth/authorize",
        )
    }

    bindEagerSingleton {
        Shikimori(
            instance(KodeinTag.Shikimori.tag),
            instance(KodeinTag.Shikimori.tag),
            instance(),
            instance(),
        )
    }


    bindSingleton<TrackDataSource>(KodeinTag.Shikimori.tag) { new(::ShikimoriTrackDataSource) }
    bindSingleton<UserDataSource>(KodeinTag.Shikimori.tag) { new(::ShikimoriUserDataSource) }
    bindSingleton<AnimeDataSource>(KodeinTag.Shikimori.tag) { new(::ShikimoriAnimeDataSource) }
    bindSingleton<MangaDataSource>(KodeinTag.Shikimori.tag) { new(::ShikimoriMangaDataSource) }
    bindSingleton<RanobeDataSource>(KodeinTag.Shikimori.tag) { new(::ShikimoriRanobeDataSource) }
    bindSingleton<CharacterDataSource>(KodeinTag.Shikimori.tag) { new(::ShikimoriCharacterDataSource) }

    bindSingleton {
        ShikimoriSource(
            instance(KodeinTag.Shikimori.tag),
            instance(KodeinTag.Shikimori.tag),
            instance(KodeinTag.Shikimori.tag),
            instance(KodeinTag.Shikimori.tag),
            instance(KodeinTag.Shikimori.tag),
            instance(KodeinTag.Shikimori.tag),
            instance(KodeinTag.Shikimori.tag),
        )
    }

    inBindSet<CatalogueSource> {
        add {
            singleton {
                instance<ShikimoriSource>()
            }
        }
    }

    inBindSet<TrackSource> {
        add {
            singleton {
                instance<ShikimoriSource>()
            }
        }
    }
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
    bindProvider { new(::AnimeVideoMapper) }
    bindProvider { new(::AnimeVideoTypeMapper) }
    bindProvider { new(::AnimeScreenshotMapper) }
}