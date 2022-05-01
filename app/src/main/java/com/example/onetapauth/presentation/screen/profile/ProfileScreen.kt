package com.example.onetapauth.presentation.screen

import android.app.Activity
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.onetapauth.domain.model.ApiRequest
import com.example.onetapauth.domain.model.ApiResponse
import com.example.onetapauth.navigation.Screen
import com.example.onetapauth.presentation.screen.commom.StartActivityForResult
import com.example.onetapauth.presentation.screen.commom.signIn
import com.example.onetapauth.presentation.screen.profile.ProfileContent
import com.example.onetapauth.presentation.screen.profile.ProfileTopBar
import com.example.onetapauth.presentation.screen.profile.ProfileViewModel
import com.example.onetapauth.util.RequestState
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.Api
import retrofit2.HttpException

@Composable
fun ProfileScreen(
    navController: NavHostController,
    profileViewModel: ProfileViewModel = hiltViewModel()
){
    val apiResponse by profileViewModel.apiResponse
    val clearSessionResponse by profileViewModel.clearSessionResponse
    val messageBarState by profileViewModel.messageBarState
    val user by profileViewModel.user
    val firstName by profileViewModel.firstName
    val lastName by profileViewModel.lastName

    Scaffold(
      topBar = {
          ProfileTopBar(
              onSave = {
                       profileViewModel.updateUserInfo()
              },
              onDeleteAllConfirmed = {
                  profileViewModel.deleteUser()
              }
          )
      },
        content = {
            ProfileContent(
                apiResponse = apiResponse,
                messageBarState = messageBarState,
                firstname = firstName,
                onFirstNameChanged = {
                    profileViewModel.updateFirstName(it)
                },
                lastName = lastName,
                onLastNameChanged = {
                    profileViewModel.updateLastName(it)
                },
                emailAddress = user?.emailAddress,
                profilePhoto = user?.profilePicture,
                onSignOutClicked = {
                    profileViewModel.clearSession()
                }
            )
        }
    )

    val activity = LocalContext.current as Activity

    StartActivityForResult(
        key = apiResponse,
        onResultReceived = { tokenId ->
            profileViewModel.verifyTokenOnBackend(request = ApiRequest(tokenId = tokenId))
        },
        onDialogDismissed = {
            profileViewModel.saveSignedInState(signedIn = false)
            navigateToLoginScreen(navController = navController)
        }
    ){ activityLauncher ->
        if (apiResponse is RequestState.Success){
            val result = (apiResponse as RequestState.Success<ApiResponse>).data
            if (result.error is HttpException && result.error.code() == 401){
                signIn(
                    activity = activity,
                    accountNotFound = {
                        profileViewModel.saveSignedInState(signedIn = false)
                        navigateToLoginScreen(navController = navController)
                    },
                    launchActivityResult = {
                        activityLauncher.launch(it)
                    }
                )
            }
        }

    }

    LaunchedEffect(key1 = clearSessionResponse){
        if (clearSessionResponse is RequestState.Success &&
            (clearSessionResponse as RequestState.Success<ApiResponse>).data.success
        ){
            val oneTapClient = Identity.getSignInClient(activity)
            oneTapClient.signOut()
            profileViewModel.saveSignedInState(signedIn  = false)
            navigateToLoginScreen(navController = navController)
        }
    }
}

private fun navigateToLoginScreen(
    navController: NavHostController
){
    navController.navigate(route = Screen.Login.route){
        popUpTo(route = Screen.Profile.route){
            inclusive = true
        }
    }
}