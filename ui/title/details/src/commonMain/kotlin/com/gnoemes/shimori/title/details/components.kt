package com.gnoemes.shimori.title.details

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.itemKey
import com.gnoemes.shimori.common.compose.LocalShimoriTextCreator
import com.gnoemes.shimori.common.compose.itemSpacer
import com.gnoemes.shimori.common.compose.noRippleClickable
import com.gnoemes.shimori.common.compose.rememberLazyListState
import com.gnoemes.shimori.common.compose.theme.favorite
import com.gnoemes.shimori.common.compose.theme.favoriteContainer
import com.gnoemes.shimori.common.compose.ui.CharacterItem
import com.gnoemes.shimori.common.compose.ui.StatusButton
import com.gnoemes.shimori.common.ui.resources.Icons
import com.gnoemes.shimori.common.ui.resources.icons.ic_arrow_right
import com.gnoemes.shimori.common.ui.resources.icons.ic_heart
import com.gnoemes.shimori.common.ui.resources.icons.ic_more
import com.gnoemes.shimori.common.ui.resources.icons.ic_open_in_browser
import com.gnoemes.shimori.common.ui.resources.icons.ic_share
import com.gnoemes.shimori.common.ui.resources.icons.ic_star
import com.gnoemes.shimori.common.ui.resources.strings.filter_age_rating
import com.gnoemes.shimori.common.ui.resources.strings.title_chapters
import com.gnoemes.shimori.common.ui.resources.strings.title_characters
import com.gnoemes.shimori.common.ui.resources.strings.title_ep
import com.gnoemes.shimori.common.ui.resources.strings.title_ep_duration
import com.gnoemes.shimori.common.ui.resources.strings.title_menu_share
import com.gnoemes.shimori.common.ui.resources.strings.title_menu_web
import com.gnoemes.shimori.common.ui.resources.strings.title_release
import com.gnoemes.shimori.common.ui.resources.strings.title_score
import com.gnoemes.shimori.common.ui.resources.strings.title_type
import com.gnoemes.shimori.common.ui.resources.util.Strings
import com.gnoemes.shimori.data.ShimoriTitleEntity
import com.gnoemes.shimori.data.characters.Character
import com.gnoemes.shimori.data.titles.anime.Anime
import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.track.TrackTargetType
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TitleHeader(
    title: ShimoriTitleEntity
) {
    val textCreator = LocalShimoriTextCreator.current

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            Modifier.fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            //trigger arrangement
            Spacer(Modifier.width(0.dp))

            val score = textCreator.nullable { title.score() }
            if (score != null) {
                TitleHeaderDescriptionItem(
                    stringResource(Strings.title_score)
                ) {
                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.secondary) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                painterResource(Icons.ic_star),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(bottom = 1.dp)
                                    .size(12.dp),
                            )

                            Text(score)
                        }
                    }
                }
            }

            val status = textCreator.nullable { title.status(releaseFull = true) }
            if (status != null) {
                TitleHeaderDescriptionItem(
                    stringResource(Strings.title_release)
                ) {
                    Text(status, color = MaterialTheme.colorScheme.tertiary)
                }
            }

            val size = textCreator.nullable { title.size() }
            if (size != null) {
                TitleHeaderDescriptionItem(
                    stringResource(
                        when (title) {
                            is Anime -> Strings.title_ep
                            else -> Strings.title_chapters
                        }
                    )
                ) {
                    Text(size, color = MaterialTheme.colorScheme.tertiary)
                }
            }

            val type = textCreator.nullable { title.type() }
            if (type != null) {
                TitleHeaderDescriptionItem(
                    stringResource(Strings.title_type)
                ) {
                    Text(type, color = MaterialTheme.colorScheme.onSurface)
                }
            }

            val duration = if (title is Anime) textCreator.nullable { title.duration() } else null
            if (duration != null) {
                TitleHeaderDescriptionItem(
                    stringResource(Strings.title_ep_duration)
                ) {
                    Text(duration, color = MaterialTheme.colorScheme.onSurface)
                }
            }

            val ageRating = textCreator.nullable { title.ageRating() }
            if (ageRating != null) {
                TitleHeaderDescriptionItem(
                    stringResource(Strings.filter_age_rating)
                ) {
                    Text(ageRating, color = MaterialTheme.colorScheme.onSurface)
                }
            }

            Spacer(Modifier.width(0.dp))
        }
    }
}

