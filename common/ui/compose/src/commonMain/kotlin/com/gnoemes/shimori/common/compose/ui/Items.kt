package com.gnoemes.shimori.common.compose.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.common.compose.LocalShimoriTextCreator
import com.gnoemes.shimori.common.compose.LocalWindowSizeClass
import com.gnoemes.shimori.common.compose.isCompact
import com.gnoemes.shimori.common.ui.resources.Icons
import com.gnoemes.shimori.common.ui.resources.icons.ic_add_one
import com.gnoemes.shimori.common.ui.resources.icons.ic_edit
import com.gnoemes.shimori.common.ui.resources.icons.ic_star
import com.gnoemes.shimori.common.ui.resources.strings.title_trailer_unknown
import com.gnoemes.shimori.common.ui.resources.util.Strings
import com.gnoemes.shimori.data.TitleWithTrackEntity
import com.gnoemes.shimori.data.characters.Character
import com.gnoemes.shimori.data.common.Related
import com.gnoemes.shimori.data.common.ShimoriImage
import com.gnoemes.shimori.data.titles.anime.Anime
import com.gnoemes.shimori.data.titles.anime.AnimeVideo
import com.gnoemes.shimori.data.titles.manga.Manga
import com.gnoemes.shimori.data.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.track.TrackStatus
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun TitleGridItem(
    name: String,
    image: ShimoriImage?,
    status: TrackStatus?,
    showEditButton: Boolean,
    showStatusButton: Boolean,
    modifier: Modifier = Modifier,
    subInfo: @Composable () -> Unit,
    onCoverClick: () -> Unit,
    onEditClick: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable(onClick = onCoverClick),
    ) {
        TrackCover(
            image,
            status = status,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3 / 4f)
                .animateContentSize(),
            showEditButton = showEditButton,
            showStatusButton = showStatusButton,
            onClick = onCoverClick,
            onButtonClick = onEditClick
        )

        Text(
            name,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        subInfo()
    }
}

