package com.gnoemes.shimori.common.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.common.R
import com.gnoemes.shimori.common.compose.theme.ShimoriTheme
import com.gnoemes.shimori.common.compose.theme.toolbar
import com.gnoemes.shimori.model.user.UserShort
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun RootScreenToolbar(
    title: String,
    showSearchButton: Boolean,
    searchButtonClick: () -> Unit = {},
    user: UserShort? = null,
    authorized: Boolean = false,
    avatarClick: () -> Unit = {}
) {
    TopAppBar(
            backgroundColor = MaterialTheme.colors.toolbar,
            contentPadding = PaddingValues(16.dp),
            elevation = 0.dp,
            modifier = Modifier
                .statusBarsPadding()
                .height(64.dp)
                .fillMaxWidth(),
    ) {

        Text(
                text = title,
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.onPrimary,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
        )

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