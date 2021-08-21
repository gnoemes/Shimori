package com.gnoemes.shimori.common.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.gnoemes.shimori.common.compose.theme.caption
import com.gnoemes.shimori.common.compose.theme.titleAnnounced
import com.gnoemes.shimori.common.compose.theme.titleOngoing
import com.gnoemes.shimori.model.anime.Anime
import com.gnoemes.shimori.model.common.ContentStatus
import com.gnoemes.shimori.model.manga.Manga

const val Divider = " • "

@Composable
fun animeDescription(
    anime: Anime,
    format: DescriptionFormat,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.caption,
    color: Color = MaterialTheme.colors.caption
) {

    val text = when (format) {
        DescriptionFormat.List -> buildListText(anime = anime)
        else -> buildAnnotatedString { }
    }

    Text(
            text = text,
            modifier = modifier,
            style = style,
            color = color
    )
}

@Composable
fun mangaDescription(
    manga: Manga,
    format: DescriptionFormat,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.caption,
    color: Color = MaterialTheme.colors.caption
) {

    val text = when (format) {
        DescriptionFormat.List -> buildListText(manga = manga)
        else -> buildAnnotatedString { }
    }

    Text(
            text = text,
            modifier = modifier,
            style = style,
            color = color
    )
}

@Composable
private fun buildListText(anime: Anime) = buildAnnotatedString {
    val episodeInfo = LocalShimoriTextCreator.current.statusDescription(anime)
    val typeInfo = LocalShimoriTextCreator.current.typeDescription(anime)

    val color = when (anime.status) {
        ContentStatus.ANONS -> titleAnnounced
        else -> titleOngoing
    }

    if (!episodeInfo.isNullOrEmpty()) {
        withStyle(style = SpanStyle(color = color)) {
            append(episodeInfo)
        }
    }

    if (!typeInfo.isNullOrEmpty()) {
        if (length > 0) append(Divider)

        append(typeInfo)
    }
}

@Composable
private fun buildListText(manga: Manga) = buildAnnotatedString {
    val statusInfo = LocalShimoriTextCreator.current.statusDescription(manga)
    val typeInfo = LocalShimoriTextCreator.current.typeDescription(manga)

    val color = when (manga.status) {
        ContentStatus.ANONS -> titleAnnounced
        else -> titleOngoing
    }

    if (!statusInfo.isNullOrEmpty()) {
        withStyle(style = SpanStyle(color = color)) {
            append(statusInfo)
        }
    }

    if (!typeInfo.isNullOrEmpty()) {
        if (length > 0) append(Divider)

        append(typeInfo)
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