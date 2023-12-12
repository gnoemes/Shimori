package com.gnoemes.shimori.data.shikimori.models.common

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
enum class ShikimoriContentType {
    @SerialName("Anime")
    ANIME,
    @SerialName("Manga")
    MANGA,
    @SerialName("Ranobe")
    RANOBE,
    @SerialName("Character")
    CHARACTER,
    @SerialName("Person")
    PERSON,
    @SerialName("User")
    USER,
    @SerialName("Club")
    CLUB,
    @SerialName("ClubPage")
    CLUB_PAGE,
    @SerialName("Collection")
    COLLECTION,
    @SerialName("Review")
    REVIEW,
    @SerialName("CosplayGallery")
    COSPLAY,
    @SerialName("Contest")
    CONTEST,
}