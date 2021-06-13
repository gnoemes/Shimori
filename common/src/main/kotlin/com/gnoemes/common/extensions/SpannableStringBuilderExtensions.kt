package com.gnoemes.common.extensions

import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.text.style.TypefaceSpan
import androidx.core.text.inSpans

inline fun SpannableStringBuilder.fontFamily(fontFamily: String, builderAction: SpannableStringBuilder.() -> Unit) =
    inSpans(TypefaceSpan(fontFamily), builderAction = builderAction)

inline fun SpannableStringBuilder.fontFamilyAndScale(fontFamily: String,
                                                     proportion: Float,
                                                     builderAction: SpannableStringBuilder.() -> Unit) =
    inSpans(TypefaceSpan(fontFamily), RelativeSizeSpan(proportion), builderAction = builderAction)