@file:Suppress("NOTHING_TO_INLINE")

package com.gnoemes.shimori.base.extensions


inline fun Double.twoDigitsString(): String = "%.2f".format(this)