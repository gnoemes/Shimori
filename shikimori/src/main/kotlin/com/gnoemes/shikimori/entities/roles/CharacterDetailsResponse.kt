package com.gnoemes.shikimori.entities.roles

import com.gnoemes.shikimori.entities.anime.AnimeResponse
import com.gnoemes.shikimori.entities.common.ImageResponse
import com.gnoemes.shikimori.entities.manga.MangaResponse
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
internal data class CharacterDetailsResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("russian") val nameRu: String?,
    @SerialName("image") val image: ImageResponse,
    @SerialName("url") val url: String,
    @SerialName("altname") val nameAlt: String?,
    @SerialName("japanese") val nameJp: String?,
    @SerialName("description") val description: String?,
    @SerialName("description_source") val descriptionSource: String?,
    @SerialName("seyu") val seyu: List<PersonResponse>?,
    @SerialName("animes") val animes: List<AnimeResponse>,
    @SerialName("mangas") val mangas: List<MangaResponse>
)