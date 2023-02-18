package com.gnoemes.shimori.common.ui.components

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextOverflow
import com.gnoemes.shimori.common.ui.LocalShimoriTextCreator
import com.gnoemes.shimori.common.ui.theme.titleAnnounced
import com.gnoemes.shimori.common.ui.theme.titleDiscontinued
import com.gnoemes.shimori.common.ui.theme.titleOngoing
import com.gnoemes.shimori.common.ui.theme.titlePaused
import com.gnoemes.shimori.data.core.entities.ShimoriTitleEntity
import com.gnoemes.shimori.data.core.entities.common.TitleStatus
import com.gnoemes.shimori.data.core.entities.titles.anime.Anime
import com.gnoemes.shimori.data.core.entities.titles.manga.Manga
import com.gnoemes.shimori.data.core.entities.titles.ranobe.Ranobe
import com.gnoemes.shimori.ui.R

const val Divider = " • "
private const val SCORE_IMAGE = "score_image"

@Composable
fun TitleDescription(
    title: ShimoriTitleEntity,
    format: DescriptionFormat,
    modifier: Modifier = Modifier
) {
    when (title) {
        is Anime -> AnimeDescription(anime = title, format = format, modifier = modifier)
        is Manga -> MangaDescription(manga = title, format = format, modifier = modifier)
        is Ranobe -> RanobeDescription(ranobe = title, format = format, modifier = modifier)
    }
}

@Composable
fun AnimeDescription(
    anime: Anime,
    format: DescriptionFormat,
    modifier: Modifier = Modifier
) {
    val text = when (format) {
        DescriptionFormat.List -> buildListText(anime = anime)
        DescriptionFormat.Title -> buildTitleText(anime = anime)
        else -> buildAnnotatedString { }
    }

    Description(text = text, modifier = modifier)
}

@Composable
fun MangaDescription(
    manga: Manga,
    format: DescriptionFormat,
    modifier: Modifier = Modifier,
) {
    val text = when (format) {
        DescriptionFormat.List -> buildListText(manga = manga)
        DescriptionFormat.Title -> buildTitleText(manga = manga)
        else -> buildAnnotatedString { }
    }

    Description(text = text, modifier = modifier)
}

@Composable
fun RanobeDescription(
    ranobe: Ranobe,
    format: DescriptionFormat,
    modifier: Modifier = Modifier,
) {
    val text = when (format) {
        DescriptionFormat.List -> buildListText(ranobe = ranobe)
        DescriptionFormat.Title -> buildTitleText(ranobe = ranobe)
        else -> buildAnnotatedString { }
    }

    Description(text = text, modifier = modifier)
}

@Composable
fun ReleaseDateDescription(
    title: ShimoriTitleEntity,
    modifier: Modifier = Modifier
) {
    val text = LocalShimoriTextCreator.current.releaseDate(title)

    if (text != null) {
        Text(
            modifier = modifier,
            text = buildAnnotatedString {

                if (title.status == TitleStatus.DISCONTINUED) {
                    withStyle(style = SpanStyle(color = titleDiscontinued)) { append(text = text) }
                } else {
                    append(text)
                }
            }
        )
    }
}

@Composable
fun TitlePropertyInfo(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = text,
        color = MaterialTheme.colorScheme.secondary
    )
}

@Composable
fun TitleTrailerInfo(
    name: String,
    hosting: String?,
    modifier: Modifier = Modifier
) {
    Text(
        text = buildAnnotatedString {
            if (hosting != null) {
                append(hosting)
                append(Divider)
            }
            append(name)
        },
        style = MaterialTheme.typography.labelMedium,
        modifier = modifier,
        maxLines = 1,
        color = MaterialTheme.colorScheme.secondary,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun Description(
    text: AnnotatedString,
    modifier: Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.secondary,
        overflow = TextOverflow.Ellipsis,
        inlineContent = mapOf(
            SCORE_IMAGE to InlineTextContent(
                Placeholder(
                    MaterialTheme.typography.labelMedium.fontSize,
                    MaterialTheme.typography.labelMedium.fontSize,
                    PlaceholderVerticalAlign.Center
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_star),
                    tint = MaterialTheme.colorScheme.secondary,
                    contentDescription = ""
                )
            }
        )
    )
}

