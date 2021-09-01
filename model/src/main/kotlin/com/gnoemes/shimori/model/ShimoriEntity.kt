package com.gnoemes.shimori.model

import com.gnoemes.shimori.model.common.ShimoriImage
import com.gnoemes.shimori.model.rate.Rate

interface ShimoriEntity {
    val id: Long
}

interface ShikimoriEntity {
    val shikimoriId: Long?
}

interface ShikimoriContentEntity : ShikimoriEntity {
    val image: ShimoriImage?
    val name: String
    val nameRu: String?
    val size : Int?
}

interface EntityWithRate<E : ShimoriEntity> {
    var entity: E
    var relations: List<Rate>?
    val rate : Rate?
}