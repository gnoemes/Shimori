package com.gnoemes.shimori.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gnoemes.shimori.base.core.settings.AppAccentColor
import com.gnoemes.shimori.base.core.settings.AppLocale
import com.gnoemes.shimori.base.core.settings.AppTheme
import com.gnoemes.shimori.base.core.settings.AppTitlesLocale
import com.gnoemes.shimori.common.ui.components.ShimoriSecondaryToolbar
import com.gnoemes.shimori.common.ui.navigation.Screen
import com.gnoemes.shimori.settings.components.AccentColor
import com.gnoemes.shimori.settings.components.Locale
import com.gnoemes.shimori.settings.components.Spoilers
import com.gnoemes.shimori.settings.components.Theme
import com.gnoemes.shimori.settings.components.TitlesLocale

internal object SettingsScreen : Screen() {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<SettingsScreenModel>()
        val navigator = LocalNavigator.currentOrThrow

        val state by screenModel.state.collectAsState()

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            state?.let { state ->
                Settings(
                    navigateUp = { navigator.pop() },
                    appVersion = state.appVersion,
                    titlesLocale = state.titlesLocale,
                    onChangeTitlesLocale = screenModel::onChangeTitlesLocale,
                    locale = state.locale,
                    onChangeLocale = screenModel::onChangeLocale,
                    showSpoilers = state.showSpoilers,
                    onChangeSpoilers = screenModel::onChangeSpoilers,
                    theme = state.theme,
                    onChangeTheme = screenModel::onChangeTheme,
                    accentColor = state.accentColor,
                    onChangeAccentColor = screenModel::onChangeSecondaryColor,
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun Settings(
        navigateUp: () -> Unit,
        appVersion: String,
        titlesLocale: AppTitlesLocale,
        onChangeTitlesLocale: (AppTitlesLocale) -> Unit,
        locale: AppLocale,
        onChangeLocale: (AppLocale) -> Unit,
        showSpoilers: Boolean,
        onChangeSpoilers: (Boolean) -> Unit,
        theme: AppTheme,
        onChangeTheme: (AppTheme) -> Unit,
        accentColor: AppAccentColor,
        onChangeAccentColor: (AppAccentColor) -> Unit
    ) {
        Column {
            ShimoriSecondaryToolbar(
                navigateUp = navigateUp,
                modifier = Modifier.statusBarsPadding(),
                title = stringResource(id = R.string.app_name),
                subTitle = appVersion,
                // use InterpolatingTopAppBarColors instead AnimatingTopAppBarColors
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.96f),
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )

            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                TitlesLocale(titlesLocale, onChangeTitlesLocale)
                Spacer(modifier = Modifier.height(24.dp))
                Locale(locale, onChangeLocale)
                Spacer(modifier = Modifier.height(24.dp))
                Spoilers(showSpoilers, onChangeSpoilers)
                Spacer(modifier = Modifier.height(24.dp))
                Theme(theme, onChangeTheme)
                Spacer(modifier = Modifier.height(24.dp))
                AccentColor(accentColor, onChangeAccentColor)
            }
        }
    }
}