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
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.gnoemes.shimori.common.R
import com.gnoemes.shimori.common.compose.LocalShimoriTextCreator
import com.gnoemes.shimori.common.compose.ShimoriCircleButton
import com.gnoemes.shimori.common.compose.theme.dimens
import com.gnoemes.shimori.model.anime.AnimeWithRate
import com.gnoemes.shimori.model.common.ShimoriImage
import com.gnoemes.shimori.model.manga.MangaWithRate
import com.gnoemes.shimori.model.ranobe.RanobeWithRate

@OptIn(ExperimentalCoilApi::class)
@Composable
fun AnimeListCard(
    anime: AnimeWithRate,
    onCoverLongClick: () -> Unit,
    onEditClick: () -> Unit,
    onIncrementClick: () -> Unit,
    onIncrementHold: () -> Unit,
    padding: PaddingValues = PaddingValues(horizontal = 16.dp)
) {
    Box(
        modifier = Modifier.padding(padding)
    ) {
        ListCard(
            image = anime.entity.image,
            name = LocalShimoriTextCreator.current.name(anime.entity),
            description = {
                AnimeDescription(
                    anime = anime.entity,
                    format = DescriptionFormat.List,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            score = anime.rate?.score,
            progress = stringResource(
                R.string.progress_format,
                anime.rate?.episodes ?: 0,
                anime.entity.episodesOrUnknown
            ),
            onCoverLongClick = onCoverLongClick,
            onEditClick = onEditClick,
            onIncrementClick = onIncrementClick,
            onIncrementHold = onIncrementHold
        )
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun MangaListCard(
    manga: MangaWithRate,
    onCoverLongClick: () -> Unit,
    onEditClick: () -> Unit,
    onIncrementClick: () -> Unit,
    onIncrementHold: () -> Unit,
    padding: PaddingValues = PaddingValues(horizontal = 16.dp)
) {
    Box(
        modifier = Modifier.padding(padding)
    ) {
        ListCard(
            image = manga.entity.image,
            name = LocalShimoriTextCreator.current.name(manga.entity),
            description = {
                MangaDescription(
                    manga = manga.entity,
                    format = DescriptionFormat.List,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            score = manga.rate?.score,
            progress = stringResource(
                R.string.progress_format,
                manga.rate?.chapters ?: 0,
                manga.entity.chaptersOrUnknown
            ),
            onCoverLongClick = onCoverLongClick,
            onEditClick = onEditClick,
            onIncrementClick = onIncrementClick,
            onIncrementHold = onIncrementHold
        )
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun RanobeListCard(
    ranobe: RanobeWithRate,
    onCoverLongClick: () -> Unit,
    onEditClick: () -> Unit,
    onIncrementClick: () -> Unit,
    onIncrementHold: () -> Unit,
    padding: PaddingValues = PaddingValues(horizontal = 16.dp)
) {
    Box(
        modifier = Modifier.padding(padding)
    ) {
        ListCard(
            image = ranobe.entity.image,
            name = LocalShimoriTextCreator.current.name(ranobe.entity),
            description = {
                RanobeDescription(
                    ranobe = ranobe.entity,
                    format = DescriptionFormat.List,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            score = ranobe.rate?.score,
            progress = stringResource(
                R.string.progress_format,
                ranobe.rate?.chapters ?: 0,
                ranobe.entity.chaptersOrUnknown
            ),
            onCoverLongClick = onCoverLongClick,
            onEditClick = onEditClick,
            onIncrementClick = onIncrementClick,
            onIncrementHold = onIncrementHold
        )
    }
}

@Composable
@OptIn(ExperimentalCoilApi::class)
fun ListCard(
    image: ShimoriImage?,
    name: String,
    description: @Composable () -> Unit,
    score: Int?,
    progress: String,
    onCoverLongClick: () -> Unit,
    onEditClick: () -> Unit,
    onIncrementClick: () -> Unit,
    onIncrementHold: () -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(MaterialTheme.dimens.listPosterHeight),
    ) {

        Cover(
            painter = rememberImagePainter(image),
            onLongClick = onCoverLongClick,
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
                            contentDescription = stringResource(id = R.string.edit)
                        )
                    }
                )

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