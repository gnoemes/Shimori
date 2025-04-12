package com.gnoemes.shimori.source.model

data class SGenre(
    val id : Long,
    val name : String,
    val nameRu : String? = null,
    val type : Int,
    val description : String? = null,
)