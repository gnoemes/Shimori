package com.gnoemes.common.textcreators

import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import androidx.core.graphics.ColorUtils
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import com.gnoemes.common.R
import com.gnoemes.common.extensions.color
import com.gnoemes.common.extensions.fontFamilyAndScale
import com.gnoemes.shimori.base.settings.ShimoriPreferences
import com.gnoemes.shimori.model.anime.Anime
import com.gnoemes.shimori.model.anime.AnimeType
import com.gnoemes.shimori.model.common.ContentStatus
import com.gnoemes.shimori.model.rate.Rate
import javax.inject.Inject

class AnimeTextCreator @Inject constructor(
    private val context: Context,
    private val settings: ShimoriPreferences
) {

    companion object {
        private const val divider = "  •  "
    }

    private val dividerColor by lazy {
        val lightTheme = context.resources.getBoolean(R.bool.is_light_theme)
        (if (lightTheme) Color.BLACK else Color.WHITE)
            .let { ColorUtils.setAlphaComponent(it, 97) }
    }

    fun title(anime: Anime): String =
        if (settings.isRussianNaming) anime.nameRu ?: anime.name
        else anime.name


    fun searchDescription(anime: Anime): CharSequence = buildSpannedString {
        appendStatus(anime.status)
        appendYear(anime)
        appendLocalizedType(anime.type)
        appendEpisodes(anime)
    }

    fun rateDescription(anime: Anime): CharSequence = buildSpannedString {
        appendStatus(anime.status)
        appendYear(anime)
        appendLocalizedType(anime.type)
        appendEpisodes(anime)
    }

    private fun SpannableStringBuilder.appendStatus(status: ContentStatus?) {
        when (status) {
            ContentStatus.ANONS -> color(context.color(R.color.status_anons)) { append(context.getString(R.string.status_anons)) }
            ContentStatus.ONGOING -> color(context.color(R.color.status_ongoing)) { append(context.getString(R.string.status_ongoing)) }
            else -> Unit
        }
    }

    private fun SpannableStringBuilder.appendYear(anime: Anime) {
        if (!anime.isOngoing) {
            //smart cast doesn't work through modules
            anime.dateReleased?.let { date ->
                appendDotIfNotEmpty()
                append(date.year.toString())
            } ?: anime.dateAired?.let { date ->
                appendDotIfNotEmpty()
                append(date.year.toString())
            }
        }
    }

    private fun SpannableStringBuilder.appendLocalizedType(type: AnimeType?) {
        val localizedType = getLocalizedType(type)

        if (localizedType != null) {
            appendDotIfNotEmpty()
            append(localizedType)
        }
    }

    private fun SpannableStringBuilder.appendEpisodes(anime: Anime) {
        if (anime.isMovie) {
            return
        } else if (anime.status == ContentStatus.ANONS) {
            if (anime.episodes != 0) {
                appendDotIfNotEmpty()
                append(anime.episodes.toString())
            }
        } else if (anime.isOngoing) {
            appendDotIfNotEmpty()
            append("${anime.episodesAired}/${anime.episodes.invalidIfNullOrZero()} ${context.getString(R.string.episode_short)}")
        } else {
            appendDotIfNotEmpty()
            append(context.getString(R.string.episode_short_format, anime.episodes))
        }
    }

    private fun SpannableStringBuilder.appendDotIfNotEmpty() {
        if (this.isNotEmpty()) {
            color(dividerColor) { append(divider) }
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

    fun rateProgress(rate: Rate, anime: Anime): CharSequence = buildSpannedString {
        val progress = rate.episodes.invalidIfNullOrZero()
        val size =
            (if (anime.isOngoing) anime.episodesAired else anime.episodes).invalidIfNullOrZero()

        val format = " / $size"

        fontFamilyAndScale("sans-serif-medium", 1.3f) { append(progress) }
        color(dividerColor) { append(format) }
    }
}