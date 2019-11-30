package com.gnoemes.common.textcreators

import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import androidx.core.graphics.ColorUtils
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import com.gnoemes.common.R
import com.gnoemes.common.extensions.color
import com.gnoemes.shimori.model.anime.Anime
import com.gnoemes.shimori.model.anime.AnimeType
import com.gnoemes.shimori.model.common.ContentStatus
import javax.inject.Inject

class AnimeTextCreator @Inject constructor(
    private val context: Context
//TODO romadzi naming setting
) {

    companion object {
        private const val divider = "  •  "
    }

    private val dividerColor by lazy { ColorUtils.setAlphaComponent(Color.BLACK, 97) }

    //TODO romadzi
    fun showTitle(anime: Anime): String = anime.nameRu ?: anime.name

    fun showDescription(anime: Anime): CharSequence = buildSpannedString {

        when (anime.status) {
            ContentStatus.ANONS -> color(context.color(R.color.status_anons)) { append(context.getString(R.string.status_anons)) }
            ContentStatus.ONGOING -> color(context.color(R.color.status_ongoing)) { append(context.getString(R.string.status_ongoing)) }
            else -> Unit
        }

        if (anime.status != ContentStatus.ONGOING) {
            //smart cast doesn't work through modules
            anime.dateReleased?.let { date ->
                appendDotIfNotEmpty(this)
                append(date.year.toString())
            } ?: anime.dateAired?.let { date ->
                appendDotIfNotEmpty(this)
                append(date.year.toString())
            }
        }

        val type = getLocalizedType(anime.type)

        if (type != null) {
            appendDotIfNotEmpty(this)
            append(type)
        }

        if (anime.type == AnimeType.MOVIE) {
            return@buildSpannedString
        } else if (anime.status == ContentStatus.ANONS) {
            if (anime.episodes != 0) {
                appendDotIfNotEmpty(this)
                append(anime.episodes.toString())
            }
        } else if (anime.status == ContentStatus.ONGOING) {
            appendDotIfNotEmpty(this)
            append("${anime.episodesAired}/${anime.episodes.invalidIfNullOrZero()} ${context.getString(R.string.episode_short)}")
        } else {
            appendDotIfNotEmpty(this)
            append(context.getString(R.string.episode_short_format, anime.episodes))
        }
    }

    private fun appendDotIfNotEmpty(builder: SpannableStringBuilder) {
        if (builder.isNotEmpty()) {
            builder.color(dividerColor) { append(divider) }
        }
    }

    private fun getLocalizedType(type: AnimeType?): String? {
        return when (type) {
            AnimeType.TV -> context.getString(R.string.type_tv_short_translatable)
            AnimeType.OVA -> context.getString(R.string.type_ova)
            AnimeType.ONA -> context.getString(R.string.type_ona)
            AnimeType.MUSIC -> context.getString(R.string.type_music_translatable)
            AnimeType.MOVIE -> context.getString(R.string.type_movie_translatable)
            AnimeType.SPECIAL -> context.getString(R.string.type_special_translatable)
            else -> null
        }
    }

    private fun Int?.invalidIfNullOrZero(): String =
        if (this == null || this == 0) "-" else this.toString()
}