package com.gnoemes.shimori.common.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.gnoemes.shimori.common.ui.statusBarHeight
import com.gnoemes.shimori.data.core.entities.user.UserShort
import com.gnoemes.shimori.ui.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShimoriMainToolbar(
    modifier: Modifier = Modifier,
    title: String,
    onSearchClick: () -> Unit,
    onUserClick: () -> Unit,
    user: UserShort? = null,
    containerColor: Color = MaterialTheme.colorScheme.background.copy(alpha = 0.96f),
    navigationIconContentColor: Color = MaterialTheme.colorScheme.onSurface,
    titleContentColor: Color = MaterialTheme.colorScheme.onSurface,
    actionIconContentColor: Color = MaterialTheme.colorScheme.onSurface,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    Column(
        Modifier.background(containerColor)
    ) {

        Spacer(
            modifier = Modifier.statusBarHeight(),
        )

        SmallTopAppBar(
            title = { Text(text = title) },
            scrollBehavior = scrollBehavior,
            actions = {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search),
                        contentDescription = stringResource(id = R.string.search),
                        modifier = Modifier.size(24.dp)
                    )
                }

                IconButton(onClick = onUserClick) {
                    val avatar = user?.image?.preview
                    if (avatar != null) {
                        Image(
                            painter = rememberImagePainter(avatar, builder = {
                                crossfade(true)
                            }),
                            contentDescription = stringResource(R.string.profile),
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape),
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_profile),
                            contentDescription = stringResource(id = R.string.profile),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent,
                navigationIconContentColor = navigationIconContentColor,
                titleContentColor = titleContentColor,
                actionIconContentColor = actionIconContentColor
            )
        )
    }
}

@Composable
fun ShimoriSecondaryToolbar(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    title: String,
    actions: @Composable RowScope.() -> Unit = {},
    subTitle: String? = null,
    colors: TopAppBarColors = TopAppBarDefaults.smallTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.background,
        scrolledContainerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.96f),
        navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        actionIconContentColor = MaterialTheme.colorScheme.onSurface
    ),
) {
    SmallTopAppBar(title = {
        if (subTitle == null) {
            Text(text = title)
        } else {
            Column(
                modifier = Modifier.padding(start = 12.dp)
            ) {
                Text(
                    text = title, style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = subTitle,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }, modifier = modifier, navigationIcon = {
        BackIcon(navigateUp)
    }, actions = actions, colors = colors
    )
}