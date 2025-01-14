package com.gnoemes.shimori.data.source.auth

import com.gnoemes.shimori.source.SourceAuthState
import org.publicvalue.multiplatform.oidc.types.remote.AccessTokenResponse

fun AccessTokenResponse.toSimpleState(sourceId: Long) : SourceAuthState {
    return SimpleAuthState(
        sourceId,
        accessToken = access_token,
        refreshToken = refresh_token!!
    )
}