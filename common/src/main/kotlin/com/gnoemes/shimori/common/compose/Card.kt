package com.gnoemes.shimori.common.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.gnoemes.shimori.common.R
import com.gnoemes.shimori.common.compose.theme.keyInfoStyle
import com.gnoemes.shimori.model.anime.AnimeWithRate

@Composable
fun animeListCard(
    anime: AnimeWithRate
) {
    Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
    ) {

        Cover(
                painter = rememberImagePainter(anime.entity.image),
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

            animeDescription(
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