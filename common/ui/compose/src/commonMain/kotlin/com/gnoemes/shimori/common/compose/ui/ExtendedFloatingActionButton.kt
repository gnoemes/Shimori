package com.gnoemes.shimori.common.compose.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun ExtendedSurfaceFloatingActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit
) {
    ExtendedFloatingActionButton(
        text = text,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary,
        onClick = onClick,
        icon = icon
    )
}

@Composable
fun ExtendedPrimaryFloatingActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit
) {
    ExtendedFloatingActionButton(
        text = text,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        onClick = onClick,
        icon = icon
    )
}

@Composable
fun ExtendedSecondaryFloatingActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit
) {
    ExtendedFloatingActionButton(
        text = text,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        onClick = onClick,
        icon = icon
    )
}

@Composable
fun ExtendedTertiaryFloatingActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit
) {
    ExtendedFloatingActionButton(
        text = text,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        onClick = onClick,
        icon = icon
    )
}

@Composable
private fun ExtendedFloatingActionButton(
    text: String,
    containerColor: Color,
    contentColor: Color = contentColorFor(containerColor),
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    androidx.compose.material3.ExtendedFloatingActionButton(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        onClick = onClick,
        content = {
            icon()
            Spacer(Modifier.width(12.dp))
            Text(text)
        }
    )
}