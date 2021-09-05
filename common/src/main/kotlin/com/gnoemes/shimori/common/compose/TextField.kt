package com.gnoemes.shimori.common.compose

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun ShimoriTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.caption,
    textColor: Color = MaterialTheme.colors.onPrimary,
    handleColor: Color = MaterialTheme.colors.secondary,
    selectionBackgroundColor: Color = MaterialTheme.colors.secondaryVariant,
    cursorColor: Color = MaterialTheme.colors.secondary,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    hint: @Composable () -> Unit = {},
    decorationBox: @Composable (innerTextField: @Composable () -> Unit) -> Unit =
        @Composable { innerTextField -> innerTextField() }
) {
    val customTextSelectionColors = TextSelectionColors(
            handleColor = handleColor,
            backgroundColor = selectionBackgroundColor
    )

    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        val inputField = @Composable {
            BasicTextField(
                    value = value,
                    textStyle = textStyle.copy(color = textColor),
                    onValueChange = onValueChange,
                    modifier = modifier,
                    cursorBrush = SolidColor(cursorColor),
                    keyboardActions = keyboardActions,
                    keyboardOptions = keyboardOptions,
                    enabled = enabled,
                    readOnly = readOnly,
                    singleLine = singleLine,
                    maxLines = maxLines,
                    visualTransformation = visualTransformation,
                    onTextLayout = onTextLayout,
                    interactionSource = interactionSource,
                    decorationBox = decorationBox
            )
        }

        Layout(
                content = {
                    inputField()
                    hint()
                }
        ) { measurables, constraints ->
            val inputFieldPlace = measurables[0].measure(constraints)
            val hintEditPlace = measurables[1].measure(constraints)

            layout(
                    inputFieldPlace.width,
                    inputFieldPlace.height
            ) {
                inputFieldPlace.placeRelative(0, 0)
                if (value.isEmpty()) hintEditPlace.place(0, 0)
            }
        }

    }
}