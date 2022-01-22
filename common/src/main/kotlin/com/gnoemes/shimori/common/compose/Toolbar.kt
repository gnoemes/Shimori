package com.gnoemes.shimori.common.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ShimoriSecondaryToolbar(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    title: String,
    actions: @Composable RowScope.() -> Unit = {},
    subTitle: String? = null,
    colors: TopAppBarColors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.background,
        scrolledContainerColor = MaterialTheme.colorScheme.background,
        navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        actionIconContentColor = MaterialTheme.colorScheme.onSurface
    ),
) {
    SmallTopAppBar(
        title = {
            if (subTitle == null) {
                Text(text = title)
            } else {
                Column(
                    modifier = Modifier.padding(start = 12.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = subTitle,
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        },
        modifier = Modifier
            .then(modifier),
        navigationIcon = {
            BackIcon(navigateUp)
        },
        actions = actions,
        colors = colors
    )
}