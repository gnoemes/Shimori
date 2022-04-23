package com.gnoemes.shimori.common.ui.utils

import android.os.Bundle
import androidx.lifecycle.*
import androidx.navigation.NavBackStackEntry
import androidx.savedstate.SavedStateRegistryOwner
import org.kodein.di.DI
import org.kodein.di.instanceOrNull

fun kodeinFactory(
    di: DI,
    navBackStackEntry: NavBackStackEntry
): ViewModelProvider.Factory {
    return savedStateFactory(
        owner = navBackStackEntry,
        navBackStackEntry.arguments,
        di = di
    )
}

fun kodeinFactory(
    di: DI,
): ViewModelProvider.Factory {
    return defaultFactory(di = di)
}

private fun savedStateFactory(
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle?,
    di: DI
): ViewModelProvider.Factory {
    return object : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
        override fun <T : ViewModel?> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T {
            val vm by di.instanceOrNull<SavedStateHandle, ViewModel>(
                tag = modelClass.simpleName,
                arg = handle
            )
            return vm as T
        }
    }
}

private fun defaultFactory(
    di: DI
): ViewModelProvider.Factory {
    return object : AbstractSavedStateViewModelFactory() {
        override fun <T : ViewModel?> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T {
            val vm by di.instanceOrNull<SavedStateHandle, ViewModel>(
                tag = modelClass.simpleName,
                arg = handle
            )
            return vm as T
        }
    }
}