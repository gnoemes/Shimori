package com.gnoemes.shimori.data.source.track

import com.gnoemes.shimori.data.source.mapper.SourceRequestMapper
import com.gnoemes.shimori.data.source.mapper.SourceUserProfileMapper
import com.gnoemes.shimori.data.user.User
import com.gnoemes.shimori.source.Source
import com.gnoemes.shimori.source.model.SourceDataType
import com.gnoemes.shimori.source.model.SourceIdArgument
import com.gnoemes.shimori.source.track.UserDataSource
import me.tatarka.inject.annotations.Inject

@Inject
class UserDataSourceAdapter(
    private val requestMapper: SourceRequestMapper,
    private val mapper: SourceUserProfileMapper,
) {

    suspend inline operator fun <ResponseType> invoke(
        crossinline action: suspend UserDataSourceAdapter.() -> suspend (UserDataSource.(Source) -> ResponseType)
    ): suspend UserDataSource.(Source) -> ResponseType {
        val wrap: suspend UserDataSourceAdapter.() -> suspend (UserDataSource.(Source) -> ResponseType) =
            { action() }
        return wrap()
    }

    fun getMyUser(): suspend UserDataSource.(Source) -> User = { source ->
        getMyUser().let(mapper::map)
    }

    fun get(id: Long): suspend UserDataSource.(Source) -> User = { source ->
        val remoteId = requestMapper.findRemoteId(source, id, SourceDataType.User)
        get(SourceIdArgument(remoteId)).let(mapper::map)
    }

}