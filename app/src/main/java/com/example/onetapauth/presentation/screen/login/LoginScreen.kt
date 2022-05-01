package com.example.onetapauth.presentation.screen

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.onetapauth.R
import com.example.onetapauth.components.GoogleButton
import com.example.onetapauth.components.MessageBar
import com.example.onetapauth.domain.model.ApiRequest
import com.example.onetapauth.domain.model.ApiResponse
import com.example.onetapauth.domain.model.MessageBarState
import com.example.onetapauth.navigation.Screen
import com.example.onetapauth.presentation.screen.commom.StartActivityForResult
import com.example.onetapauth.presentation.screen.commom.signIn
import com.example.onetapauth.presentation.screen.login.LoginContent
import com.example.onetapauth.presentation.screen.login.LoginTopBar
import com.example.onetapauth.presentation.screen.login.LoginViewModel
import com.example.onetapauth.ui.theme.OneTapAuthTheme
import com.example.onetapauth.util.RequestState

@Composable
fun LoginScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel = hiltViewModel()
){
    val signedInState by loginViewModel.signedInState
    val messageBarState by loginViewModel.messageBarState
    val apiResponse by loginViewModel.apiResponse

    Scaffold(
      topBar = {
            LoginTopBar()
      },
    content = {
        LoginContent(
            signedInState = signedInState,
            messageBarState = messageBarState ,
            onButtonClicked = {
                loginViewModel.saveSignedInState(signedIn = true)
            }

        )
    }
    )
    val activity = LocalContext.current as Activity
    StartActivityForResult(
        key = signedInState,
        onResultReceived = { tokenId ->
            loginViewModel.verifyTokenOnBackend(
                request = ApiRequest(tokenId = tokenId)  )
        },
        onDialogDismissed = {
            loginViewModel.saveSignedInState(signedIn = false)
        },
    ){ activityLauncher ->
        if (signedInState){
            signIn(
                activity = activity,
                launchActivityResult = { intentSenderRequest ->
                    activityLauncher.launch(intentSenderRequest)
                },
                accountNotFound = {
                    loginViewModel.saveSignedInState(signedIn = false)
                    loginViewModel.updateMessageBarState()
                }
            )
        }
    }
    LaunchedEffect(key1 = apiResponse){
        when(apiResponse){
            is RequestState.Success ->{
                val response = (apiResponse as RequestState.Success<ApiResponse>).data.success
                if (response){
                    navigateToProfileScreen(navController = navController)
                }else{
                    loginViewModel.saveSignedInState(signedIn = false)
                }

            } else ->{}
        }
    }
}

private fun navigateToProfileScreen(
    navController: NavHostController
){
    navController.navigate(route = Screen.Profile.route){
        popUpTo(route = Screen.Login.route){
            inclusive = true
        }
    }
}

