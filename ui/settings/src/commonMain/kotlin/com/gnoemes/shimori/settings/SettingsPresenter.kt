package com.gnoemes.shimori.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.gnoemes.shimori.base.entities.ApplicationInfo
import com.gnoemes.shimori.base.inject.GithubLink
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.common.ui.wrapEventSink
import com.gnoemes.shimori.screens.SettingsAppearanceScreen
import com.gnoemes.shimori.screens.SettingsScreen
import com.gnoemes.shimori.screens.UrlScreen
import com.gnoemes.shimori.sources.shikimori.ShikimoriValues
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.CoroutineScope
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
@CircuitInject(screen = SettingsScreen::class, UiScope::class)
class SettingsPresenter(
    @Assisted private val navigator: Navigator,
    private val applicationInfo: Lazy<ApplicationInfo>,
    private val githubLink: Lazy<GithubLink>,
    private val shikimoriValues: Lazy<ShikimoriValues>,
) : Presenter<SettingsUiState> {

    @Composable
    override fun present(): SettingsUiState {
        val navigateUpEnabled by remember(navigator) { mutableStateOf(navigator.peekBackStack().size > 1) }

        val eventSink: CoroutineScope.(SettingsUiEvent) -> Unit = { event ->
            when (event) {
                is SettingsUiEvent.NavigateUp -> navigator.pop()
                is SettingsUiEvent.OpenAppearenceSettings -> navigator.goTo(SettingsAppearanceScreen())
                is SettingsUiEvent.OpenGithub -> navigator.goTo(UrlScreen(githubLink.value))
                is SettingsUiEvent.OpenShikimori -> navigator.goTo(UrlScreen(shikimoriValues.value.url))
            }
        }

        return SettingsUiState(
            appName = applicationInfo.value.name,
            versionName = applicationInfo.value.versionName,
            navigateUpEnabled = navigateUpEnabled,
            eventSink = wrapEventSink(eventSink)
        )
    }
}