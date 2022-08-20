@file:OptIn(ExperimentalLifecycleComposeApi::class)

package com.gnoemes.shimori.lists.change

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gnoemes.shimori.common.ui.components.BottomSheetTitle
import com.gnoemes.shimori.common.ui.components.ChevronIcon
import com.gnoemes.shimori.common.ui.components.EnlargedButton
import com.gnoemes.shimori.common.ui.utils.shimoriViewModel
import com.gnoemes.shimori.data.core.entities.rate.ListType
import com.gnoemes.shimori.lists.change.section.StatusSection

@Composable
fun ListsChangeSheet(
    navigateUp: () -> Unit
) {
    ListsChange(viewModel = shimoriViewModel(), navigateUp)
}

@Composable
private fun ListsChange(
    viewModel: ListsChangeViewModel,
    navigateUp: () -> Unit
) {
    val type by viewModel.type.collectAsStateWithLifecycle()

    ListsChange(
        type = type,
        onOpenPinedClick = { viewModel.openPinned(); navigateUp() },
        onOpenRandomTitleClick = { viewModel.openRandomTitle(); navigateUp() },
        navigateUp = navigateUp,
    )
}

@Composable
private fun ListsChange(
    type : ListType,
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