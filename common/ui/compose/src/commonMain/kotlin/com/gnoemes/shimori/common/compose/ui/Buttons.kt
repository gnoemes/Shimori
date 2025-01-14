package com.gnoemes.shimori.common.compose.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.common.compose.theme.disabledContainerAlpha

@Composable
fun FilledButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.medium,
    colors: ButtonColors = ButtonDefaults.filledTonalButtonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = contentColorFor(MaterialTheme.colorScheme.primary),
        disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = disabledContainerAlpha),
        disabledContentColor = MaterialTheme.colorScheme.onSurface
    ),
    elevation: ButtonElevation? = ButtonDefaults.filledTonalButtonElevation(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    icon: (@Composable () -> Unit)? = null,
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        contentPadding = contentPadding,
        content = {
            if (icon != null) {
                icon()

                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(text = text)
        }
    )
}

@Composable
fun TonalButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.medium,
    colors: ButtonColors = ButtonDefaults.filledTonalButtonColors(
        disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = disabledContainerAlpha),
        disabledContentColor = MaterialTheme.colorScheme.onSurface
    ),
    elevation: ButtonElevation? = ButtonDefaults.filledTonalButtonElevation(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    icon: (@Composable () -> Unit)? = null,
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        contentPadding = contentPadding,
        content = {
            if (icon != null) {
                icon()

                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(text = text)
        }
    )
}

@Composable
fun TextButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.medium,
    colors: ButtonColors = ButtonDefaults.textButtonColors(
        disabledContentColor = MaterialTheme.colorScheme.onSurface
    ),
    elevation: ButtonElevation? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    icon: (@Composable () -> Unit)? = null,
) {
    androidx.compose.material3.TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        contentPadding = contentPadding,
        content = {
            if (icon != null) {
                icon()

                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(text = text)
        }
    )
}

@Composable
fun OutlinedButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.medium,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.secondary,
        disabledContainerColor = Color.Transparent,
        disabledContentColor = MaterialTheme.colorScheme.onSurface
    ),
    elevation: ButtonElevation? = null,
    border: BorderStroke? = BorderStroke(
        width = 1.dp,
        color = MaterialTheme.colorScheme.outlineVariant
    ),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    icon: (@Composable () -> Unit)? = null,
) {
    androidx.compose.material3.OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        contentPadding = contentPadding,
        border = border,
        content = {
            if (icon != null) {
                icon()

                Spacer(modifier = Modifier.width(8.dp))
            }

            Text(text = text)
        }
    )
}