package com.gnoemes.shimori.data.common

@kotlinx.serialization.Serializable
enum class AgeRating(val rating: String) {
    NONE("none"),
    G("g"),
    PG("pg"),
    PG_13("pg_13"),
    R("r"),
    R_PLUS("r_plus"),
    RX("rx")
}