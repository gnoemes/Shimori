@file:OptIn(ExperimentalLifecycleComposeApi::class)

package com.gnoemes.shimori.settings

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gnoemes.shimori.base.core.settings.AppAccentColor
import com.gnoemes.shimori.base.core.settings.AppLocale
import com.gnoemes.shimori.base.core.settings.AppTheme
import com.gnoemes.shimori.base.core.settings.AppTitlesLocale
import com.gnoemes.shimori.common.ui.components.ShimoriChip
import com.gnoemes.shimori.common.ui.components.ShimoriSecondaryToolbar
import com.gnoemes.shimori.common.ui.noRippleClickable
import com.gnoemes.shimori.common.ui.theme.ShimoriSmallRoundedCornerShape
import com.gnoemes.shimori.common.ui.theme.secondaryColorFromType
import com.gnoemes.shimori.common.ui.utils.shimoriViewModel

@Composable
fun Settings(
    navigateUp: () -> Unit
) {
    Settings(
        navigateUp = navigateUp,
        viewModel = shimoriViewModel()
    )
}

@Composable
private fun Settings(
    navigateUp: () -> Unit,
    viewModel: SettingsViewModel
) {

    val viewState by viewModel.state.collectAsStateWithLifecycle(initialValue = null)

    viewState?.let { state ->
        Settings(
            navigateUp = navigateUp,
            appVersion = state.appVersion,
            titlesLocale = state.titlesLocale,
            onChangeTitlesLocale = { viewModel.onChangeTitlesLocale(it) },
            locale = state.locale,
            onChangeLocale = { viewModel.onChangeLocale(it) },
            showSpoilers = state.showSpoilers,
            onChangeSpoilers = { viewModel.onChangeSpoilers(it) },
            theme = state.theme,
            onChangeTheme = { viewModel.onChangeTheme(it) },
            accentColor = state.accentColor,
            onChangeAccentColor = { viewModel.onChangeSecondaryColor(it) },
        )
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
    Surface {
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

@Composable
private fun ColumnScope.TitlesLocale(
    setting: AppTitlesLocale,
    onChange: (AppTitlesLocale) -> Unit,
) {

    SettingTitle(stringResource(id = R.string.settings_titles))

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        mutableListOf(
            AppTitlesLocale.Russian to stringResource(id = R.string.settings_language_russian),
            AppTitlesLocale.English to stringResource(id = R.string.settings_language_english),
            AppTitlesLocale.Romaji to stringResource(id = R.string.settings_language_romaji),
        )
            .forEach { (value, text) ->
                ShimoriChip(
                    onClick = { onChange(value) },
                    modifier = Modifier.height(32.dp),
                    text = text,
                    selected = value == setting
                )
            }
    }
}


@Composable
private fun ColumnScope.Locale(
    setting: AppLocale,
    onChange: (AppLocale) -> Unit,
) {

    SettingTitle(stringResource(id = R.string.settings_app_language))

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        mutableListOf(
            AppLocale.Russian to stringResource(id = R.string.settings_language_russian),
            AppLocale.English to stringResource(id = R.string.settings_language_english),
        )
            .forEach { (value, text) ->
                ShimoriChip(
                    onClick = { onChange(value) },
                    modifier = Modifier.height(32.dp),
                    text = text,
                    selected = value == setting
                )
            }
    }
}

@Composable
private fun ColumnScope.Spoilers(
    setting: Boolean,
    onChange: (Boolean) -> Unit,
) {

    SettingTitle(stringResource(id = R.string.settings_spoilers))

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        mutableListOf(
            false to stringResource(id = R.string.settings_spoilers_hide),
            true to stringResource(id = R.string.settings_spoilers_show),
        )
            .forEach { (spoiler, text) ->
                ShimoriChip(
                    onClick = { onChange(spoiler) },
                    modifier = Modifier.height(32.dp),
                    text = text,
                    selected = spoiler == setting
                )
            }
    }
}

@Composable
private fun ColumnScope.Theme(
    setting: AppTheme,
    onChange: (AppTheme) -> Unit,
) {

    SettingTitle(stringResource(id = R.string.settings_theme))

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        mutableListOf(
            AppTheme.SYSTEM to stringResource(id = R.string.settings_theme_system),
            AppTheme.LIGHT to stringResource(id = R.string.settings_theme_light),
            AppTheme.DARK to stringResource(id = R.string.settings_theme_dark),
        )
            .forEach { (value, text) ->
                ShimoriChip(
                    onClick = { onChange(value) },
                    modifier = Modifier.height(32.dp),
                    text = text,
                    selected = value == setting
                )
            }
    }
}

@Composable
private fun ColumnScope.AccentColor(
    setting: AppAccentColor,
    onChange: (AppAccentColor) -> Unit,
) {

    SettingTitle(stringResource(id = R.string.settings_accent_color))

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ShimoriChip(
                onClick = { onChange(AppAccentColor.System) },
                modifier = Modifier.height(32.dp),
                text = stringResource(id = R.string.settings_accent_color_system),
                selected = setting == AppAccentColor.System
            )
        }

        mutableListOf(
            AppAccentColor.Red,
            AppAccentColor.Orange,
            AppAccentColor.Yellow,
            AppAccentColor.Green,
            AppAccentColor.Blue,
            AppAccentColor.Purple,
        )
            .forEach { value ->
                AccentColor(
                    color = secondaryColorFromType(value),
                    selected = value == setting,
                    onClick = { onChange(value) }
                )
            }
    }
}

@Composable
private fun ColumnScope.SettingTitle(
    title: String
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.secondary
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun AccentColor(
    color: Color,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .background(color, ShimoriSmallRoundedCornerShape)
            .noRippleClickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Icon(
                painter = painterResource(id = R.drawable.ic_tick),
                contentDescription = null,
                tint = Color.Unspecified
            )
        }
    }
}
