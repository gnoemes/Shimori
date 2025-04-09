package com.gnoemes.shimori.data.source.track

import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.db.api.daos.SourceIdsSyncDao
import com.gnoemes.shimori.data.source.SourceManager
import com.gnoemes.shimori.source.Source
import com.gnoemes.shimori.source.track.TrackDataSource
import com.gnoemes.shimori.source.track.TrackSource
import com.gnoemes.shimori.source.track.UserDataSource
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
class TrackManager(
    private val bundledTrackers: Set<TrackSource>,
    dao: SourceIdsSyncDao,
    private val trackAdapter: TrackDataSourceAdapter,
    private val userAdapter: UserDataSourceAdapter,
) : SourceManager<TrackSource>(dao) {

    //TODO runtime extensions via jars
    private val addedTrackers by lazy { emptySet<TrackSource>() }

    val trackers = bundledTrackers + addedTrackers

    suspend fun <ResponseType> track(
        sourceId: Long,
        action: suspend TrackDataSourceAdapter.() -> suspend TrackDataSource.(Source) -> ResponseType
    ): SourceResponse<ResponseType> =
        trackers[sourceId]?.run {
            val sourceAction = trackAdapter(action)
            return request(this, trackDataSource, sourceAction)
        } ?: throw IllegalArgumentException("Track source with id $sourceId not found")

    suspend fun <ResponseType> user(
        sourceId: Long,
        action: suspend UserDataSourceAdapter.() -> suspend UserDataSource.(Source) -> ResponseType
    ): SourceResponse<ResponseType> =
        trackers[sourceId]?.run {
            val sourceAction = userAdapter(action)
            return request(this, userDataSource, sourceAction)
        } ?: throw IllegalArgumentException("Track source with id $sourceId not found")


    private operator fun Set<TrackSource>.get(id: Long) = find { it.id == id }

}