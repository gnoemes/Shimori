@file:OptIn(ExperimentalLayoutApi::class)

package com.gnoemes.shimori.settings

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowOverflow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.common.compose.LocalWindowSizeClass
import com.gnoemes.shimori.common.compose.NestedScaffold
import com.gnoemes.shimori.common.compose.ui.ListItem
import com.gnoemes.shimori.common.compose.ui.ShimoriCard
import com.gnoemes.shimori.common.compose.ui.TransparentToolbar
import com.gnoemes.shimori.common.compose.ui.gradientBackground
import com.gnoemes.shimori.common.ui.resources.Icons
import com.gnoemes.shimori.common.ui.resources.icons.ic_appearence
import com.gnoemes.shimori.common.ui.resources.icons.ic_back
import com.gnoemes.shimori.common.ui.resources.icons.ic_github
import com.gnoemes.shimori.common.ui.resources.icons.ic_shikimori
import com.gnoemes.shimori.common.ui.resources.icons.ic_shimori_logo
import com.gnoemes.shimori.common.ui.resources.strings.feature_work_in_progress
import com.gnoemes.shimori.common.ui.resources.strings.settings
import com.gnoemes.shimori.common.ui.resources.strings.settings_appearence
import com.gnoemes.shimori.common.ui.resources.strings.settings_backup
import com.gnoemes.shimori.common.ui.resources.strings.settings_feedback_and_updates
import com.gnoemes.shimori.common.ui.resources.strings.settings_feedback_and_updates_github
import com.gnoemes.shimori.common.ui.resources.strings.settings_feedback_and_updates_github_description
import com.gnoemes.shimori.common.ui.resources.strings.settings_special_thanks
import com.gnoemes.shimori.common.ui.resources.strings.settings_special_thanks_shikimori
import com.gnoemes.shimori.common.ui.resources.strings.settings_watching
import com.gnoemes.shimori.common.ui.resources.util.Strings
import com.gnoemes.shimori.screens.SettingsAppearanceScreen
import com.gnoemes.shimori.screens.SettingsScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.CircuitContent
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
@CircuitInject(screen = SettingsScreen::class, scope = UiScope::class)
internal fun SettingsUi(
    state: SettingsUiState,
    modifier: Modifier = Modifier
) {
    val windowSizeClass = LocalWindowSizeClass.current
    val viewType = remember(windowSizeClass) {
        when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> SettingsViewType.Grid
            else -> SettingsViewType.List
        }
    }

    val eventSink = state.eventSink

    SettingsUi(
        appName = state.appName,
        versionName = state.versionName,
        navigateUpEnabled = state.navigateUpEnabled,
        viewType = viewType,
        openAppearenceSettings = { eventSink(SettingsUiEvent.OpenAppearenceSettings) },
        openGithub = { eventSink(SettingsUiEvent.OpenGithub) },
        openShikimori = { eventSink(SettingsUiEvent.OpenShikimori) },
        navigateUp = { eventSink(SettingsUiEvent.NavigateUp) }
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalLayoutApi::class)
@Composable
private fun SettingsUi(
    appName: String,
    versionName: String,
    navigateUpEnabled : Boolean,
    viewType: SettingsViewType,
    openAppearenceSettings: () -> Unit,
    openGithub: () -> Unit,
    openShikimori: () -> Unit,
    navigateUp: () -> Unit
) {
    NestedScaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
                TransparentToolbar(
                    title = {
                        if (viewType == SettingsViewType.Grid) {
                            Text(stringResource(Strings.settings))
                        }
                    },
                    navigationIcon = {
                        if (navigateUpEnabled) {
                            IconButton(
                                onClick = navigateUp,
                            ) {
                                Icon(painterResource(Icons.ic_back), contentDescription = null)
                            }
                        }
                    },
                    onNavigationClick = navigateUp
                )
        }
    ) { paddingValues ->
        AnimatedContent(viewType) {
            if (viewType == SettingsViewType.Grid) {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState()),
                ) {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        maxItemsInEachRow = 2,
                        overflow = FlowRowOverflow.Visible
                    ) {
                        FlowColumn(
                            modifier = Modifier
                                .widthIn(max = 720.dp)
                                .weight(1f, fill = false),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            AboutCard(
                                appName,
                                versionName
                            )
                            AppearanceCard()
                            BackupCard()
                        }

                        FlowColumn(
                            modifier = Modifier
                                .widthIn(max = 720.dp)
                                .weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            FeedbackCard(openGithub)
                            ThanksCard(openShikimori)
                            WatchingCard()
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                }
            } else {
                SettingsUiList(
                    paddingValues,
                    modifier = Modifier
                        .fillMaxSize(),
                    appName = appName,
                    versionName = versionName,
                    openAppearenceSettings = openAppearenceSettings,
                    openGithub = openGithub,
                    openShikimori = openShikimori
                )
            }
        }
    }
}


