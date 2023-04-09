package com.gnoemes.shimori.lists.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.common.ui.components.ListTypeIcon
import com.gnoemes.shimori.common.ui.components.ShimoriChip
import com.gnoemes.shimori.data.core.entities.track.ListType
import com.gnoemes.shimori.lists.R


@Composable
internal fun ListsEmpty(
    type: ListType,
    onAnimeExplore: () -> Unit,
    onMangaExplore: () -> Unit,
    onRanobeExplore: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        Spacer(
            modifier = Modifier
                .statusBarsPadding()
                .height(128.dp)
        )

        Icon(
            painter = painterResource(
                id = if (type == ListType.Pinned) R.drawable.ic_pin_big
                else R.drawable.ic_list
            ),
            contentDescription = null,
            modifier = Modifier
                .size(96.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(
                id = if (type == ListType.Pinned) R.string.no_pinned_titles
                else R.string.no_titles
            ),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            ShimoriChip(
                onClick = onAnimeExplore,
                modifier = Modifier.height(32.dp),
                text = stringResource(id = R.string.anime),
                icon = {
                    ListTypeIcon(ListType.Anime)
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            ShimoriChip(
                onClick = onMangaExplore,
                modifier = Modifier.height(32.dp),
                text = stringResource(id = R.string.manga),
                icon = {
                    ListTypeIcon(ListType.Manga)
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            ShimoriChip(
                onClick = onRanobeExplore,
                modifier = Modifier.height(32.dp),
                text = stringResource(id = R.string.ranobe),
                icon = {
                    ListTypeIcon(ListType.Ranobe)
                }
            )
        }
    }
}