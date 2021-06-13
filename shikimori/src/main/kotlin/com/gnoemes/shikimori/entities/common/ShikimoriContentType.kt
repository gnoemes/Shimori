package com.gnoemes.shikimori.entities.common

import com.google.gson.annotations.SerializedName

internal enum class ShikimoriContentType {
    @SerializedName("Anime")
    ANIME,
    @SerializedName("Manga")
    MANGA,
    @SerializedName("Ranobe")
    RANOBE,
    @SerializedName("Character")
    CHARACTER,
    @SerializedName("Person")
    PERSON,
    @SerializedName("User")
    USER,
    @SerializedName("Club")
    CLUB,
    @SerializedName("ClubPage")
    CLUB_PAGE,
    @SerializedName("Collection")
    COLLECTION,
    @SerializedName("Review")
    REVIEW,
    @SerializedName("CosplayGallery")
    COSPLAY,
    @SerializedName("Contest")
    CONTEST,
}