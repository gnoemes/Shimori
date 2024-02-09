package com.gnoemes.shimori.common.compose.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun SurfaceFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    FloatingActionButton(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary,
        onClick = onClick,
        content = content
    )
}

@Composable
fun PrimaryFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    FloatingActionButton(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        onClick = onClick,
        content = content
    )
}

@Composable
fun SecondaryFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    FloatingActionButton(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        onClick = onClick,
        content = content
    )
}

@Composable
fun TertiaryFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    FloatingActionButton(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        onClick = onClick,
        content = content
    )
}

@Composable
private fun FloatingActionButton(
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color = contentColorFor(containerColor),
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    androidx.compose.material3.FloatingActionButton(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        onClick = onClick,
        content = content
    )
}