package com.gnoemes.shimori.lists.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.gnoemes.shimori.common.ui.components.BottomSheetTitle
import com.gnoemes.shimori.common.ui.components.ChevronIcon
import com.gnoemes.shimori.common.ui.components.EnlargedButton
import com.gnoemes.shimori.common.ui.navigation.Screen
import com.gnoemes.shimori.data.core.entities.track.ListType
import com.gnoemes.shimori.lists.menu.R
import com.gnoemes.shimori.lists.menu.components.StatusSection

internal class ListMenuScreen : Screen() {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<ListMenuScreenModel>()
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        val type by screenModel.state.collectAsState()

        val navigateUp = {
            bottomSheetNavigator.hide()
        }

        ListMenu(
            type = type,
            onOpenPinedClick = {
                screenModel.openPinned()
                navigateUp()
            },
            onOpenRandomTitleClick = {
                screenModel.openRandomTitle()
                navigateUp()
            },
            navigateUp = navigateUp
        )
    }


    @Composable
    private fun ListMenu(
        type: ListType,
        onOpenPinedClick: () -> Unit,
        onOpenRandomTitleClick: () -> Unit,
        navigateUp: () -> Unit,
    ) {
        Column {
            BottomSheetTitle(text = stringResource(id = R.string.lists_title))

            EnlargedButton(
                selected = type == ListType.Pinned,
                onClick = onOpenPinedClick,
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp, end = 12.dp)
                    .height(48.dp)
                    .fillMaxWidth(),
                text = stringResource(id = R.string.pinned),
                leftIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_pin),
                        contentDescription = stringResource(id = R.string.pinned)
                    )
                }
            )

            EnlargedButton(
                onClick = onOpenRandomTitleClick,
                modifier = Modifier
                    .padding(start = 16.dp, top = 12.dp, end = 12.dp)
                    .height(48.dp)
                    .fillMaxWidth(),
                text = stringResource(R.string.open_random_title_from_list),
                leftIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_random),
                        contentDescription = stringResource(R.string.open_random_title_from_list)
                    )
                },
                rightIcon = {
                    ChevronIcon()
                }
            )

            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.onSurface,
            ) {
                StatusSection(type = ListType.Anime, navigateUp = navigateUp)
                StatusSection(type = ListType.Manga, navigateUp = navigateUp)
                StatusSection(type = ListType.Ranobe, navigateUp = navigateUp)
            }


            Spacer(
                modifier = Modifier
                    .navigationBarsPadding()
                    .height(24.dp)
            )
        }
    }
}