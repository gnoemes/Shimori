package com.gnoemes.shimori.title.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.LazyPagingItems
import coil3.compose.AsyncImage
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.common.compose.LocalShimoriIconsUtil
import com.gnoemes.shimori.common.compose.LocalShimoriTextCreator
import com.gnoemes.shimori.common.compose.LocalWindowSizeClass
import com.gnoemes.shimori.common.compose.NestedScaffold
import com.gnoemes.shimori.common.compose.calculateBottomWithAdditional
import com.gnoemes.shimori.common.compose.isCompact
import com.gnoemes.shimori.common.compose.itemSpacer
import com.gnoemes.shimori.common.compose.mouseWheelNestedScrollConnectionFix
import com.gnoemes.shimori.common.compose.ui.imageGradientBackground
import com.gnoemes.shimori.common.ui.resources.Icons
import com.gnoemes.shimori.common.ui.resources.icons.ic_back
import com.gnoemes.shimori.data.ShimoriTitleEntity
import com.gnoemes.shimori.data.common.Genre
import com.gnoemes.shimori.data.common.Studio
import com.gnoemes.shimori.data.person.Person
import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.screens.TitleDetailsScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import org.jetbrains.compose.resources.painterResource

@Composable
@CircuitInject(screen = TitleDetailsScreen::class, scope = UiScope::class)
internal fun TitleDetailsUi(
    state: TitleDetailsUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink

    TitleDetailsUi(
        title = state.title,
        track = state.track,
        genres = state.genres,
        isFavorite = state.isFavorite,
        isShowCharacters = state.isShowCharacters,
        isFramesExists = state.isFramesExists,
        isShowTrailers = state.isShowTrailers,
        isTranslatorsExists = state.isTranslatorsExists,
        studios = state.studios,
        persons = state.persons,

        toggleFavorite = { eventSink(TitleDetailsUiEvent.ToggleFavorite) },
        expandDescription = { eventSink(TitleDetailsUiEvent.ExpandDescription) },
        share = { eventSink(TitleDetailsUiEvent.Share) },
        openInBrowser = { eventSink(TitleDetailsUiEvent.OpenInBrowser) },
        openCharactersList = { eventSink(TitleDetailsUiEvent.OpenCharactersList) },
        openFrames = { eventSink(TitleDetailsUiEvent.OpenFrames) },
        openTrailers = { eventSink(TitleDetailsUiEvent.OpenTrailers) },
        openTranslators = { eventSink(TitleDetailsUiEvent.OpenTranslators) },
        openChronology = { eventSink(TitleDetailsUiEvent.OpenChronology) },
        navigateUp = { eventSink(TitleDetailsUiEvent.NavigateUp) },

        openEditTrack = { id, type -> eventSink(TitleDetailsUiEvent.OpenEditTrack(id, type)) },
        openGenreSearch = { eventSink(TitleDetailsUiEvent.OpenGenreSearch(it)) },
        openStudioSearch = { eventSink(TitleDetailsUiEvent.OpenStudioSearch(it)) },
        openPerson = { eventSink(TitleDetailsUiEvent.OpenPerson(it)) },
        openTitle = { id, type -> eventSink(TitleDetailsUiEvent.OpenTitle(id, type)) },
    )
}

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
private fun TitleDetailsUi(
    title: ShimoriTitleEntity?,
    track: Track?,
    genres: List<Genre>,
    isFavorite: Boolean,
    isShowCharacters: Boolean,
    isFramesExists: Boolean,
    isShowTrailers: Boolean,
    isTranslatorsExists: Boolean,

    studios: List<Studio>,
    persons: LazyPagingItems<Person>,

    toggleFavorite: () -> Unit,
    expandDescription: () -> Unit,
    share: () -> Unit,
    openInBrowser: () -> Unit,
    openCharactersList: () -> Unit,
    openFrames: () -> Unit,
    openTrailers: () -> Unit,
    openTranslators: () -> Unit,
    openChronology: () -> Unit,
    navigateUp: () -> Unit,

    openEditTrack: (Long, TrackTargetType) -> Unit,
    openGenreSearch: (Long) -> Unit,
    openStudioSearch: (String) -> Unit,
    openPerson: (Long) -> Unit,
    openTitle: (Long, TrackTargetType) -> Unit,
) {
    val behavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val scrollBehavior by remember(behavior) { mutableStateOf(behavior) }
    val textCreator = LocalShimoriTextCreator.current


    NestedScaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            val isCollapsed by remember(scrollBehavior.state) { derivedStateOf { scrollBehavior.state.collapsedFraction == 1f } }
            val isHalfCollapsed by remember(scrollBehavior.state) {
                derivedStateOf { scrollBehavior.state.collapsedFraction >= 0.5f }
            }

            val animateColor by animateColorAsState(
                when {
                    scrollBehavior.state.collapsedFraction <= .1f -> Color.Transparent
                    else -> MaterialTheme.colorScheme.surface.copy(alpha = scrollBehavior.state.collapsedFraction)
                }
            )

            MediumTopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors().copy(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = animateColor,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                expandedHeight = 196.dp,
                scrollBehavior = scrollBehavior,
                title = {
                    Box(
                        modifier = Modifier.fillMaxSize()
                            .animateContentSize(),
                    ) {
                        textCreator.nullable { title?.name() }?.let {

                            if (isCollapsed || isHalfCollapsed) {
                                Text(
                                    it,
                                    modifier = Modifier.align(Alignment.CenterStart),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            } else {
                                Text(
                                    it,
                                    modifier = Modifier.align(Alignment.BottomStart),
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 4,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }

                    }
                },
                navigationIcon = {
                    IconButton(navigateUp) {
                        Icon(painterResource(Icons.ic_back), contentDescription = null)
                    }
                },
            )
        }
    ) { paddingValue ->

        Box(
            Modifier.fillMaxWidth()
                .height(248.dp)
        ) {
            AsyncImage(
                title?.image,
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.FillWidth,
                alignment = Alignment.TopCenter
            )

            Box(
                Modifier.matchParentSize()
                    .imageGradientBackground()
            )
        }


        if (title == null) {
            //TODO loading screen
        } else {
            TitleDetailsUiContent(
                paddingValue = paddingValue,
                scrollBehavior = scrollBehavior,
                title = title,
                track = track,
                genres = genres,
                isFavorite = isFavorite,
                isShowCharacters = isShowCharacters,
                isFramesExists = isFramesExists,
                isShowTrailers = isShowTrailers,
                isTranslatorsExists = isTranslatorsExists,
                studios = studios,
                persons = persons,
                toggleFavorite = toggleFavorite,
                expandDescription = expandDescription,
                share = share,
                openInBrowser = openInBrowser,
                openCharactersList = openCharactersList,
                openFrames = openFrames,
                openTrailers = openTrailers,
                openTranslators = openTranslators,
                openChronology = openChronology,
                openEditTrack = openEditTrack,
                openGenreSearch = openGenreSearch,
                openStudioSearch = openStudioSearch,
                openPerson = openPerson,
                openTitle = openTitle
            )
        }

    }
}


@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)
@Composable
private fun TitleDetailsUiContent(
    paddingValue: PaddingValues,
    scrollBehavior: TopAppBarScrollBehavior,

    title: ShimoriTitleEntity,
    track: Track?,
    isFavorite: Boolean,
    isShowCharacters: Boolean,
    genres: List<Genre>,
    isFramesExists: Boolean,
    isShowTrailers: Boolean,
    isTranslatorsExists: Boolean,

    studios: List<Studio>,
    persons: LazyPagingItems<Person>,

    toggleFavorite: () -> Unit,
    expandDescription: () -> Unit,
    share: () -> Unit,
    openInBrowser: () -> Unit,
    openCharactersList: () -> Unit,
    openFrames: () -> Unit,
    openTrailers: () -> Unit,
    openTranslators: () -> Unit,
    openChronology: () -> Unit,

    openEditTrack: (Long, TrackTargetType) -> Unit,
    openGenreSearch: (Long) -> Unit,
    openStudioSearch: (String) -> Unit,
    openPerson: (Long) -> Unit,
    openTitle: (Long, TrackTargetType) -> Unit,
) {
    val textCreator = LocalShimoriTextCreator.current
    val icons = LocalShimoriIconsUtil.current
    val isCompact = LocalWindowSizeClass.current.widthSizeClass.isCompact()

    val state = rememberLazyGridState()

    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
        LazyVerticalGrid(
            state = state,
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .mouseWheelNestedScrollConnectionFix(state, scrollBehavior)
        ) {

            itemSpacer(paddingValue.calculateTopPadding(), "title_spacer")
            itemSpacer(12.dp)

            item(
                key = "header",
                span = { GridItemSpan(this.maxLineSpan) }
            ) {
                TitleHeader(title)
            }

            itemSpacer(16.dp)

            item(
                key = "actions",
                span = { GridItemSpan(this.maxLineSpan) }
            ) {
                TitleActions(
                    title,
                    track,
                    isFavorite,
                    openEditTrack,
                    toggleFavorite,
                    share,
                    openInBrowser
                )
            }

            itemSpacer(24.dp)

            item(
                key = "characters",
                span = { GridItemSpan(this.maxLineSpan) }
            ) {
                AnimatedVisibility(
                    isShowCharacters,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column {
                        TitleCharacters(title.id, title.type, openCharactersList)
                        //moved from LazyVerticalGrid for smooth hiding
                        Spacer(Modifier.height(24.dp))
                    }
                }
            }

            item(
                key = "about",
                span = {
                    GridItemSpan(
                        if (isCompact) this.maxLineSpan
                        else 1
                    )
                }
            ) {
                Column {
                    TitleAbout(
                        genres = genres,
                        description = title.description,
                        onGenreClicked = openGenreSearch
                    )
                    Spacer(Modifier.height(24.dp))
                }
            }


            if (
                title.type.anime && (isFramesExists || isShowTrailers)
            ) {
                item(
                    key = "frames_and_trailers",
                    span = {
                        GridItemSpan(
                            if (isCompact) this.maxLineSpan
                            else 1
                        )
                    }
                ) {
                    TitleFramesAndTrailers(
                        titleId = title.id,
                        titleType = title.type,
                        isFramesExists = isFramesExists,
                        isShowTrailers = isShowTrailers,
                        onFramesClicked = openFrames,
                        openTrailerList = openTrailers
                    )
                }
            }

            if (title.type.anime && (isFramesExists || isShowTrailers)) {
                itemSpacer(24.dp)
            }

            if (
                isTranslatorsExists || studios.isNotEmpty() || persons.itemCount > 0
            ) {
                item(
                    key = "staff",
                    span = {
                        GridItemSpan(
                            if (isCompact) this.maxLineSpan
                            else if (isFramesExists || isShowTrailers) this.maxLineSpan
                            else 1
                        )
                    }
                ) {
                    TitleStaff(
                        isTranslatorsExists = isTranslatorsExists,
                        studios = studios,
                        persons = persons,
                        openTranslators = openTranslators,
                        openStudioSearch = openStudioSearch,
                        openPerson = openPerson,
                    )
                }
            }


            itemSpacer(paddingValue.calculateBottomWithAdditional())
        }
    }
}