@Composable
private fun TitleHeaderDescriptionItem(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.titleSmall) {
            content()
        }
        Text(
            title,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
internal fun TitleActions(
    title: ShimoriTitleEntity,
    track: Track?,
    isFavorite: Boolean,
    openEditTrack: (Long, TrackTargetType) -> Unit,
    toggleFavorite: () -> Unit,
    share: () -> Unit,
    openInBrowser: () -> Unit,
) {
    var showMenu by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(Modifier.width(0.dp))
        StatusButton(
            title,
            track,
            openEditTrack,
            modifier = Modifier
                .weight(1f, false)
                .widthIn(max = 332.dp)
                .fillMaxWidth()
                .height(48.dp)
        )

        FilledTonalIconButton(
            toggleFavorite,
            Modifier.size(48.dp),
            colors = IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = if (isFavorite) favoriteContainer else MaterialTheme.colorScheme.surfaceContainer,
                contentColor = if (isFavorite) favorite else MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            Icon(
                painter = painterResource(Icons.ic_heart),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        }

        Box {
            FilledTonalIconButton(
                { showMenu = !showMenu },
                Modifier.size(48.dp),
            ) {
                Icon(
                    painter = painterResource(Icons.ic_more),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
            }

            DropdownMenu(
                showMenu,
                onDismissRequest = {
                    showMenu = false
                },
                offset = DpOffset(0.dp, 16.dp)
            ) {
                DropdownMenuItem(
                    text = {
                        Text(stringResource(Strings.title_menu_share))
                    },
                    leadingIcon = {
                        Icon(
                            painterResource(Icons.ic_share),
                            contentDescription = null
                        )
                    },
                    onClick = {
                        showMenu = false
                        share()
                    },
                )

                DropdownMenuItem(
                    text = {
                        Text(stringResource(Strings.title_menu_web))
                    },
                    leadingIcon = {
                        Icon(
                            painterResource(Icons.ic_open_in_browser),
                            contentDescription = null
                        )
                    },
                    onClick = {
                        showMenu = false
                        openInBrowser()
                    },
                )
            }

        }

        Spacer(Modifier.width(0.dp))
    }
}

@Composable
internal fun TitleCharacters(
    characters: LazyPagingItems<Character>,
    openCharactersList: () -> Unit,
    openCharacter: (Long) -> Unit,
) {
    TitleListCategory(
        stringResource(Strings.title_characters),
        openCharactersList,
    ) {
        LazyRow(
            Modifier.fillMaxWidth(),
            state = characters.rememberLazyListState(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemSpacer(0.dp)
            items(
                count = characters.itemCount,
                key = characters.itemKey { "character_${it.id}" },
            ) { index ->
                val entity = characters[index]
                if (entity != null) {
                    val openCharacterClick = remember(entity.id) {
                        { openCharacter(entity.id) }
                    }
                    CharacterItem(entity, onClick = openCharacterClick)

                }


            }
            itemSpacer(0.dp)
        }
    }
}

@Composable
internal fun TitleListCategory(
    title: String,
    openList: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        Modifier.fillMaxWidth()
    ) {
        Row(
            Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
                .noRippleClickable(openList),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onBackground) {
                Text(
                    title,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleMedium
                )

                Icon(
                    painter = painterResource(Icons.ic_arrow_right),
                    contentDescription = null
                )
            }
        }

        content()
    }
}