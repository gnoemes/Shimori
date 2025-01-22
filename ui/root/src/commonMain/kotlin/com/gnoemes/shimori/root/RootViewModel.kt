package com.gnoemes.shimori.root

import com.gnoemes.shimori.base.utils.launchOrThrow
import com.gnoemes.shimori.domain.interactors.UpdateUserAndTracks
import com.gnoemes.shimori.domain.observers.ObserveShikimoriAuth
import com.gnoemes.shimori.logging.api.Logger
import kotlinx.coroutines.CoroutineScope
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class RootViewModel(
    @Assisted private val coroutineScope: CoroutineScope,
    observeShikimoriAuth: Lazy<ObserveShikimoriAuth>,
    private val updateUserAndTracks: Lazy<UpdateUserAndTracks>,
    private val logger: Logger,
) {

    init {
        coroutineScope.launchOrThrow {
            observeShikimoriAuth.value.flow.collect {
                logger.i(tag = "[Root]") { "New auth status $it" }
                if (it.isAuthorized) sync()
            }
        }

        observeShikimoriAuth.value.invoke(Unit)
    }

    private fun sync() {
        coroutineScope.launchOrThrow {
            updateUserAndTracks.value(
                UpdateUserAndTracks.Params(
                    forceTitlesUpdate = false,
                    optionalTitlesUpdate = true
                )
            ).onFailure {
                logger.e(throwable = it) { "Update user & tracks error" }
            }.onSuccess {
                logger.d { "User & tracks successfully updated" }
            }
        }
    }
}