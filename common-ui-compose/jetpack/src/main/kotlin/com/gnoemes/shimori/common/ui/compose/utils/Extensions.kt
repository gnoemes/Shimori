package com.gnoemes.shimori.common.ui.compose.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import com.gnoemes.shimori.base.core.settings.AppTheme
import com.gnoemes.shimori.base.core.settings.ShimoriSettings
import org.kodein.di.DI
import org.kodein.di.DirectDI
import org.kodein.di.bindFactory
import org.kodein.di.compose.localDI

@Composable
fun ShimoriSettings.shouldUseDarkColors(): Boolean {
    val themePreference = theme.observe.collectAsStateWithLifecycle(initial = AppTheme.SYSTEM)
    return when (themePreference.value) {
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
        else -> isSystemInDarkTheme()
    }
}

@Composable
inline fun <reified VM : ViewModel> shimoriViewModel(
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current),
): VM {
    val factory = createVMFactory(viewModelStoreOwner = viewModelStoreOwner)
    return viewModel(viewModelStoreOwner, factory = factory)
}

inline fun <reified T : ViewModel> DI.Builder.bindViewModel(noinline creator: DirectDI.(SavedStateHandle) -> T) {
    return bindFactory(tag = T::class.java.simpleName, creator = creator)
}

@Composable
@PublishedApi
internal fun createVMFactory(
    viewModelStoreOwner: ViewModelStoreOwner
): ViewModelProvider.Factory {
    val di = localDI()

    return if (viewModelStoreOwner is NavBackStackEntry) {
        kodeinFactory(
            navBackStackEntry = viewModelStoreOwner,
            di = di
        )
    } else kodeinFactory(di)
}
