package com.gnoemes.shimori.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.common.compose.LocalShimoriIconsUtil
import com.gnoemes.shimori.common.ui.resources.Icons
import com.gnoemes.shimori.common.ui.resources.icons.ic_profile
import com.gnoemes.shimori.common.ui.resources.strings.login
import com.gnoemes.shimori.common.ui.resources.util.Strings
import com.gnoemes.shimori.screens.AuthScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
@CircuitInject(screen = AuthScreen::class, UiScope::class)
internal fun AuthUi(
    state: AuthUiState,
    modifier: Modifier = Modifier
) {

    val eventSink = state.eventSink
    AuthUi(
        state = state,
        signIn = { eventSink(AuthUiEvent.SignIn(it)) }
    )
}

@Composable
private fun AuthUi(
    state: AuthUiState,
    signIn: (sourceId: Long) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(Icons.ic_profile),
                contentDescription = null,
                modifier = Modifier.size(96.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                stringResource(Strings.login),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            state.availableSources.forEach { source ->
                FilledTonalButton(
                    modifier = Modifier
                        .widthIn(max = 328.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    onClick = { signIn(source.id) },
                    content = {
                        val icon = LocalShimoriIconsUtil.current.sourceIcon(source)
                        if (icon != null) {
                            Icon(
                                painter = painterResource(icon),
                                contentDescription = source.name
                            )

                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        Text(text = source.name)
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}