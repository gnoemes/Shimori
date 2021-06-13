package com.gnoemes.shimori.model.common

import com.google.gson.annotations.SerializedName

enum class ContentStatus(val status: String) {
    @SerializedName("anons")
    ANONS("anons"),
    @SerializedName("ongoing")
    ONGOING("ongoing"),
    @SerializedName("released")
    RELEASED("released")
}