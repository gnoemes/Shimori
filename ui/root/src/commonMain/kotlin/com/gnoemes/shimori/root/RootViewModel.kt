package com.gnoemes.shimori.root

import com.gnoemes.shimori.base.utils.launchOrThrow
import com.gnoemes.shimori.domain.interactors.UpdateUser
import com.gnoemes.shimori.domain.observers.ObserveShikimoriAuth
import com.gnoemes.shimori.logging.api.Logger
import kotlinx.coroutines.CoroutineScope
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class RootViewModel(
    @Assisted private val coroutineScope: CoroutineScope,
    observeShikimoriAuth: Lazy<ObserveShikimoriAuth>,
    private val updateUser: Lazy<UpdateUser>,
    private val logger: Logger,
) {

    init {
        coroutineScope.launchOrThrow {
            observeShikimoriAuth.value.flow.collect {
                logger.i(tag = "[Root]") { "New auth status $it" }
                if (it.isAuthorized)
                    updateUser.value.invoke(UpdateUser.Params(null, true)).onFailure {
                        logger.e(throwable = it) { "Update user error" }
                    }
            }
        }

        observeShikimoriAuth.value.invoke(Unit)
    }
}