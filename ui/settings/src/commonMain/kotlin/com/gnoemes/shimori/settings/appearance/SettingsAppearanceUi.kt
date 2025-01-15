package com.gnoemes.shimori.settings.appearance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.common.compose.LocalShimoriTextCreator
import com.gnoemes.shimori.common.compose.NestedScaffold
import com.gnoemes.shimori.common.compose.ui.TransparentToolbar
import com.gnoemes.shimori.common.ui.resources.strings.settings_app_language
import com.gnoemes.shimori.common.ui.resources.strings.settings_appearence
import com.gnoemes.shimori.common.ui.resources.strings.settings_theme
import com.gnoemes.shimori.common.ui.resources.strings.settings_titles
import com.gnoemes.shimori.common.ui.resources.util.Strings
import com.gnoemes.shimori.screens.SettingsAppearanceScreen
import com.gnoemes.shimori.settings.AppLocale
import com.gnoemes.shimori.settings.AppTheme
import com.gnoemes.shimori.settings.AppTitlesLocale
import com.slack.circuit.codegen.annotations.CircuitInject
import org.jetbrains.compose.resources.stringResource

@Composable
@CircuitInject(screen = SettingsAppearanceScreen::class, scope = UiScope::class)
internal fun SettingsAppearanceUi(
    state: SettingsAppearanceUiState,
    modifier: Modifier = Modifier
) {
    val eventSink = state.eventSink

    SettingsAppearanceUi(
        modifier = modifier,
        asCard = state.screenAsCard,
        selectedLocale = state.locale,
        availableLocales = state.availableLocales,
        selectedTitlesLocale = state.titlesLocale,
        availableTitlesLocale = state.availableTitlesLocale,
        selectedTheme = state.theme,
        availableThemes = state.availableThemes,
        changeLocale = { eventSink(SettingsAppearanceUiEvent.ChangeLocale(it)) },
        changeTitlesLocale = { eventSink(SettingsAppearanceUiEvent.ChangeTitlesLocale(it)) },
        changeTheme = { eventSink(SettingsAppearanceUiEvent.ChangeTheme(it)) },
        navigateUp = { eventSink(SettingsAppearanceUiEvent.NavigateUp) },
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SettingsAppearanceUi(
    modifier: Modifier,
    asCard: Boolean,
    selectedLocale: AppLocale,
    availableLocales: List<AppLocale>,
    selectedTitlesLocale: AppTitlesLocale,
    availableTitlesLocale: List<AppTitlesLocale>,
    selectedTheme: AppTheme,
    availableThemes: List<AppTheme>,
    changeLocale: (AppLocale) -> Unit,
    changeTitlesLocale: (AppTitlesLocale) -> Unit,
    changeTheme: (AppTheme) -> Unit,
    navigateUp: () -> Unit
) {

    if (asCard) {
        SettingsAppearanceUi(
            PaddingValues(),
            true,
            selectedLocale,
            availableLocales,
            selectedTitlesLocale,
            availableTitlesLocale,
            selectedTheme,
            availableThemes,
            changeLocale,
            changeTitlesLocale,
            changeTheme
        )
    } else {
        NestedScaffold(
            modifier = modifier,
            topBar = {
                TransparentToolbar(
                    title = { Text(stringResource(Strings.settings_appearence)) },
                    onNavigationClick = navigateUp
                )
            }
        ) { paddingValues ->
            SettingsAppearanceUi(
                paddingValues,
                false,
                selectedLocale,
                availableLocales,
                selectedTitlesLocale,
                availableTitlesLocale,
                selectedTheme,
                availableThemes,
                changeLocale,
                changeTitlesLocale,
                changeTheme
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SettingsAppearanceUi(
    paddingValues: PaddingValues,
    asCard: Boolean,
    selectedLocale: AppLocale,
    availableLocales: List<AppLocale>,
    selectedTitlesLocale: AppTitlesLocale,
    availableTitlesLocale: List<AppTitlesLocale>,
    selectedTheme: AppTheme,
    availableThemes: List<AppTheme>,
    changeLocale: (AppLocale) -> Unit,
    changeTitlesLocale: (AppTitlesLocale) -> Unit,
    changeTheme: (AppTheme) -> Unit,
) {
    val textCreator = LocalShimoriTextCreator.current

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
    ) {
        if (asCard) Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(Strings.settings_app_language),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            availableLocales.forEach { locale ->
                val selected = selectedLocale == locale
                FilterChip(
                    selected = selected,
                    onClick = { changeLocale(locale) },
                    label = {
                        Text(textCreator.name(locale))
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(Strings.settings_titles),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            availableTitlesLocale.forEach { locale ->
                val selected = selectedTitlesLocale == locale
                FilterChip(
                    selected = selected,
                    onClick = { changeTitlesLocale(locale) },
                    label = {
                        Text(textCreator.name(locale))
                    }
                )
            }
        }


        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(Strings.settings_theme),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            availableThemes.forEach { theme ->
                val selected = selectedTheme == theme
                FilterChip(
                    selected = selected,
                    onClick = { changeTheme(theme) },
                    label = {
                        Text(textCreator.name(theme))
                    }
                )
            }
        }
    }
}