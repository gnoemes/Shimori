package com.gnoemes.shimori.common.compose.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.gnoemes.shimori.common.ui.resources.Icons
import com.gnoemes.shimori.common.ui.resources.icons.ic_back
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransparentToolbar(
    onNavigationClick: () -> Unit,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    title: (@Composable () -> Unit) = {},
    navigationIcon: (@Composable () -> Unit) = {
        IconButton(
            onClick = onNavigationClick,
        ) {
            Icon(painterResource(Icons.ic_back), contentDescription = null)
        }
    }
) {
    TopAppBar(
        title = title,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
            navigationIconContentColor = contentColor,
            actionIconContentColor = contentColor
        ),
        navigationIcon = navigationIcon,
    )
}