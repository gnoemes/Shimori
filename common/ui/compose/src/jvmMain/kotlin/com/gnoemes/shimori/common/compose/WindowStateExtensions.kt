package com.gnoemes.shimori.common.compose

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPosition

fun DpSize.encode() = "${width.value}/${height.value}"
fun String.decodeDpSize() = split('/').let {
    DpSize(it[0].toFloat().dp, it[1].toFloat().dp)
}

fun WindowPosition.encode() = "${x.value}/${y.value}"
fun String.decodeWindowPosition() = split('/').let {
    WindowPosition(it[0].toFloat().dp, it[1].toFloat().dp)
}
