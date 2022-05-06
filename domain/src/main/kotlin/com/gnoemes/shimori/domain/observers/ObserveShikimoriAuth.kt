package com.gnoemes.shimori.domain.observers

import com.gnoemes.shikimori.Shikimori
import com.gnoemes.shimori.data.core.entities.auth.ShikimoriAuthState
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow

class ObserveShikimoriAuth(
    private val shikimori: Shikimori
) : SubjectInteractor<Unit, ShikimoriAuthState>() {
    override fun create(params: Unit): Flow<ShikimoriAuthState> = shikimori.authState
}