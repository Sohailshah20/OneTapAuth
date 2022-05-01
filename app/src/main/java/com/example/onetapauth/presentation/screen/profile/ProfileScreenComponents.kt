package com.example.onetapauth.presentation.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.onetapauth.R
import com.example.onetapauth.components.DisplayAlertDialog
import com.example.onetapauth.components.GoogleButton
import com.example.onetapauth.components.MessageBar
import com.example.onetapauth.domain.model.ApiResponse
import com.example.onetapauth.domain.model.MessageBarState
import com.example.onetapauth.ui.theme.LoadingBlue
import com.example.onetapauth.util.RequestState

@Composable
fun ProfileTopBar(
    onSave : () -> Unit,
    onDeleteAllConfirmed : () -> Unit,
){
    TopAppBar(
        title = {
            Text(
                text = "Profile",
                color = MaterialTheme.colors.onSurface
            )
        },
        backgroundColor = MaterialTheme.colors.primary,
        actions = {
            ProfileTopBarActions(
                onSave = onSave,
                onDeleteAllConfirmed = onDeleteAllConfirmed
            )
        }
    )
}

@Composable
fun ProfileTopBarActions(
    onSave : () -> Unit,
    onDeleteAllConfirmed : () -> Unit,
) {

    var openDialog by remember { mutableStateOf(false)}

    DisplayAlertDialog(
        openDialog = openDialog,
        onYesClicked = {onDeleteAllConfirmed()},
        onDialogClosed = {openDialog = false}
    )

    SaveAction(onSave = onSave)
    DeleteAllAction(onDeleteAllConfirmed = {openDialog = true})
}

@Composable
fun SaveAction(onSave : () -> Unit) {
    IconButton(onClick = onSave) {
        Icon(painter = painterResource(id = R.drawable.save_icon),
            contentDescription = "saveIcon",
            tint = MaterialTheme.colors.onSurface
        )
    }
}

@Composable
fun DeleteAllAction(
    onDeleteAllConfirmed : () -> Unit,
){
    var expanded by remember { mutableStateOf(false)}
    IconButton(onClick = {expanded = true}) {
        Icon(painter = painterResource(id = R.drawable.vertical_menu),
            contentDescription = "delete icon",
            tint = MaterialTheme.colors.onSurface
        )
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false}
    ) {
        DropdownMenuItem(
            onClick = {
            expanded = false
                onDeleteAllConfirmed()
        }) {
            Text(
                text = "Delete Account",
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
}

@Composable
fun ProfileContent(
    apiResponse : RequestState<ApiResponse>,
    messageBarState : MessageBarState,
    firstname : String,
    onFirstNameChanged: (String) -> Unit,
    lastName : String,
    onLastNameChanged : (String) -> Unit,
    emailAddress : String?,
    profilePhoto : String?,
    onSignOutClicked : () -> Unit
){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            if (apiResponse is RequestState.Loading){
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = LoadingBlue
                )
            }else{
                MessageBar(messageBarState = messageBarState)
            }
        }
        Column(
            modifier = Modifier.weight(9f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileCentralContent(
                firstname = firstname,
                onFirstNameChanged = onFirstNameChanged,
                lastName = lastName,
                onLastNameChanged = onLastNameChanged,
                emailAddress = emailAddress,
                profilePhoto = profilePhoto,
                onSignOutClicked = onSignOutClicked
            )
        }

    }
}

@Composable
fun ProfileCentralContent(
    firstname: String,
    onFirstNameChanged: (String) -> Unit,
    lastName: String,
    onLastNameChanged: (String) -> Unit,
    emailAddress: String?,
    profilePhoto: String?,
    onSignOutClicked: () -> Unit
){
    val painter = rememberImagePainter(data = profilePhoto){
        crossfade(1000)
        placeholder(R.drawable.place_holder_light)
    }

    Image(
        modifier = Modifier
            .padding(bottom = 40.dp)
            .size(150.dp)
            .clip(CircleShape)
                ,
        painter = painter,
        contentDescription = "Profile Photo"
    )
    OutlinedTextField(
        value = firstname,
        onValueChange = {onFirstNameChanged(it)},
        label = { Text(text = "First Name")},
        textStyle = MaterialTheme.typography.body1,
        singleLine = true
    )
    OutlinedTextField(
        value = lastName,
        onValueChange = {onLastNameChanged(it)},
        label = { Text(text = "last Name")},
        textStyle = MaterialTheme.typography.body1,
        singleLine = true
    )
    OutlinedTextField(
        value = emailAddress.toString(),
        onValueChange = {},
        label = { Text(text = "Email Address")},
        textStyle = MaterialTheme.typography.body1,
        singleLine = true,
        enabled = false
    )
    GoogleButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ,
        defaultText = "Sign Out",
        loadingText = "Sign Out",
        onClick = onSignOutClicked
    )
}




@Composable
@Preview
fun topBartestPreview(){
    ProfileTopBar(onSave = {},
    onDeleteAllConfirmed = {})
}
