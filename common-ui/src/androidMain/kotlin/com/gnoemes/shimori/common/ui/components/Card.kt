package com.gnoemes.shimori.common.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.common.ui.LocalShimoriTextCreator
import com.gnoemes.shimori.common.ui.noRippleClickable
import com.gnoemes.shimori.common.ui.theme.ShimoriDefaultRoundedCornerShape
import com.gnoemes.shimori.common.ui.theme.dimens
import com.gnoemes.shimori.data.core.entities.TitleWithTrackEntity
import com.gnoemes.shimori.data.core.entities.common.ShimoriImage
import com.gnoemes.shimori.data.core.entities.common.TitleStatus
import com.gnoemes.shimori.data.core.entities.titles.anime.Anime
import com.gnoemes.shimori.data.core.entities.titles.manga.Manga
import com.gnoemes.shimori.data.core.entities.titles.ranobe.Ranobe
import com.gnoemes.shimori.ui.R

@Composable
fun NavigationCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier then Modifier
            .background(MaterialTheme.colorScheme.surface)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.12f),
                ShimoriDefaultRoundedCornerShape
            )
            .clip(ShimoriDefaultRoundedCornerShape)
    ) {
        Column {
            content()
        }
    }
}

@Composable
fun ColumnScope.NavigationCardItem(
    title: String,
    onClick: () -> Unit,
    subTitle: String? = null,
    icon: (@Composable () -> Unit)? = null,
    button: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.onSurface
        ) {
            if (icon != null) {
                icon()
                Spacer(modifier = Modifier.width(12.dp))
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall
                )

                if (subTitle != null) {
                    Text(
                        text = subTitle,
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            if (button != null) {
                Spacer(modifier = Modifier.width(12.dp))
                button()
            }
        }
    }
}

@Composable
fun ColumnScope.NavigationCardDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
    )
}

@Composable
fun ListCard(
    modifier: Modifier = Modifier,
    title: TitleWithTrackEntity,
    onClick: () -> Unit,
    onCoverLongClick: () -> Unit,
    onEditClick: () -> Unit,
    onIncrementClick: () -> Unit,
    padding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
) {
    Box(
        modifier = modifier then Modifier.padding(padding)
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
            .noRippleClickable(onClick = onClick),
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

@Composable
fun CharacterCard(
    image: ShimoriImage?,
    name: String,
    onClick: () -> Unit
) {
    Column {
        CharacterCover(
            image,
            modifier = Modifier
                .width(MaterialTheme.dimens.characterPosterWidth)
                //3:4
                .aspectRatio(0.75f),
            onClick = onClick,
            contentDescription = name
        )
        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier.width(MaterialTheme.dimens.characterPosterWidth)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .align(Alignment.Center),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun TrailerCard(
    image: String?,
    name: String,
    hosting: String?,
    onClick: () -> Unit,
) {
    Column {
        Box {
            TrailerCover(
                image,
                modifier = Modifier
                    .width(MaterialTheme.dimens.trailerPosterWidth)
                    .height(MaterialTheme.dimens.trailerPosterHeight),
                onClick = onClick,
                contentDescription = name
            )
            ShimoriCircleButton(
                onClick = onClick,
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.Center),
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_play),
                        contentDescription = null
                    )
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier.width(MaterialTheme.dimens.trailerPosterWidth)
        ) {
            TitleTrailerInfo(name = name, hosting = hosting)
        }
    }
}