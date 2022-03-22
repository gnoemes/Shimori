package com.gnoemes.shimori.lists_change

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gnoemes.shimori.common.compose.ui.BottomSheetTitle
import com.gnoemes.shimori.common.compose.ui.ChevronIcon
import com.gnoemes.shimori.common.compose.ui.EnlargedButton
import com.gnoemes.shimori.lists_change.section.StatusSection
import com.gnoemes.shimori.model.rate.ListType

@Composable
fun ListsChangeSheet(
    navigateUp: () -> Unit
) {
    ListsChange(viewModel = hiltViewModel(), navigateUp)
}

@Composable
private fun ListsChange(
    viewModel: ListsChangeViewModel,
    navigateUp: () -> Unit
) {
    ListsChange(
        onOpenPinedClick = { viewModel.openPinned(); navigateUp() },
        onOpenRandomTitleClick = { viewModel.openRandomTitle(); navigateUp() },
        navigateUp = navigateUp,
    )
}

@Composable
private fun ListsChange(
    onOpenPinedClick: () -> Unit,
    onOpenRandomTitleClick: () -> Unit,
    navigateUp: () -> Unit,
) {
    Column {
        BottomSheetTitle(text = stringResource(id = R.string.lists_title))

        EnlargedButton(
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