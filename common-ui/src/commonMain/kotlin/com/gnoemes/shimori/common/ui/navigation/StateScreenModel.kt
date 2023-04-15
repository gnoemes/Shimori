package com.gnoemes.shimori.common.ui.navigation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.ScreenModelStore
import cafe.adriel.voyager.core.model.StateScreenModel
import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.plus

abstract class StateScreenModel<S>(
    initialState: S,
    protected val dispatchers: AppCoroutineDispatchers
) : StateScreenModel<S>(initialState) {
    val ScreenModel.ioCoroutineScope: CoroutineScope
        get() = ScreenModelStore.getOrPutDependency(
            screenModel = this,
            name = "ScreenModelIoCoroutineScope",
            factory = { key -> CoroutineScope(dispatchers.io + SupervisorJob()) + CoroutineName(key) },
            onDispose = { scope -> scope.cancel() }
        )
}