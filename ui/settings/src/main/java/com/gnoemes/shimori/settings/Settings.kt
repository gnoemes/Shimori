package com.gnoemes.shimori.settings

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gnoemes.shimori.base.settings.AppAccentColor
import com.gnoemes.shimori.base.settings.AppLocale
import com.gnoemes.shimori.base.settings.AppTheme
import com.gnoemes.shimori.base.settings.AppTitlesLocale
import com.gnoemes.shimori.common.compose.ShimoriChip
import com.gnoemes.shimori.common.compose.ShimoriSecondaryToolbar
import com.gnoemes.shimori.common.compose.noRippleClickable
import com.gnoemes.shimori.common.compose.theme.ShimoriSmallRoundedCornerShape
import com.gnoemes.shimori.common.compose.theme.secondaryColorFromType
import com.gnoemes.shimori.common.extensions.collectAsStateWithLifecycle
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun Settings(
    navigateUp: () -> Unit
) {
    Settings(
        navigateUp = navigateUp,
        viewModel = hiltViewModel()
    )
}

@Composable
private fun Settings(
    navigateUp: () -> Unit,
    viewModel: SettingsViewModel
) {
    val viewState = viewModel.state.collectAsStateWithLifecycle(initial = null).value

    viewState?.let {
        Settings(
            navigateUp = navigateUp,
            appVersion = viewState.appVersion,
            titlesLocale = viewState.titlesLocale,
            onChangeTitlesLocale = { viewModel.onChangeTitlesLocale(it) },
            locale = viewState.locale,
            onChangeLocale = { viewModel.onChangeLocale(it) },
            showSpoilers = viewState.showSpoilers,
            onChangeSpoilers = { viewModel.onChangeSpoilers(it) },
            theme = viewState.theme,
            onChangeTheme = { viewModel.onChangeTheme(it) },
            accentColor = viewState.accentColor,
            onChangeAccentColor = { viewModel.onChangeSecondaryColor(it) },
        )
    }
}

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
            subTitle = appVersion
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