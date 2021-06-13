package com.gnoemes.shimori.data_base.mappers

fun <F, T> Mapper<F, T>.toListMapper(): suspend (List<F>) -> List<T> {
    return { list -> list.map { item -> map(item) } }
}

fun <F, T> Mapper<F, T>.toLambda(): suspend (F) -> T {
    return { map(it) }
}