@Composable
private fun buildTitleText(anime: Anime): AnnotatedString {
    val scoreInfo = LocalShimoriTextCreator.current.scoreDescription(anime.rating)

    if (anime.isOngoing) {
        val statusInfo = LocalShimoriTextCreator.current.statusDescription(anime)

        return buildText(
            anime.status,
            statusInfo,
            scoreInfo,
            typeInfo = null
        )
    }

    return buildText(
        anime.status,
        statusInfo = null,
        scoreInfo,
        typeInfo = null
    )
}

@Composable
private fun buildTitleText(manga: Manga): AnnotatedString {
    val scoreInfo = LocalShimoriTextCreator.current.scoreDescription(manga.rating)

    if (manga.isOngoing) {
        val statusInfo = LocalShimoriTextCreator.current.statusDescription(manga)

        return buildText(
            manga.status,
            statusInfo,
            scoreInfo,
            typeInfo = null
        )
    }

    return buildText(
        manga.status,
        statusInfo = null,
        scoreInfo,
        typeInfo = null
    )
}

@Composable
private fun buildTitleText(ranobe: Ranobe): AnnotatedString {
    val scoreInfo = LocalShimoriTextCreator.current.scoreDescription(ranobe.rating)

    if (ranobe.isOngoing) {
        val statusInfo = LocalShimoriTextCreator.current.statusDescription(ranobe)

        return buildText(
            ranobe.status,
            statusInfo,
            scoreInfo,
            typeInfo = null
        )
    }

    return buildText(
        ranobe.status,
        statusInfo = null,
        scoreInfo,
        typeInfo = null
    )
}

@Composable
private fun buildListText(anime: Anime): AnnotatedString {
    val statusInfo = LocalShimoriTextCreator.current.statusDescription(anime)
    val typeInfo = LocalShimoriTextCreator.current.typeDescription(anime)
    val scoreInfo = LocalShimoriTextCreator.current.scoreDescription(anime.rating)

    return buildText(
        anime.status,
        statusInfo,
        scoreInfo,
        typeInfo
    )
}

@Composable
private fun buildListText(manga: Manga) = buildAnnotatedString {
    val statusInfo = LocalShimoriTextCreator.current.statusDescription(manga)
    val typeInfo = LocalShimoriTextCreator.current.typeDescription(manga)
    val scoreInfo = LocalShimoriTextCreator.current.scoreDescription(manga.rating)

    return buildText(
        manga.status,
        statusInfo,
        scoreInfo,
        typeInfo
    )
}

@Composable
private fun buildListText(ranobe: Ranobe) = buildAnnotatedString {
    val statusInfo = LocalShimoriTextCreator.current.statusDescription(ranobe)
    val typeInfo = LocalShimoriTextCreator.current.typeDescription(ranobe)
    val scoreInfo = LocalShimoriTextCreator.current.scoreDescription(ranobe.rating)

    return buildText(
        ranobe.status,
        statusInfo,
        scoreInfo,
        typeInfo
    )
}

@Composable
private fun buildText(
    status: TitleStatus?,
    statusInfo: String?,
    scoreInfo: String?,
    typeInfo: String?,
) = buildAnnotatedString {
    val color = when (status) {
        TitleStatus.ANONS -> titleAnnounced
        TitleStatus.ONGOING -> titleOngoing
        TitleStatus.PAUSED -> titlePaused
        TitleStatus.DISCONTINUED -> titleDiscontinued
        else -> null
    }

    val statusPart = {
        if (!statusInfo.isNullOrEmpty()) {
            if (length > 0) append(Divider)
            val append: AnnotatedString.Builder.() -> Unit = { append(statusInfo) }
            color?.let { withStyle(style = SpanStyle(color = color), append) } ?: append()
        }
    }

    val scorePart = {
        if (!scoreInfo.isNullOrEmpty()) {
            if (length > 0) append(Divider)
            appendInlineContent(id = SCORE_IMAGE)
            append(" $scoreInfo")
        }
    }

    val typePart = {
        if (!typeInfo.isNullOrEmpty()) {
            if (length > 0) append(Divider)

            append(typeInfo)
        }
    }

    when (status) {
        TitleStatus.ANONS, TitleStatus.ONGOING -> {
            statusPart()
            scorePart()
            typePart()
        }
        else -> {
            scorePart()
            statusPart()
            typePart()
        }
    }
}

@JvmInline
value class DescriptionFormat private constructor(val value: Int) {

    companion object {
        /**
         * Episode information based on status + type (TV, MOVIE...)
         */
        val List = DescriptionFormat(0)

        /**
         * Rating + episode information based on status
         */
        val Title = DescriptionFormat(1)
    }
}