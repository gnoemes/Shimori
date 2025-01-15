package com.gnoemes.shimori.settings.appearance

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.base.utils.launchOrThrow
import com.gnoemes.shimori.common.compose.theme.isDynamicColorsAvailable
import com.gnoemes.shimori.common.ui.wrapEventSink
import com.gnoemes.shimori.screens.SettingsAppearanceScreen
import com.gnoemes.shimori.settings.AppLocale
import com.gnoemes.shimori.settings.AppTheme
import com.gnoemes.shimori.settings.AppTitlesLocale
import com.gnoemes.shimori.settings.ShimoriSettings
import com.gnoemes.shimori.settings.invoke
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.CoroutineScope
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
@CircuitInject(screen = SettingsAppearanceScreen::class, UiScope::class)
class SettingsAppearancePresenter(
    @Assisted private val screen: SettingsAppearanceScreen,
    @Assisted private val navigator: Navigator,
    private val settings: ShimoriSettings,
) : Presenter<SettingsAppearanceUiState> {

    @Composable
    override fun present(): SettingsAppearanceUiState {

        val locale by settings.locale.observe.collectAsRetainedState(AppLocale.English)
        val titlesLocale by settings.titlesLocale.observe.collectAsRetainedState(AppTitlesLocale.English)
        val theme by settings.theme.observe.collectAsRetainedState(AppTheme.DARK)
        val systemThemeAvailable = isDynamicColorsAvailable()

        val eventSink: CoroutineScope.(SettingsAppearanceUiEvent) -> Unit = { event ->
            when (event) {
                is SettingsAppearanceUiEvent.NavigateUp -> navigator.pop()
                is SettingsAppearanceUiEvent.ChangeLocale -> launchOrThrow { settings.locale(event.new) }
                is SettingsAppearanceUiEvent.ChangeTitlesLocale -> launchOrThrow {
                    settings.titlesLocale(
                        event.new
                    )
                }

                is SettingsAppearanceUiEvent.ChangeTheme -> launchOrThrow { settings.theme(event.new) }
            }
        }

        return SettingsAppearanceUiState(
            screenAsCard = screen.card,
            locale = locale,
            availableLocales = listOf(AppLocale.English, AppLocale.Russian),
            titlesLocale = titlesLocale,
            availableTitlesLocale = listOf(
                AppTitlesLocale.Russian,
                AppTitlesLocale.English,
                AppTitlesLocale.Romaji
            ),
            theme = theme,
            availableThemes = mutableListOf(AppTheme.DARK, AppTheme.LIGHT).apply {
                if (systemThemeAvailable) add(0, AppTheme.SYSTEM)
            },
            eventSink = wrapEventSink(eventSink)
        )
    }
}