package com.gnoemes.shimori.model

interface ShimoriEntity {
    val id : Long
    val contentType : ContentType
    val image : ShimoriImage
    val name : String
    val nameRu : String?
}