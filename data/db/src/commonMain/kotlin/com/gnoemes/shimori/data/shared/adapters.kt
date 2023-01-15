package com.gnoemes.shimori.data.shared

import com.gnoemes.shimori.data.core.entities.app.Request
import com.gnoemes.shimori.data.core.entities.app.SyncAction
import com.gnoemes.shimori.data.core.entities.common.AgeRating
import com.gnoemes.shimori.data.core.entities.common.Genre
import com.gnoemes.shimori.data.core.entities.common.TitleStatus
import com.gnoemes.shimori.data.core.entities.track.ListSortOption
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.squareup.sqldelight.ColumnAdapter
import comgnoemesshimoridatadb.data.*
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal object LocalDateAdapter : ColumnAdapter<LocalDate, String> {
    override fun decode(databaseValue: String) = LocalDate.parse(databaseValue)
    override fun encode(value: LocalDate) = value.toString()
}

internal object TrackTargetAdapter : ColumnAdapter<TrackTargetType, String> {
    override fun decode(databaseValue: String) = TrackTargetType.valueOf(databaseValue)
    override fun encode(value: TrackTargetType) = value.name
}

internal object TrackStatusAdapter : ColumnAdapter<TrackStatus, String> {
    override fun decode(databaseValue: String) = TrackStatus.valueOf(databaseValue)
    override fun encode(value: TrackStatus): String = value.name
}

internal object ListSortOptionAdapter : ColumnAdapter<ListSortOption, String> {
    override fun decode(databaseValue: String) = ListSortOption.valueOf(databaseValue)
    override fun encode(value: ListSortOption): String = value.name
}

internal object RequestAdapter : ColumnAdapter<Request, String> {
    override fun decode(databaseValue: String) = Request.valueOf(databaseValue)
    override fun encode(value: Request): String = value.name
}

internal object InstantAdapter : ColumnAdapter<Instant, Long> {
    override fun decode(databaseValue: Long) = Instant.fromEpochMilliseconds(databaseValue)
    override fun encode(value: Instant): Long = value.toEpochMilliseconds()
}

internal object TitleStatusAdapter : ColumnAdapter<TitleStatus, String> {
    override fun decode(databaseValue: String) = TitleStatus.valueOf(databaseValue)
    override fun encode(value: TitleStatus): String = value.name
}

internal object AgeRatingAdapter : ColumnAdapter<AgeRating, String> {
    override fun decode(databaseValue: String) = AgeRating.valueOf(databaseValue)
    override fun encode(value: AgeRating): String = value.name
}

internal object GenresAdapter : ColumnAdapter<List<Genre>, String> {

    override fun decode(databaseValue: String) = Json.decodeFromString<List<Genre>>(databaseValue)
    override fun encode(value: List<Genre>): String = Json.encodeToString(value)
}

internal object SyncActionAdapter : ColumnAdapter<SyncAction, String> {
    override fun decode(databaseValue: String) = SyncAction.valueOf(databaseValue)
    override fun encode(value: SyncAction): String = value.name
}


internal val TrackAdapter = Track.Adapter(
    target_typeAdapter = TrackTargetAdapter,
    statusAdapter = TrackStatusAdapter,
    date_createdAdapter = InstantAdapter,
    date_updatedAdapter = InstantAdapter
)

internal val UserAdapter = User.Adapter(
    last_onlineAdapter = InstantAdapter
)

internal val ListSortAdapter = List_sort.Adapter(
    sortAdapter = ListSortOptionAdapter
)

internal val LastRequestAdapter = Last_request.Adapter(
    requestAdapter = RequestAdapter,
    timestampAdapter = InstantAdapter
)

internal val AnimeAdapter = Anime.Adapter(
    statusAdapter = TitleStatusAdapter,
    date_airedAdapter = LocalDateAdapter,
    date_releasedAdapter = LocalDateAdapter,
    next_episode_dateAdapter = InstantAdapter,
    next_episode_end_dateAdapter = InstantAdapter,
    age_ratingAdapter = AgeRatingAdapter,
    genresAdapter = GenresAdapter
)

internal val MangaAdapter = Manga.Adapter(
    statusAdapter = TitleStatusAdapter,
    date_airedAdapter = LocalDateAdapter,
    date_releasedAdapter = LocalDateAdapter,
    age_ratingAdapter = AgeRatingAdapter,
    genresAdapter = GenresAdapter
)

internal val RanobeAdapter = Ranobe.Adapter(
    statusAdapter = TitleStatusAdapter,
    date_airedAdapter = LocalDateAdapter,
    date_releasedAdapter = LocalDateAdapter,
    age_ratingAdapter = AgeRatingAdapter,
    genresAdapter = GenresAdapter
)

internal val PinnedAdapter = Pinned.Adapter(
    target_typeAdapter = TrackTargetAdapter
)

internal val TrackToSyncAdapter = Track_to_sync.Adapter(
    sync_actionAdapter = SyncActionAdapter,
    last_attemptAdapter = InstantAdapter
)

internal val CharacterRoleAdapter = Character_role.Adapter(
    target_typeAdapter = TrackTargetAdapter,
)