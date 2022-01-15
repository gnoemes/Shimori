//package com.gnoemes.shimori.auth
//
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material.Icon
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import com.gnoemes.shimori.common.R
//import com.gnoemes.shimori.common.compose.ChevronIcon
//import com.gnoemes.shimori.common.compose.EnlargedButton
//import com.gnoemes.shimori.common.compose.theme.alpha
//import com.gnoemes.shimori.common.compose.theme.caption
//
//@Composable
//fun Auth() {
//    Auth(viewModel = hiltViewModel())
//}
//
//@Composable
//private fun Auth(
//    viewModel: AuthViewModel,
//) {
//    val signInLauncher =
//        rememberLauncherForActivityResult(viewModel.buildLoginActivityResult()) { result ->
//            if (result != null) {
//                viewModel.onLoginResult(result)
//            }
//        }
//
//    val signUpLauncher =
//        rememberLauncherForActivityResult(viewModel.buildRegisterActivityResult()) { result ->
//            if (result != null) {
//                viewModel.onLoginResult(result)
//            }
//        }
//
//    Auth(
//            signIn = { signInLauncher.launch(Unit) },
//            signUp = { signUpLauncher.launch(Unit) },
//    )
//}
//
//@Composable
//private fun Auth(
//    signIn: () -> Unit,
//    signUp: () -> Unit,
//) {
//
//    Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = 16.dp)
//    ) {
//
//        Spacer(modifier = Modifier.height(152.dp))
//
//        Icon(
//                painter = painterResource(id = R.drawable.ic_profile),
//                contentDescription = stringResource(id = R.string.profile),
//                modifier = Modifier
//                    .size(96.dp)
//                    .background(color = MaterialTheme.colors.alpha, shape = CircleShape)
//                    .padding(24.dp)
//                    .align(Alignment.CenterHorizontally)
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text(
//                text = stringResource(id = R.string.sign_in_title),
//                color = MaterialTheme.colors.onPrimary,
//                style = MaterialTheme.typography.h2,
//                textAlign = TextAlign.Center,
//                modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Text(
//                text = stringResource(id = R.string.sign_in_message),
//                color = MaterialTheme.colors.caption,
//                style = MaterialTheme.typography.caption,
//                textAlign = TextAlign.Center,
//                modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(64.dp))
//
//        EnlargedButton(
//                selected = false,
//                onClick = signIn,
//                painter = painterResource(id = R.drawable.ic_sign_in),
//                text = stringResource(id = R.string.sign_in),
//                modifier = Modifier
//                    .height(48.dp)
//                    .fillMaxWidth()
//        ) {
//            ChevronIcon()
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        EnlargedButton(
//                selected = false,
//                onClick = signUp,
//                painter = painterResource(id = R.drawable.ic_create_account),
//                text = stringResource(id = R.string.sign_up),
//                modifier = Modifier
//                    .height(48.dp)
//                    .fillMaxWidth()
//        ) {
//            ChevronIcon()
//        }
//    }
//}