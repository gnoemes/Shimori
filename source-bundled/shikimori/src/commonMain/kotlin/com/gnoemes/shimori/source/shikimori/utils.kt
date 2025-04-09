package com.gnoemes.shimori.source.shikimori

import com.gnoemes.shimori.source.model.SimpleAuthState
import com.gnoemes.shimori.source.model.SourceAuthState
import org.publicvalue.multiplatform.oidc.types.remote.AccessTokenResponse

fun AccessTokenResponse.toSimpleState(sourceId: Long) : SourceAuthState {
    return SimpleAuthState(
        sourceId,
        accessToken = access_token,
        refreshToken = refresh_token!!
    )
}