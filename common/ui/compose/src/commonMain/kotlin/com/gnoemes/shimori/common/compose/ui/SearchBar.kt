package com.gnoemes.shimori.common.compose.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.gnoemes.shimori.common.ui.resources.Icons
import com.gnoemes.shimori.common.ui.resources.icons.ic_search
import com.gnoemes.shimori.common.ui.resources.icons.ic_settings
import com.gnoemes.shimori.common.ui.resources.strings.search
import com.gnoemes.shimori.common.ui.resources.util.Strings
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShimoriSearchBar(
    modifier: Modifier,
    openSettings: () -> Unit,
) {
    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                "",
                onQueryChange = {},
                onSearch = {},
                expanded = false,
                onExpandedChange = {},
                placeholder = {
                    Text(stringResource(Strings.search))
                },
                leadingIcon = {
                    Icon(
                        painterResource(Icons.ic_search),
                        contentDescription = stringResource(Strings.search),
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = openSettings,
                    ) {
                        Icon(
                            painterResource(Icons.ic_settings),
                            contentDescription = null,
                        )
                    }
                }
            )
        },
        expanded = false,
        onExpandedChange = {},
        modifier = modifier
    ) {

    }
}