@Composable
private fun SettingsUiList(
    paddings: PaddingValues,
    modifier: Modifier,
    appName: String,
    versionName: String,
    openAppearenceSettings: () -> Unit,
    openGithub: () -> Unit,
    openShikimori: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxSize()
            .gradientBackground(painterResource(Icons.ic_shimori_logo)),
        horizontalArrangement = Arrangement.Center
    ) {
        Column(modifier = modifier) {
            Spacer(modifier = Modifier.height(paddings.calculateTopPadding()))

            Spacer(modifier = Modifier.height(8.dp))

            About(appName, versionName)

            Spacer(modifier = Modifier.height(32.dp))

            ListItem(
                painterResource(Icons.ic_appearence),
                text = stringResource(Strings.settings_appearence),
                onClick = openAppearenceSettings
            )

            Spacer(Modifier.height(24.dp))

            Text(
                stringResource(Strings.settings_feedback_and_updates),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(8.dp))

            ListItem(
                painterResource(Icons.ic_github),
                text = stringResource(Strings.settings_feedback_and_updates_github),
                description = stringResource(Strings.settings_feedback_and_updates_github_description),
                onClick = openGithub
            )

            Spacer(Modifier.height(24.dp))

            Text(
                stringResource(Strings.settings_special_thanks),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(8.dp))

            ListItem(
                painterResource(Icons.ic_shikimori),
                text = stringResource(Strings.settings_special_thanks_shikimori),
                onClick = openShikimori
            )
        }
    }
}

@Composable
private fun AboutCard(
    appName: String,
    versionName: String
) {
    ShimoriCard(
        Modifier
            .widthIn(240.dp, 720.dp)
            .heightIn(min = 240.dp, 240.dp)
            .fillMaxWidth(),
        gradientPainter = painterResource(Icons.ic_shimori_logo),
        tonalElevation = 0.dp
    ) {
        Spacer(Modifier.weight(1f))
        About(appName, versionName)
        Spacer(Modifier.weight(1f))
    }
}


@Composable
private fun FeedbackCard(
    openGithub: () -> Unit,
) {
    ShimoriCard(
        Modifier
            .widthIn(240.dp, 720.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        title = {
            Text(stringResource(Strings.settings_feedback_and_updates))
        }
    ) {
        ListItem(
            painterResource(Icons.ic_github),
            text = stringResource(Strings.settings_feedback_and_updates_github),
            description = stringResource(Strings.settings_feedback_and_updates_github_description),
            onClick = openGithub
        )
    }
}

@Composable
private fun ThanksCard(
    openShikimori: () -> Unit,
) {
    ShimoriCard(
        Modifier
            .widthIn(240.dp, 720.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        title = {
            Text(stringResource(Strings.settings_special_thanks))
        }
    ) {
        ListItem(
            painterResource(Icons.ic_shikimori),
            text = stringResource(Strings.settings_special_thanks_shikimori),
            onClick = openShikimori
        )
    }
}

@Composable
private fun AppearanceCard() {
    ShimoriCard(
        Modifier.widthIn(240.dp, 720.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        title = {
            Text(stringResource(Strings.settings_appearence))
        }
    ) {
        CircuitContent(SettingsAppearanceScreen(card = true))
    }
}

@Composable
private fun BackupCard() {
    ShimoriCard(
        Modifier.widthIn(240.dp, 720.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        title = {
            Text(stringResource(Strings.settings_backup))
        }
    ) {
        Modifier.weight(1f)
        Text(
            stringResource(Strings.feature_work_in_progress),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Modifier.weight(1f)
    }
}

@Composable
private fun WatchingCard() {
    ShimoriCard(
        Modifier.widthIn(240.dp, 720.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        title = {
            Text(stringResource(Strings.settings_watching))
        }
    ) {
        Modifier.weight(1f)
        Text(
            stringResource(Strings.feature_work_in_progress),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Modifier.weight(1f)
    }
}

@Composable
private fun ColumnScope.About(
    appName: String,
    versionName: String
) {
    Image(
        painterResource(Icons.ic_shimori_logo),
        contentDescription = null,
        modifier = Modifier.align(Alignment.CenterHorizontally)
    )
    Spacer(modifier = Modifier.height(16.dp))

    Text(
        "$appName $versionName",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.align(Alignment.CenterHorizontally)
    )
}


internal enum class SettingsViewType {
    List,
    Grid
}