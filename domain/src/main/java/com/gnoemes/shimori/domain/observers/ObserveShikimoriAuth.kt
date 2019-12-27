package com.gnoemes.shimori.domain.observers

import com.gnoemes.shikimori.ShikimoriManager
import com.gnoemes.shikimori.entities.user.ShikimoriAuthState
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveShikimoriAuth @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val manager: ShikimoriManager
) : SubjectInteractor<Unit, ShikimoriAuthState>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.main

    override fun createObservable(params: Unit): Flow<ShikimoriAuthState> = manager.state
}