package com.gnoemes.shimori.data.source.track

import com.gnoemes.shimori.data.app.SourceDataType
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.source.SourceManager
import com.gnoemes.shimori.data.source.mapper.SourceRequestMapper
import com.gnoemes.shimori.preferences.ShimoriPreferences
import com.gnoemes.shimori.source.TrackSource
import com.gnoemes.shimori.source.data.TrackDataSource
import com.gnoemes.shimori.source.data.UserDataSource
import me.tatarka.inject.annotations.Inject

@Inject
class TrackManager(
    val trackers: Set<TrackSource>,
    private val prefs: ShimoriPreferences,
    mapper: SourceRequestMapper,
) : SourceManager<TrackSource>(mapper) {
    suspend fun <ResponseType> track(
        sourceId: Long,
        action: suspend TrackDataSource.() -> ResponseType
    ): SourceResponse<ResponseType> =
        trackers[sourceId]?.run {
            request(this, trackDataSource, action)
        } ?: throw IllegalArgumentException("Track source with id $sourceId not found")

    suspend fun <RequestType, ResponseType> track(
        sourceId: Long,
        data: RequestType,
        action: suspend TrackDataSource.(RequestType) -> ResponseType
    ): SourceResponse<ResponseType> = trackers[sourceId]?.run {
        request(this, trackDataSource, mapper, SourceDataType.Track, data, action)
    } ?: throw IllegalArgumentException("Track source with id $sourceId not found")


    suspend fun <ResponseType> user(
        sourceId: Long,
        action: suspend UserDataSource.() -> ResponseType
    ): SourceResponse<ResponseType> =
        trackers[sourceId]?.run {
            request(this, userDataSource, action)
        } ?: throw IllegalArgumentException("Track source with id $sourceId not found")

    suspend fun <RequestType, ResponseType> user(
        sourceId: Long,
        data: RequestType,
        action: suspend UserDataSource.(RequestType) -> ResponseType
    ): SourceResponse<ResponseType> = trackers[sourceId]?.run {
        request(this, userDataSource, mapper, SourceDataType.User, data, action)
    } ?: throw IllegalArgumentException("Track source with id $sourceId not found")


    private operator fun Set<TrackSource>.get(id: Long) = find { it.id == id }

}