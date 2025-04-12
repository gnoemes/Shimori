package com.gnoemes.shimori.data.adapters

import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import com.gnoemes.shimori.data.app.Request
import com.gnoemes.shimori.data.app.SyncAction
import com.gnoemes.shimori.data.common.AgeRating
import com.gnoemes.shimori.data.common.GenreType
import com.gnoemes.shimori.data.common.TitleStatus
import com.gnoemes.shimori.data.track.ListSortOption
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.track.TrackTargetType
import migrations.Anime
import migrations.Anime_video
import migrations.Character_role
import migrations.Genre
import migrations.Genre_relation
import migrations.Last_request
import migrations.List_sort
import migrations.Manga
import migrations.Pinned
import migrations.Ranobe
import migrations.Source_ids_sync
import migrations.Track
import migrations.Track_to_sync
import migrations.User


internal val TrackAdapter = Track.Adapter(
    target_typeAdapter = EnumColumnAdapter<TrackTargetType>(),
    statusAdapter = EnumColumnAdapter<TrackStatus>(),
    date_createdAdapter = InstantAdapter,
    date_updatedAdapter = InstantAdapter,
    scoreAdapter = IntColumnAdapter,
    progressAdapter = IntColumnAdapter,
    re_counterAdapter = IntColumnAdapter
)

internal val UserAdapter = User.Adapter(
    last_onlineAdapter = InstantAdapter,
    full_yearsAdapter = IntColumnAdapter,
)

internal val ListSortAdapter = List_sort.Adapter(
    sortAdapter = EnumColumnAdapter<ListSortOption>(),
    typeAdapter = EnumColumnAdapter<TrackTargetType>(),
)

internal val LastRequestAdapter = Last_request.Adapter(
    requestAdapter = EnumColumnAdapter<Request>(),
    timestampAdapter = InstantAdapter
)

internal val SourceIdsSyncAdapter = Source_ids_sync.Adapter(
    IntColumnAdapter
)

internal val AnimeVideoAdapter = Anime_video.Adapter(
    IntColumnAdapter
)

internal val AnimeAdapter = Anime.Adapter(
    statusAdapter = EnumColumnAdapter<TitleStatus>(),
    date_airedAdapter = LocalDateAdapter,
    date_releasedAdapter = LocalDateAdapter,
    next_episode_dateAdapter = InstantAdapter,
    age_ratingAdapter = EnumColumnAdapter<AgeRating>(),
    episodesAdapter = IntColumnAdapter,
    episodes_airedAdapter = IntColumnAdapter,
    durationAdapter = IntColumnAdapter,
    next_episodeAdapter = IntColumnAdapter,
    )

internal val MangaAdapter = Manga.Adapter(
    statusAdapter = EnumColumnAdapter<TitleStatus>(),
    date_airedAdapter = LocalDateAdapter,
    date_releasedAdapter = LocalDateAdapter,
    age_ratingAdapter = EnumColumnAdapter<AgeRating>(),
    chaptersAdapter = IntColumnAdapter,
    volumesAdapter = IntColumnAdapter,
)

internal val RanobeAdapter = Ranobe.Adapter(
    statusAdapter = EnumColumnAdapter<TitleStatus>(),
    date_airedAdapter = LocalDateAdapter,
    date_releasedAdapter = LocalDateAdapter,
    age_ratingAdapter = EnumColumnAdapter<AgeRating>(),
    chaptersAdapter = IntColumnAdapter,
    volumesAdapter = IntColumnAdapter,
)

internal val TrackToSyncAdapter = Track_to_sync.Adapter(
    sync_actionAdapter = EnumColumnAdapter<SyncAction>(),
    last_attemptAdapter = InstantAdapter,
    attemptsAdapter = IntColumnAdapter
)

internal val CharacterRoleAdapter = Character_role.Adapter(
    target_typeAdapter = EnumColumnAdapter<TrackTargetType>(),
)

internal val GenreAdapter = Genre.Adapter(
    typeAdapter = EnumColumnAdapter<GenreType>()
)

internal val GenreRelationAdapter = Genre_relation.Adapter(
    target_typeAdapter = EnumColumnAdapter<TrackTargetType>(),
)

internal val PinnedAdapter = Pinned.Adapter(
    target_typeAdapter = EnumColumnAdapter<TrackTargetType>(),
)