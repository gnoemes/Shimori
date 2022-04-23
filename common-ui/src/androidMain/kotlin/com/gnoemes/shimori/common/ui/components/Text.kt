//package com.gnoemes.shimori.common_ui.compose.components
//
//import androidx.compose.foundation.text.InlineTextContent
//import androidx.compose.foundation.text.appendInlineContent
//import androidx.compose.material3.Icon
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.*
//import androidx.compose.ui.text.style.TextOverflow
//import com.gnoemes.shimori.common.compose.LocalShimoriTextCreator
//import com.gnoemes.shimori.common.compose.theme.titleAnnounced
//import com.gnoemes.shimori.common.compose.theme.titleOngoing
//import com.gnoemes.shimori.model.anime.Anime
//import com.gnoemes.shimori.model.common.ContentStatus
//import com.gnoemes.shimori.model.manga.Manga
//import com.gnoemes.shimori.model.ranobe.Ranobe
//
//const val Divider = " • "
//
//@Composable
//fun AnimeDescription(
//    anime: Anime,
//    format: DescriptionFormat,
//    modifier: Modifier = Modifier
//) {
//    val text = when (format) {
//        DescriptionFormat.List -> buildListText(anime = anime)
//        else -> buildAnnotatedString { }
//    }
//
//    Description(text = text, modifier = modifier)
//}
//
//@Composable
//fun MangaDescription(
//    manga: Manga,
//    format: DescriptionFormat,
//    modifier: Modifier = Modifier,
//) {
//    val text = when (format) {
//        DescriptionFormat.List -> buildListText(manga = manga)
//        else -> buildAnnotatedString { }
//    }
//
//    Description(text = text, modifier = modifier)
//}
//
//@Composable
//fun RanobeDescription(
//    ranobe: Ranobe,
//    format: DescriptionFormat,
//    modifier: Modifier = Modifier,
//) {
//    val text = when (format) {
//        DescriptionFormat.List -> buildListText(ranobe = ranobe)
//        else -> buildAnnotatedString { }
//    }
//
//    Description(text = text, modifier = modifier)
//}
//
//@Composable
//private fun Description(
//    text: AnnotatedString,
//    modifier: Modifier
//) {
//    Text(
//        text = text,
//        modifier = modifier,
//        style = MaterialTheme.typography.labelMedium,
//        color = MaterialTheme.colorScheme.secondary,
//        overflow = TextOverflow.Ellipsis,
//        inlineContent = mapOf(
//            "score_image" to InlineTextContent(
//                Placeholder(
//                    MaterialTheme.typography.labelMedium.fontSize,
//                    MaterialTheme.typography.labelMedium.fontSize,
//                    PlaceholderVerticalAlign.Center
//                )
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_star),
//                    tint = MaterialTheme.colorScheme.secondary,
//                    contentDescription = ""
//                )
//            }
//        )
//    )
//}
//
//@Composable
//private fun buildListText(anime: Anime): AnnotatedString {
//    val statusInfo = LocalShimoriTextCreator.current.statusDescription(anime)
//    val typeInfo = LocalShimoriTextCreator.current.typeDescription(anime)
//    val scoreInfo = LocalShimoriTextCreator.current.scoreDescription(anime.score)
//
//    return buildListText(
//        anime.status,
//        statusInfo,
//        scoreInfo,
//        typeInfo
//    )
//}
//
//@Composable
//private fun buildListText(manga: Manga) = buildAnnotatedString {
//    val statusInfo = LocalShimoriTextCreator.current.statusDescription(manga)
//    val typeInfo = LocalShimoriTextCreator.current.typeDescription(manga)
//    val scoreInfo = LocalShimoriTextCreator.current.scoreDescription(manga.score)
//
//    return buildListText(
//        manga.status,
//        statusInfo,
//        scoreInfo,
//        typeInfo
//    )
//}
//
//@Composable
//private fun buildListText(ranobe: Ranobe) = buildAnnotatedString {
//    val statusInfo = LocalShimoriTextCreator.current.statusDescription(ranobe)
//    val typeInfo = LocalShimoriTextCreator.current.typeDescription(ranobe)
//    val scoreInfo = LocalShimoriTextCreator.current.scoreDescription(ranobe.score)
//
//    return buildListText(
//        ranobe.status,
//        statusInfo,
//        scoreInfo,
//        typeInfo
//    )
//}
//
//@Composable
//private fun buildListText(
//    status: ContentStatus?,
//    statusInfo: String?,
//    scoreInfo: String?,
//    typeInfo: String?,
//) = buildAnnotatedString {
//    val color = when (status) {
//        ContentStatus.ANONS -> titleAnnounced
//        ContentStatus.ONGOING -> titleOngoing
//        else -> null
//    }
//
//    val statusPart = {
//        if (!statusInfo.isNullOrEmpty()) {
//            if (length > 0) append(Divider)
//            val append: AnnotatedString.Builder.() -> Unit = { append(statusInfo) }
//            color?.let { withStyle(style = SpanStyle(color = color), append) } ?: append()
//        }
//    }
//
//    val scorePart = {
//        if (!scoreInfo.isNullOrEmpty()) {
//            if (length > 0) append(Divider)
//            appendInlineContent(id = "score_image")
//            append(" $scoreInfo")
//        }
//    }
//
//    val typePart = {
//        if (!typeInfo.isNullOrEmpty()) {
//            if (length > 0) append(Divider)
//
//            append(typeInfo)
//        }
//    }
//
//    when (status) {
//        ContentStatus.ANONS, ContentStatus.ONGOING -> {
//            statusPart()
//            scorePart()
//            typePart()
//        }
//        else -> {
//            scorePart()
//            statusPart()
//            typePart()
//        }
//    }
//}
//
//@JvmInline
//value class DescriptionFormat private constructor(val value: Int) {
//
//
//    companion object {
//        /**
//         * Episode information based on status + type (TV, MOVIE...)
//         */
//        val List = DescriptionFormat(0)
//
//        /**
//         * Rating + episode information based on status
//         */
//        val Title = DescriptionFormat(1)
//    }
//}