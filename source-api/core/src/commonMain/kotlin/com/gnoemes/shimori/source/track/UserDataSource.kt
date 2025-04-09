package com.gnoemes.shimori.source.track

import com.gnoemes.shimori.source.model.SUserProfile
import com.gnoemes.shimori.source.model.SourceIdArgument


interface UserDataSource {
    suspend fun getMyUser(): SUserProfile
    suspend fun get(id: SourceIdArgument): SUserProfile
}