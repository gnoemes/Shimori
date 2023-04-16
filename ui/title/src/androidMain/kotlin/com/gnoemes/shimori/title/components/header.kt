package com.gnoemes.shimori.title.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.gnoemes.shimori.common.ui.LocalShimoriTextCreator
import com.gnoemes.shimori.common.ui.components.DescriptionFormat
import com.gnoemes.shimori.common.ui.components.EnlargedButton
import com.gnoemes.shimori.common.ui.components.ReleaseDateDescription
import com.gnoemes.shimori.common.ui.components.ShimoriButtonDefaults
import com.gnoemes.shimori.common.ui.components.TitleDescription
import com.gnoemes.shimori.common.ui.components.TitlePropertyInfo
import com.gnoemes.shimori.common.ui.components.TrackIcon
import com.gnoemes.shimori.common.ui.ignoreHorizontalParentPadding
import com.gnoemes.shimori.common.ui.shimoriPlaceholder
import com.gnoemes.shimori.common.ui.statusBarHeight
import com.gnoemes.shimori.common.ui.theme.ShimoriDefaultRoundedCornerShape
import com.gnoemes.shimori.common.ui.theme.accentRed
import com.gnoemes.shimori.common.ui.theme.dimens
import com.gnoemes.shimori.common.ui.utils.rememberDominantColorState
import com.gnoemes.shimori.data.core.entities.ShimoriTitleEntity
import com.gnoemes.shimori.data.core.entities.TitleWithTrackEntity
import com.gnoemes.shimori.data.core.entities.common.ShimoriImage
import com.gnoemes.shimori.data.core.entities.titles.anime.Anime
import com.gnoemes.shimori.data.core.entities.titles.manga.Manga
import com.gnoemes.shimori.data.core.entities.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.gnoemes.shimori.title.R

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun TitleHeader(
    title: TitleWithTrackEntity?,
    openTrackEdit: (Long, TrackTargetType, Boolean) -> Unit,
) {
    Box {
        BackDropImage(title?.entity?.image)
        when (title) {
            null -> TitleHeaderShimmer()
            else -> TitleHeaderContent(title, openTrackEdit)
        }
    }
}

