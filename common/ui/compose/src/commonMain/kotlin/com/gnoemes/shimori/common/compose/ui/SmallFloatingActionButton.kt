package com.gnoemes.shimori.common.compose.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun SurfaceSmallFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    SmallFloatingActionButton(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.primary,
        onClick = onClick,
        content = content
    )
}

@Composable
fun PrimarySmallFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    SmallFloatingActionButton(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        onClick = onClick,
        content = content
    )
}

@Composable
fun SecondarySmallFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    SmallFloatingActionButton(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        onClick = onClick,
        content = content
    )
}

@Composable
fun TertiarySmallFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    SmallFloatingActionButton(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        onClick = onClick,
        content = content
    )
}

@Composable
private fun SmallFloatingActionButton(
    containerColor: Color,
    contentColor: Color = contentColorFor(containerColor),
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    androidx.compose.material3.SmallFloatingActionButton(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        onClick = onClick,
        content = content
    )
}