package com.gnoemes.shimori.title.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.common.ui.LocalShimoriTextCreator
import com.gnoemes.shimori.common.ui.api.OptionalContent
import com.gnoemes.shimori.common.ui.components.CharacterCard
import com.gnoemes.shimori.common.ui.components.CharacterCover
import com.gnoemes.shimori.common.ui.components.ShimoriChip
import com.gnoemes.shimori.common.ui.components.TrailerCard
import com.gnoemes.shimori.common.ui.components.TrailerCover
import com.gnoemes.shimori.common.ui.shimoriPlaceholder
import com.gnoemes.shimori.common.ui.theme.ShimoriCharacterCoverRoundedCornerShape
import com.gnoemes.shimori.common.ui.theme.dimens
import com.gnoemes.shimori.data.core.entities.ShimoriTitleEntity
import com.gnoemes.shimori.data.core.entities.characters.Character
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeVideo
import com.gnoemes.shimori.title.R


@Composable
internal fun Characters(
    characters: OptionalContent<List<Character>?>,
    openCharacterDetails: (id: Long) -> Unit,
    openCharacterList: () -> Unit
) {
    RowContentSection(
        title = stringResource(id = R.string.title_characters),
        isMoreVisible = (characters.content?.size ?: 0) > 6,
        onClickMore = { if (characters.loaded) openCharacterList() },
        sectionLoaded = characters.loaded,
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
internal fun About(
    title: ShimoriTitleEntity?,
    //TODO restore
//    onGenreClick: (Genre) -> Unit
) {
    RowContentSection(
        title = stringResource(id = R.string.title_about),
        isMoreVisible = false,
        onClickMore = { },
        nonRowContent = {
            if (title == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(136.dp)
                        .shimoriPlaceholder(
                            visible = true,
                        )
                )
            } else {
                val description = title.description
                if (!description.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = description,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        },
        contentHorizontalArrangement = 8.dp,
        sectionLoaded = title != null
    ) {
        val genres = title?.genres
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
internal fun Trailers(
    videos: OptionalContent<List<AnimeVideo>?>,
//    onClickMore: () -> Unit
//    onVideoClick :(AnimeVideo) -> Unit
) {
    RowContentSection(
        title = stringResource(id = R.string.title_trailers),
        isMoreVisible = true,
        sectionLoaded = videos.loaded,
        onClickMore = { /*TODO*/ }
    ) {
        if (!videos.loaded) {
            repeat(3) {
                TrailerCover(
                    image = null,
                    modifier = Modifier
                        .width(MaterialTheme.dimens.trailerPosterWidth)
                        .height(MaterialTheme.dimens.trailerPosterHeight)
                        .shimoriPlaceholder(true),
                )
            }
        } else {
            videos.content?.forEach { video ->
                TrailerCard(
                    image = video.imageUrl,
                    name = video.name.orEmpty(),
                    hosting = video.hosting,
                    onClick = { /*TODO*/ }
                )
            }
        }
    }
}