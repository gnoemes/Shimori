package com.gnoemes.shimori.common.compose.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.common.compose.LocalShimoriTextCreator
import com.gnoemes.shimori.common.compose.LocalWindowSizeClass
import com.gnoemes.shimori.common.compose.isCompact
import com.gnoemes.shimori.common.ui.resources.Icons
import com.gnoemes.shimori.common.ui.resources.icons.ic_add_one
import com.gnoemes.shimori.common.ui.resources.icons.ic_edit
import com.gnoemes.shimori.common.ui.resources.icons.ic_star
import com.gnoemes.shimori.data.TitleWithTrackEntity
import com.gnoemes.shimori.data.titles.anime.Anime
import com.gnoemes.shimori.data.titles.manga.Manga
import com.gnoemes.shimori.data.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.track.Track
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalFoundationApi::class)
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
    titleWithTrack: TitleWithTrackEntity,
    modifier: Modifier,
    openEdit: () -> Unit,
    openDetails: () -> Unit,
    addOneToProgress: ((Track) -> Unit)? = null,
    widthSizeClass: WindowWidthSizeClass = LocalWindowSizeClass.current.widthSizeClass,
) {
    val textCreator = LocalShimoriTextCreator.current
    val track = titleWithTrack.track

    //aspect ratio doesn't work properly for parent height
    //so we are going to calculate width/height for cover
    var size by remember { mutableStateOf(IntSize.Zero) }

    Surface(
        modifier = modifier
            .composed {
                onGloballyPositioned {
                    size = it.size
                }
            }
    ) {
        if (widthSizeClass.isCompact()) {
            Row {
                val density = LocalDensity.current

                val width by remember(size.width) {
                    mutableStateOf(with(density) { (size.width * 0.25f).toDp() })
                }

                val height by remember(width) {
                    mutableStateOf(width / 3 * 4)
                }

                Spacer(Modifier.width(16.dp))
                TrackCover(
                    titleWithTrack.entity.image,
                    modifier = Modifier
                        .width(width)
                        .height(height)
                        .animateContentSize(),
                    showEditButton = false,
                    windowWidthSizeClass = widthSizeClass,
                    onClick = openDetails
                )
                Spacer(Modifier.width(16.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
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
                                        append(status)
                                        if (status != null) append(" $divider ")
                                        append(title.type())
                                    }

                                    is Manga -> {
                                        val status = title.status()
                                        append(status)
                                        if (status != null) append(" $divider ")
                                        append(title.type())
                                    }

                                    is Ranobe -> {
                                        val status = title.status()
                                        append(status)
                                        if (status != null) append(" $divider ")
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

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (track?.score != null && track.score != 0) {
                                Icon(
                                    painterResource(Icons.ic_star),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.secondary
                                )

                                Text(
                                    track.score.toString(),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.secondary,
                                )

                                Spacer(Modifier.width(8.dp))
                            }

                            Text(
                                textCreator {
                                    titleWithTrack.progress()
                                },
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }


                        Row {
                            FilledIconButton(
                                onClick = openEdit,
                                modifier = Modifier.size(32.dp),
                            ) {
                                Icon(
                                    painterResource(Icons.ic_edit),
                                    contentDescription = null
                                )
                            }

                            if (addOneToProgress != null) {
                                Spacer(Modifier.width(16.dp))
                                FilledIconButton(
                                    onClick = { addOneToProgress(track!!) },
                                    modifier = Modifier.size(32.dp),
                                ) {
                                    Icon(
                                        painterResource(Icons.ic_add_one),
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.width(16.dp))
            }
        }
    }

}