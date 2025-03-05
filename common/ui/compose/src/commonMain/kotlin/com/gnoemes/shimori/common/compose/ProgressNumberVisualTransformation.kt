package com.gnoemes.shimori.common.compose

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class ProgressNumberVisualTransformation(
    private val limit: Int
) : VisualTransformation {

    private companion object {
        const val DELIMITER = " / "
    }

    override fun filter(text: AnnotatedString): TransformedText {
        val numberValue = getProgress(text)
        val clampedValue = if (numberValue > limit) limit else numberValue
        val outputNumber = clampedValue.toString()

        val transformedTextString = "$outputNumber$DELIMITER$limit"

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return if (text.text.isEmpty()) 1 else offset
            }

            override fun transformedToOriginal(offset: Int): Int {
                return if (text.text.isEmpty()) 0
                else {
                    if (offset <= outputNumber.length) offset else outputNumber.length
                }
            }
        }

        return TransformedText(AnnotatedString(transformedTextString), offsetMapping)
    }

    fun getProgress(text: CharSequence): Int =
        text.split(DELIMITER)
            .firstOrNull()
            ?.toIntOrNull() ?: 0
}