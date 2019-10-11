package com.gnoemes.shimori.data.extensions

import com.gnoemes.shimori.data.mappers.Mapper

fun <F, T> Mapper<F, T>.toListMapper(): suspend (List<F>) -> List<T> {
    return { list -> list.map { item -> map(item) } }
}