@Composable
fun TrailerItem(
    video: AnimeVideo,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Column(
        modifier
    ) {
        TrailerCover(
            video.imageUrl,
            modifier = Modifier.height(156.dp)
                .width(280.dp),
            onClick = onClick
        )

        Text(
            video.name?.takeIf { it.isNotEmpty() } ?: stringResource(Strings.title_trailer_unknown),
            modifier = Modifier.width(280.dp),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Composable
fun CharacterItem(
    character: Character,
    coverHeight: Dp,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val textCreator = LocalShimoriTextCreator.current
    Column(
        modifier
    ) {
        CharacterCover(
            character.image,
            modifier = Modifier.height(coverHeight)
                .aspectRatio(3 / 4f),
            onClick = onClick
        )

        Text(
            textCreator {
                character.name()
            },
            modifier = Modifier.width(coverHeight / 4 * 3f),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ColumnScope.ListItem(
    icon: Painter,
    text: String,
    onClick: () -> Unit,
    description: String? = null,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(start = 4.dp, top = 8.dp, end = 16.dp, bottom = 8.dp),
) {
    Row(
        modifier = modifier
    ) {
        Icon(
            icon, contentDescription = null,
            modifier = Modifier
                .padding(12.dp)
                .size(24.dp)
        )

        Spacer(Modifier.width(4.dp))

        Column(
            modifier = Modifier.align(Alignment.CenterVertically),
        ) {
            Text(
                text,
                style = MaterialTheme.typography.bodyLarge
            )

            if (description != null) {
                Text(
                    description,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun TrackItem(
    //aspect ratio doesn't work properly for parent height
    //so we are going to calculate width/height for cover
    parentWidth: Dp,
    titleWithTrack: TitleWithTrackEntity,
    modifier: Modifier,
    openEdit: () -> Unit,
    openDetails: () -> Unit,
    addOneToProgress: (() -> Unit)? = null,
    sizeClass: WindowSizeClass = LocalWindowSizeClass.current,
) {
    val textCreator = LocalShimoriTextCreator.current
    val track = titleWithTrack.track
    val isListItem by remember(sizeClass) {
        derivedStateOf {
            sizeClass.isCompact()
        }
    }

    Surface(
        modifier = modifier,
    ) {
        if (isListItem) {
            Row {
                val width by remember(parentWidth) {
                    derivedStateOf { parentWidth * 0.25f }
                }

                val height by remember(width) {
                    derivedStateOf { (width / 3) * 4 }
                }

                Spacer(Modifier.width(16.dp))
                TrackCover(
                    titleWithTrack.entity.image,
                    status = null,
                    modifier = Modifier
                        .width(width)
                        .height(height)
                        .animateContentSize(),
                    showEditButton = false,
                    showStatusButton = false,
                    onClick = openDetails
                )
                Spacer(Modifier.width(16.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(height)
                ) {
                    SelectionContainer {
                        Text(
                            textCreator {
                                titleWithTrack.entity.name()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                    Text(
                        buildAnnotatedString {
                            textCreator.build {
                                when (val title = titleWithTrack.entity) {
                                    is Anime -> {
                                        val status = title.status()
                                        if (status != null) {
                                            append(status)
                                            append(" $divider ")
                                        }
                                        append(title.type())
                                    }

                                    is Manga -> {
                                        val status = title.status()
                                        if (status != null) {
                                            append(status)
                                            append(" $divider ")
                                        }
                                        append(title.type())
                                    }

                                    is Ranobe -> {
                                        val status = title.status()
                                        if (status != null) {
                                            append(status)
                                            append(" $divider ")
                                        }
                                        append(title.type())
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(Modifier.weight(1f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        CompositionLocalProvider(
                            LocalTextStyle provides MaterialTheme.typography.labelLarge.copy(
                                lineHeightStyle = LineHeightStyle(
                                    LineHeightStyle.Alignment.Proportional,
                                    trim = LineHeightStyle.Trim.Both
                                ),
                            ),
                            LocalContentColor provides MaterialTheme.colorScheme.secondary
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                if (track?.score != null && track.score != 0) {
                                    Icon(
                                        painterResource(Icons.ic_star),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(bottom = 1.dp)
                                            .size(16.dp),
                                    )

                                    Text(track.score.toString())
                                    Spacer(Modifier.width(8.dp))
                                }

                                Text(
                                    textCreator {
                                        titleWithTrack.progress()
                                    },
                                )
                            }
                        }

                        Row {
                            FilledTonalIconButton(
                                onClick = openEdit,
                            ) {
                                Icon(
                                    painterResource(Icons.ic_edit),
                                    contentDescription = null,
                                )
                            }

                            if (addOneToProgress != null) {
                                Spacer(Modifier.width(16.dp))
                                FilledTonalIconButton(
                                    onClick = addOneToProgress,
                                ) {
                                    Icon(
                                        painterResource(Icons.ic_add_one),
                                        contentDescription = null,
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.width(16.dp))
            }
        } else {
            val title = titleWithTrack.entity

            TitleGridItem(
                textCreator { title.name() },
                title.image,
                titleWithTrack.track?.status,
                showEditButton = true,
                showStatusButton = false,
                modifier = modifier,
                subInfo = {
                    TitleSubInfo(title, modifier.fillMaxWidth())
                },
                onCoverClick = openDetails,
                onEditClick = openEdit
            )

        }
    }
}

@Composable
fun RelatedItem(
    related: Related,
    onClick: () -> Unit,
    openEdit: () -> Unit
) {
    val textCreator = LocalShimoriTextCreator.current

    TitleGridItem(
        name = textCreator {
            related.relation()
        },
        image = related.title.entity.image,
        status = related.title.track?.status,
        showEditButton = false,
        showStatusButton = true,
        modifier = Modifier
            .width(132.dp),
        subInfo = {
            TitleSubInfo(related.title.entity, Modifier.fillMaxWidth())
        },
        onCoverClick = onClick,
        onEditClick = openEdit
    )
}
