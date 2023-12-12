package com.gnoemes.shimori.data.shikimori.models.common

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
enum class AgeRating(val rating: String) {
    @SerialName("none")
    NONE("none"),
    @SerialName("g")
    G("g"),
    @SerialName("pg")
    PG("pg"),
    @SerialName("pg_13")
    PG_13("pg_13"),
    @SerialName("r")
    R("r"),
    @SerialName("r_plus")
    R_PLUS("r_plus"),
    @SerialName("rx")
    RX("rx")
}