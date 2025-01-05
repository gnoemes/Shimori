package com.gnoemes.shimori.home

import com.gnoemes.shimori.domain.interactors.UpdateUser
import com.gnoemes.shimori.domain.observers.ObserveMyUserShort
import com.gnoemes.shimori.domain.observers.ObserveShikimoriAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class RootViewModel(
    @Assisted private val coroutineScope: CoroutineScope,
    private val observeShikimoriAuth: ObserveShikimoriAuth,
    private val observeMyUserShort: ObserveMyUserShort,
    private val updateUser: UpdateUser,
) {
    val authState = observeShikimoriAuth.flow
    val profile = observeMyUserShort.flow

    init {
        coroutineScope.launch {
            observeShikimoriAuth(Unit)
            observeMyUserShort(Unit)
        }

        coroutineScope.launch {
            updateUser.invoke(UpdateUser.Params(null, true))
        }
    }
}