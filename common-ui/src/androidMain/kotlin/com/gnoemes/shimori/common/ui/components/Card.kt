package com.gnoemes.shimori.common.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.common.ui.LocalShimoriTextCreator
import com.gnoemes.shimori.common.ui.noRippleClickable
import com.gnoemes.shimori.common.ui.theme.dimens
import com.gnoemes.shimori.data.core.entities.TitleWithTrackEntity
import com.gnoemes.shimori.data.core.entities.common.ShimoriImage
import com.gnoemes.shimori.data.core.entities.common.TitleStatus
import com.gnoemes.shimori.data.core.entities.titles.anime.Anime
import com.gnoemes.shimori.data.core.entities.titles.manga.Manga
import com.gnoemes.shimori.data.core.entities.titles.ranobe.Ranobe
import com.gnoemes.shimori.ui.R

@Composable
fun ListCard(
    title: TitleWithTrackEntity,
    onClick: () -> Unit,
    onCoverLongClick: () -> Unit,
    onEditClick: () -> Unit,
    onIncrementClick: () -> Unit,
    padding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
) {
    Box(
        modifier = Modifier.padding(padding)
    ) {
        ListCard(
            image = title.entity.image,
            name = LocalShimoriTextCreator.current.name(title.entity),
            description = {
                TitleDescription(
                    title = title.entity,
                    format = DescriptionFormat.List,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            score = title.track?.score,
            progress = when (val entity = title.entity) {
                is Anime -> stringResource(
                    R.string.progress_format,
                    title.track?.progress ?: 0,
                    entity.episodesOrUnknown
                )
                is Manga -> stringResource(
                    R.string.progress_format,
                    title.track?.progress ?: 0,
                    entity.chaptersOrUnknown
                )
                is Ranobe -> stringResource(
                    R.string.progress_format,
                    title.track?.progress ?: 0,
                    entity.chaptersOrUnknown
                )
                else -> throw IllegalArgumentException("Entity $entity does not support")
            },
            isPinned = title.pinned,
            showIncrementer = title.track?.progress != null
                    && title.entity.size != null
                    && title.track?.progress != title.entity.size
                    && title.entity.status != TitleStatus.ANONS
                    || title.entity.isOngoing,
            onClick = onClick,
            onCoverLongClick = onCoverLongClick,
            onEditClick = onEditClick,
            onIncrementClick = onIncrementClick,
        )
    }
}

@Composable
fun ListCard(
    image: ShimoriImage?,
    name: String,
    description: @Composable () -> Unit,
    score: Int?,
    progress: String,
    isPinned: Boolean,
    showIncrementer: Boolean,
    onClick: () -> Unit,
    onCoverLongClick: () -> Unit,
    onEditClick: () -> Unit,
    onIncrementClick: () -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(MaterialTheme.dimens.listPosterHeight)
            .noRippleClickable(onClick = onClick)
        ,
    ) {
        Cover(
            image = image,
            isPinned = isPinned,
            onLongClick = onCoverLongClick,
            onClick = onClick,
            modifier = Modifier
                .height(MaterialTheme.dimens.listPosterHeight)
                .width(MaterialTheme.dimens.listPosterWidth)
        )

        Spacer(Modifier.width(16.dp))

        Column {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = name,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(2.dp))

            description()

            Spacer(Modifier.height(10.dp))

            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.primary,
                LocalTextStyle provides MaterialTheme.typography.bodyMedium
            ) {
                Row {
                    if (score != null && score > 0) {
                        Icon(
                            painter = painterResource(R.drawable.ic_star),
                            modifier = Modifier.align(Alignment.CenterVertically),
                            contentDescription = null
                        )

                        Text(text = "$score")

                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Text(text = progress)
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxSize()
            ) {
                ShimoriCircleButton(
                    onClick = onEditClick,
                    modifier = Modifier
                        .size(32.dp),
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_edit),
                            contentDescription = null
                        )
                    }
                )

                if (showIncrementer) {
                    ShimoriCircleButton(
                        onClick = onIncrementClick,
                        modifier = Modifier
                            .size(32.dp),
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_add_one),
                                contentDescription = stringResource(id = R.string.add)
                            )
                        }
                    )
                }
            }
        }
    }
}