package com.gnoemes.shimori.common.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.common.R
import com.gnoemes.shimori.common.compose.theme.ShimoriTheme
import com.gnoemes.shimori.common.compose.theme.toolbar
import com.gnoemes.shimori.model.user.UserShort
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.TopAppBar

@Composable
fun RootScreenToolbar(
    title: String,
    showSearchButton: Boolean,
    user: UserShort? = null,
    authorized: Boolean = false,
    searchButtonClick: () -> Unit = {},
    avatarClick: () -> Unit = {}
) {
    TopAppBar(
            backgroundColor = MaterialTheme.colors.toolbar,
            contentPadding = rememberInsetsPaddingValues(
                    insets = LocalWindowInsets.current.statusBars,
                    applyStart = true,
                    applyTop = true,
                    applyEnd = true
            ),
            elevation = 0.dp,
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth(),
            title = {
                Text(
                        text = title,
                        style = MaterialTheme.typography.h3,
                        color = MaterialTheme.colors.onPrimary,
                        modifier = Modifier
                            .padding(end = 12.dp)
                )
            },
            actions = {
                if (showSearchButton) {
                    ShimoriIconButton(
                            onClick = searchButtonClick,
                            selected = false,
                            painter = painterResource(R.drawable.ic_search),
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .size(32.dp),
                    )
                }

                UserProfileButton(
                        user = user,
                        authorized = authorized,
                        avatarClick = avatarClick
                )
            }
    )
}

@Composable
@Preview
fun previewDark() {
    ShimoriTheme(useDarkColors = true) {
        RootScreenToolbar(
                title = "Anime",
                true
        )
    }
}

@Composable
@Preview
fun previewLight() {
    ShimoriTheme(useDarkColors = false) {
        RootScreenToolbar(
                title = "Anime",
                true
        )
    }
}