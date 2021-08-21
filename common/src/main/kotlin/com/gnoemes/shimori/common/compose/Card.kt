package com.gnoemes.shimori.common.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.gnoemes.shimori.common.R
import com.gnoemes.shimori.common.compose.theme.keyInfoStyle
import com.gnoemes.shimori.model.anime.AnimeWithRate
import com.gnoemes.shimori.model.manga.MangaWithRate

@Composable
fun AnimeListCard(
    anime: AnimeWithRate,
    onCoverLongClick : () -> Unit
) {
    Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
    ) {

        Cover(
                painter = rememberImagePainter(anime.entity.image),
                onLongClick = onCoverLongClick,
                modifier = Modifier
                    .height(120.dp)
                    .width(96.dp)
        )

        Spacer(Modifier.width(12.dp))

        Column {
            Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = LocalShimoriTextCreator.current.name(anime.entity),
                    style = MaterialTheme.typography.keyInfoStyle,
                    color = MaterialTheme.colors.onPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
            )

            AnimeDescription(
                    anime = anime.entity,
                    format = DescriptionFormat.List,
                    modifier = Modifier.fillMaxWidth()
            )

            Row {
                val score = anime.rate?.score
                if (score != null && score > 0) {
                    Icon(
                            painter = painterResource(R.drawable.ic_star),
                            tint = MaterialTheme.colors.secondary,
                            modifier = Modifier.align(Alignment.CenterVertically),
                            contentDescription = null
                    )

                    Text(
                            text = "$score",
                            style = MaterialTheme.typography.keyInfoStyle,
                            color = MaterialTheme.colors.secondary,
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                }

                val size = anime.entity.episodes.let { if (it == 0) "?" else "$it" }
                val progress = anime.rate?.episodes ?: 0

                Text(
                        text = "$progress / $size",
                        style = MaterialTheme.typography.keyInfoStyle,
                        color = MaterialTheme.colors.secondary
                )
            }
        }
    }
}

@Composable
fun MangaListCard(
    manga: MangaWithRate,
    onCoverLongClick : () -> Unit
) {
    Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
    ) {

        Cover(
                painter = rememberImagePainter(manga.entity.image),
                onLongClick = onCoverLongClick,
                modifier = Modifier
                    .height(120.dp)
                    .width(96.dp)
        )

        Spacer(Modifier.width(12.dp))

        Column {
            Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = LocalShimoriTextCreator.current.name(manga.entity),
                    style = MaterialTheme.typography.keyInfoStyle,
                    color = MaterialTheme.colors.onPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
            )

            MangaDescription(
                    manga = manga.entity,
                    format = DescriptionFormat.List,
                    modifier = Modifier.fillMaxWidth()
            )

            Row {
                val score = manga.rate?.score
                if (score != null && score > 0) {
                    Icon(
                            painter = painterResource(R.drawable.ic_star),
                            tint = MaterialTheme.colors.secondary,
                            modifier = Modifier.align(Alignment.CenterVertically),
                            contentDescription = null
                    )

                    Text(
                            text = "$score",
                            style = MaterialTheme.typography.keyInfoStyle,
                            color = MaterialTheme.colors.secondary,
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                }

                val size = manga.entity.chapters.let { if (it == 0) "?" else "$it" }
                val progress = manga.rate?.episodes ?: 0

                Text(
                        text = "$progress / $size",
                        style = MaterialTheme.typography.keyInfoStyle,
                        color = MaterialTheme.colors.secondary
                )
            }
        }
    }
}