@Composable
private fun BoxScope.TitleHeaderShimmer() {
    Column {
        Spacer(modifier = Modifier.statusBarHeight(additional = 128.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .shimoriPlaceholder(
                    visible = true,
                )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            repeat(2) {
                Box(
                    modifier = Modifier
                        .width(76.dp)
                        .height(34.dp)
                        .shimoriPlaceholder(
                            visible = true,
                        )
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .shimoriPlaceholder(
                        visible = true,
                        shape = ShimoriDefaultRoundedCornerShape
                    )
            )

            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .shimoriPlaceholder(
                        visible = true,
                        shape = ShimoriDefaultRoundedCornerShape
                    )
            )

            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .shimoriPlaceholder(
                        visible = true,
                        shape = ShimoriDefaultRoundedCornerShape
                    )
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun BoxScope.TitleHeaderContent(
    title: TitleWithTrackEntity,
    openTrackEdit: (Long, TrackTargetType, Boolean) -> Unit,
) {
    val textCreator = LocalShimoriTextCreator.current
    Column {
        Spacer(modifier = Modifier.statusBarHeight(additional = 128.dp))
        if (title.entity.isOngoing || (title.entity.rating ?: 0.0) > 0) {
            TitleDescription(
                title = title.entity,
                format = DescriptionFormat.Title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = textCreator.name(title = title.entity),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))
        TitleProperties(title.entity)
        Spacer(modifier = Modifier.height(32.dp))
        TitleActions(
            title = title,
            openListsEdit = openTrackEdit,
            onFavoriteClick = { TODO() },
            onShareClicked = { TODO() })
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun BackDropImage(
    image: ShimoriImage?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .ignoreHorizontalParentPadding(16.dp)
    ) {
        AsyncImage(
            model = image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.dimens.titlePosterHeight)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0f to MaterialTheme.colorScheme.background.copy(alpha = 0.16f),
                            0.56f to MaterialTheme.colorScheme.background,
                        ), tileMode = TileMode.Decal
                    )
                )
                .height(MaterialTheme.dimens.titlePosterHeight)

        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun TitleActions(
    title: TitleWithTrackEntity,
    openListsEdit: (Long, TrackTargetType, Boolean) -> Unit,
    onFavoriteClick: () -> Unit,
    onShareClicked: () -> Unit
) {
    val status = title.track?.status

    val buttonDefaultColor = MaterialTheme.colorScheme.primaryContainer
    val onButtonDefaultColor = MaterialTheme.colorScheme.onPrimaryContainer

    val dominantColors = rememberDominantColorState(
        defaultColor = buttonDefaultColor, defaultOnColor = onButtonDefaultColor
    )

    val statusButtonColor by animateColorAsState(
        targetValue = if (status == null) dominantColors.dominant else buttonDefaultColor,
        animationSpec = tween(150)
    )

    val onStatusButtonColor by animateColorAsState(
        targetValue = if (status == null) dominantColors.onDominant else onButtonDefaultColor,
        animationSpec = tween(150)
    )

    val imageUrl = title.entity.image?.preview
    LaunchedEffect(imageUrl, status) {
        if (imageUrl != null && status == null) {
            dominantColors.updateColorsFromImageUrl(imageUrl)
        } else {
            dominantColors.reset()
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val statusText = if (status == null) stringResource(id = R.string.add)
        else LocalShimoriTextCreator.current.trackStatusText(status = status, type = title.type)

        val buttonColors = ShimoriButtonDefaults.buttonColors(
            containerColor = statusButtonColor, contentColor = onStatusButtonColor
        )

        EnlargedButton(onClick = { openListsEdit(title.id, title.type, false) },
            modifier = Modifier
                .height(48.dp)
                .weight(1f),
            text = statusText,
            buttonColors = buttonColors,
            leftIcon = {
                AnimatedContent(targetState = status) {
                    if (it == null) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        TrackIcon(
                            trackStatus = it, modifier = Modifier.size(24.dp)
                        )
                    }
                }
            })

        EnlargedButton(onClick = onFavoriteClick, modifier = Modifier.size(48.dp), rightIcon = {
            val favoriteColor by animateColorAsState(
                targetValue = if (title.entity.favorite) accentRed else MaterialTheme.colorScheme.onSurfaceVariant,
                animationSpec = tween(500)
            )
            //TODO animate scale?

            Icon(
                painter = painterResource(id = R.drawable.ic_heart),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = favoriteColor
            )
        })

        EnlargedButton(onClick = onShareClicked, modifier = Modifier.size(48.dp), rightIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_share),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        })
    }
}

@Composable
private fun TitleProperties(title: ShimoriTitleEntity) {
    val textCreator = LocalShimoriTextCreator.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.labelMedium) {

            if (title.dateReleased != null) {
                Column {
                    ReleaseDateDescription(title = title)
                    TitlePropertyInfo(text = stringResource(id = R.string.title_release))
                }
            }

            if (title is Anime) {
                val type = textCreator.typeDescription(title)
                if (type != null) {
                    Column {
                        Text(text = type)
                        TitlePropertyInfo(text = stringResource(id = R.string.title_type))
                    }
                }

                if (!title.isMovie) {
                    Column {
                        Text(
                            text = stringResource(
                                id = R.string.progress_format,
                                title.episodesAired,
                                title.episodesOrUnknown
                            )
                        )
                        TitlePropertyInfo(text = stringResource(id = R.string.title_ep))
                    }
                }

                val duration = textCreator.duration(title)
                if (duration != null) {
                    Column {
                        Text(text = duration)
                        TitlePropertyInfo(text = stringResource(id = R.string.title_ep_duration))
                    }
                }

                val ageRating = textCreator.ageRating(title.ageRating)
                if (ageRating != null) {
                    Column {
                        Text(text = ageRating)
                        TitlePropertyInfo(text = stringResource(id = R.string.filter_age_rating))
                    }
                }
            } else if (title is Manga) {
                val type = textCreator.typeDescription(title)
                if (type != null) {
                    Column {
                        Text(text = type)
                        TitlePropertyInfo(text = stringResource(id = R.string.title_type))
                    }
                }

                val ageRating = textCreator.ageRating(title.ageRating)
                if (ageRating != null) {
                    Column {
                        Text(text = ageRating)
                        TitlePropertyInfo(text = stringResource(id = R.string.filter_age_rating))
                    }
                }

            } else if (title is Ranobe) {
                val type = textCreator.typeDescription(title)
                if (type != null) {
                    Column {
                        Text(text = type)
                        TitlePropertyInfo(text = stringResource(id = R.string.title_type))
                    }
                }

                val ageRating = textCreator.ageRating(title.ageRating)
                if (ageRating != null) {
                    Column {
                        Text(text = ageRating)
                        TitlePropertyInfo(text = stringResource(id = R.string.filter_age_rating))
                    }
                }
            }
        }
    }
}