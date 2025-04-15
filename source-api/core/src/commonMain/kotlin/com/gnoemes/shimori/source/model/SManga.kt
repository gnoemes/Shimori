package com.gnoemes.shimori.source.model

import kotlinx.datetime.LocalDate

data class SManga(
    val type: SourceDataType,
    val id: Long = 0,
    val name: String = "",
    val nameRu: String? = null,
    val nameEn: String? = null,
    val image: SImage? = null,
    val url: String? = null,
    val mangaType: String? = null,
    val rating: Double? = null,
    val status: String? = null,
    val volumes: Int = 0,
    val chapters: Int = 0,
    val dateAired: LocalDate? = null,
    val dateReleased: LocalDate? = null,
    val ageRating: String? = null,
    val description: String? = null,
    val descriptionHtml: String? = null,
    val franchise: String? = null,
    val favorite: Boolean = false,
    val topicId: Long? = null,
    val genres: List<SGenre>? = null,
    val track: STrack? = null,
    val characters: List<SCharacter>? = null,
    val charactersRoles: List<SCharacterRole>? = null,
) {

    constructor(entity: SManga, track: STrack?) : this(
        type = entity.type,
        id = entity.id,
        name = entity.name,
        nameRu = entity.nameRu,
        nameEn = entity.nameEn,
        image = entity.image,
        url = entity.url,
        mangaType = entity.mangaType,
        rating = entity.rating,
        status = entity.status,
        volumes = entity.volumes,
        chapters = entity.chapters,
        dateAired = entity.dateAired,
        dateReleased = entity.dateReleased,
        ageRating = entity.ageRating,
        description = entity.description,
        descriptionHtml = entity.descriptionHtml,
        franchise = entity.franchise,
        favorite = entity.favorite,
        topicId = entity.topicId,
        genres = entity.genres,
        track = track
    )
}