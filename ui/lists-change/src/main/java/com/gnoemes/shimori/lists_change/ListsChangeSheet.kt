package com.gnoemes.shimori.lists_change

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.hilt.navigation.compose.hiltViewModel
import com.gnoemes.shimori.common.compose.BottomSheetThumb
import com.gnoemes.shimori.common.compose.ChevronIcon
import com.gnoemes.shimori.common.compose.EnlargedButton
import com.gnoemes.shimori.model.rate.ListType
import com.google.accompanist.insets.navigationBarsHeight

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
    val type = viewModel.currentType

    val submit = { action: ListsChangeAction ->
        viewModel.submitAction(action)
        navigateUp()
    }

    ListsChange(
            type = type,
            onOpenRandomTitleClick = { submit(ListsChangeAction.Random) },
            onTypeSelected = { newType -> submit(ListsChangeAction.ChangeListType(newType)) },
    )
}

@Composable
private fun ListsChange(
    type: ListType,
    onOpenRandomTitleClick: () -> Unit,
    onTypeSelected: (ListType) -> Unit
) {

    Column {
        BottomSheetThumb(withSpace = false)

        Text(
                text = stringResource(id = R.string.titles),
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier
                    .padding(vertical = 12.dp, horizontal = 16.dp)
                    .fillMaxWidth()
        )

        EnlargedButton(
                selected = false,
                onClick = onOpenRandomTitleClick,
                painter = painterResource(R.drawable.ic_random),
                text = stringResource(R.string.open_random_title_from_list),
                modifier = Modifier
                    .padding(start = 16.dp, top = 12.dp, end = 12.dp)
                    .height(48.dp)
                    .fillMaxWidth()
        ) {
            ChevronIcon()
        }

        listChangeButtons.fastForEach { data ->
            EnlargedButton(
                    selected = type == data.type,
                    onClick = { onTypeSelected(data.type) },
                    painter = painterResource(data.iconResId),
                    text = stringResource(data.textResId),
                    modifier = Modifier
                        .padding(start = 16.dp, top = 12.dp, end = 12.dp)
                        .height(48.dp)
                        .fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.navigationBarsHeight(additional = 8.dp))
    }
}

private data class ListTypeButton(
    val type: ListType,
    @DrawableRes val iconResId: Int,
    @StringRes val textResId: Int
)

private val listChangeButtons
    get() = listOf(
            ListTypeButton(
                    ListType.Pinned,
                    R.drawable.ic_pin,
                    R.string.pinned
            ),
            ListTypeButton(
                    ListType.Anime,
                    R.drawable.ic_anime,
                    R.string.anime
            ),
            ListTypeButton(
                    ListType.Manga,
                    R.drawable.ic_manga,
                    R.string.manga
            ),
            ListTypeButton(
                    ListType.Ranobe,
                    R.drawable.ic_ranobe,
                    R.string.ranobe
            ),
    )