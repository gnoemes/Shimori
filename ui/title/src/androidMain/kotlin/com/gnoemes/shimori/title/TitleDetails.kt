package com.gnoemes.shimori.title

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Surface
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.gnoemes.shimori.common.ui.*
import com.gnoemes.shimori.common.ui.api.OptionalContent
import com.gnoemes.shimori.common.ui.components.*
import com.gnoemes.shimori.common.ui.theme.ShimoriCharacterCoverRoundedCornerShape
import com.gnoemes.shimori.common.ui.theme.accentRed
import com.gnoemes.shimori.common.ui.theme.dimens
import com.gnoemes.shimori.common.ui.utils.rememberDominantColorState
import com.gnoemes.shimori.common.ui.utils.shimoriViewModel
import com.gnoemes.shimori.data.core.entities.ShimoriTitleEntity
import com.gnoemes.shimori.data.core.entities.TitleWithTrackEntity
import com.gnoemes.shimori.data.core.entities.characters.Character
import com.gnoemes.shimori.data.core.entities.common.ShimoriImage
import com.gnoemes.shimori.data.core.entities.titles.anime.Anime
import com.gnoemes.shimori.data.core.entities.titles.manga.Manga
import com.gnoemes.shimori.data.core.entities.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType

@Composable
fun TitleDetails(
    navigateUp: () -> Unit,
    openListsEdit: (Long, TrackTargetType, Boolean) -> Unit,
    openCharacterDetails: (id: Long) -> Unit,
    openCharacterList: (id: Long, type: TrackTargetType) -> Unit
) {
    TitleDetails(
        viewModel = shimoriViewModel(),
        navigateUp = navigateUp,
        openListsEdit = openListsEdit,
        openCharacterDetails = openCharacterDetails,
        openCharacterList = openCharacterList
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TitleDetails(
    viewModel: TitleDetailsViewModel,
    navigateUp: () -> Unit,
    openListsEdit: (Long, TrackTargetType, Boolean) -> Unit,
    openCharacterDetails: (id: Long) -> Unit,
    openCharacterList: (id: Long, type: TrackTargetType) -> Unit
) {
    val state by viewModel.state.collectAsState()

    val title = state.title ?: return

    val scrollState = rememberLazyListState()

    ScaffoldExtended(modifier = Modifier.fillMaxSize(), topBar = {
        //TODO scroll condition
        val toolbarTitle = ""
        val colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.96f),
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface
        )

        ShimoriSecondaryToolbar(modifier = Modifier.statusBarsPadding(),
            navigateUp = navigateUp,
            title = toolbarTitle,
            colors = colors,
            actions = {
//                    TODO actions
            })
    }) { paddingValues ->
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            TitleContent(
                modifier = Modifier.fillMaxSize(),
                state = scrollState,
                title = title,
                characters = state.characters,
                openListsEdit = openListsEdit,
                openCharacterDetails = openCharacterDetails
            ) { openCharacterList(title.id, title.type) }
        }
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

@Composable
private fun TitleContent(
    modifier: Modifier,
    state: LazyListState,
    title: TitleWithTrackEntity,
    characters: OptionalContent<List<Character>?>,
    openListsEdit: (Long, TrackTargetType, Boolean) -> Unit,
    openCharacterDetails: (id: Long) -> Unit,
    openCharacterList: () -> Unit
) {
    val textCreator = LocalShimoriTextCreator.current

    LazyColumn(
        modifier = modifier, state = state, contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item {
            Box {
                BackDropImage(title.entity.image)
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
                        openListsEdit = openListsEdit,
                        onFavoriteClick = { TODO() },
                        onShareClicked = { TODO() })
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }


        if (!characters.loaded || !characters.content.isNullOrEmpty()) {
            item {
                Characters(
                    characters = characters,
                    openCharacterDetails = openCharacterDetails,
                    openCharacterList = openCharacterList,
                )
            }

            itemSpacer(32.dp)
        }

        if (!title.entity.description.isNullOrBlank() || !title.entity.genres.isNullOrEmpty()) {
            item {
                About(title = title.entity)
            }
        }

        itemSpacer(32.dp)
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

@Composable
private fun Characters(
    characters: OptionalContent<List<Character>?>,
    openCharacterDetails: (id: Long) -> Unit,
    openCharacterList: () -> Unit
) {
    RowContentSection(
        title = stringResource(id = R.string.title_characters),
        isMoreVisible = (characters.content?.size ?: 0) > 6,
        onClickMore = { if (characters.loaded) openCharacterList() }
    ) {
        if (!characters.loaded) {
            repeat(5) {
                CharacterCover(
                    null,
                    modifier = Modifier
                        .shimoriPlaceholder(
                            true,
                            shape = ShimoriCharacterCoverRoundedCornerShape
                        )
                        .width(MaterialTheme.dimens.characterPosterWidth)
                        .aspectRatio(0.75f),
                )
            }
        } else {
            characters.content?.forEach {
                CharacterCard(it.image,
                    LocalShimoriTextCreator.current.name(it),
                    onClick = { openCharacterDetails.invoke(it.id) })
            }
        }
    }
}

@Composable
private fun About(
    title: ShimoriTitleEntity,
    //TODO restore
//    onGenreClick: (Genre) -> Unit
) {
    RowContentSection(
        title = stringResource(id = R.string.title_about),
        isMoreVisible = false,
        onClickMore = { },
        nonRowContent = {
            val description = title.description
            if (!description.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = description,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        },
        contentHorizontalArrangement = 8.dp
    ) {
        val genres = title.genres
        if (!genres.isNullOrEmpty()) {
            genres.forEach {
                ShimoriChip(
                    onClick = {},
                    modifier = Modifier
                        .height(32.dp),
                    text = LocalShimoriTextCreator.current.genre(it),
                )
            }
        }
    }
}

@Composable
private fun RowContentSection(
    title: String,
    isMoreVisible: Boolean,
    onClickMore: () -> Unit,
    nonRowContent: @Composable ColumnScope.() -> Unit = { },
    contentHorizontalArrangement: Dp = 12.dp,
    content: @Composable RowScope.() -> Unit
) {
    Column {
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.onBackground
        ) {
            Row(
                modifier = Modifier.clickable { onClickMore() }
            ) {
                CompositionLocalProvider(
                    LocalTextStyle provides MaterialTheme.typography.titleMedium
                ) {
                    Text(
                        text = title,
                        modifier = Modifier
                            .weight(1f)
                    )
                }

                if (isMoreVisible) {
                    Spacer(modifier = Modifier.width(12.dp))
                    IconButton(
                        onClick = onClickMore, modifier = Modifier.size(24.dp)
                    ) {
                        ChevronIcon()
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val screenPadding = 16.dp

            Row(
                modifier = Modifier
                    .ignoreHorizontalParentPadding(screenPadding)
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(contentHorizontalArrangement)
            ) {
                Spacer(modifier = Modifier.width(screenPadding - contentHorizontalArrangement))
                content()
                Spacer(modifier = Modifier.width(screenPadding - contentHorizontalArrangement))
            }

            nonRowContent()
        }
    }
}

