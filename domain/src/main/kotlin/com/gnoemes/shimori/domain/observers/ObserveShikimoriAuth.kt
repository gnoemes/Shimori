package com.gnoemes.shimori.domain.observers

import com.gnoemes.shikimori.ShikimoriManager
import com.gnoemes.shikimori.entities.user.ShikimoriAuthState
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveShikimoriAuth @Inject constructor(
    private val manager: ShikimoriManager
) : SubjectInteractor<Unit, ShikimoriAuthState>() {
    override fun createObservable(params: Unit): Flow<ShikimoriAuthState> = manager.state
}