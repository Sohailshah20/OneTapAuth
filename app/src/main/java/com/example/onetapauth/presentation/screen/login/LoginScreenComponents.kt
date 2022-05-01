package com.example.onetapauth.presentation.screen.login

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.onetapauth.R
import com.example.onetapauth.components.GoogleButton
import com.example.onetapauth.components.MessageBar
import com.example.onetapauth.domain.model.MessageBarState
import com.example.onetapauth.ui.theme.OneTapAuthTheme

@Composable
fun LoginContent(
    signedInState : Boolean,
    messageBarState: MessageBarState,
    onButtonClicked : () -> Unit
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            MessageBar(messageBarState = messageBarState)
        }
        Column(
            modifier = Modifier
                .weight(9f)
                .fillMaxWidth(0.7f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CentralContent(
                signedInState = signedInState,
                onButtonClicked = onButtonClicked
            )
        }
    }
}

@Composable
fun CentralContent(
    signedInState : Boolean,
    onButtonClicked: () -> Unit
){
    Image(painter = painterResource(
        id = R.drawable.goole_logo),
        contentDescription = "google logo",
        modifier = Modifier
            .padding(20.dp)
            .size(120.dp)
    )
    Text(
        text = stringResource(id = R.string.signin_title),
        fontWeight = FontWeight.Bold,
        fontSize = MaterialTheme.typography.h5.fontSize
    )
    Text(
        text = stringResource(id = R.string.signin_subtitle),
        textAlign = TextAlign.Center,
        fontSize = MaterialTheme.typography.subtitle1.fontSize,
        modifier = Modifier
            .alpha(ContentAlpha.medium)
            .padding(bottom = 40.dp, top = 4.dp)
    )
    GoogleButton(
        loadingState = signedInState,
        onClick = onButtonClicked
    )
}

@Composable
fun LoginTopBar(){
    TopAppBar(
        title = {
            Text(
                text = "Sign in",
                color = MaterialTheme.colors.onSurface
            )
        },
        backgroundColor = MaterialTheme.colors.primary
    )
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name= "dark")
fun topBarPreview(){
    OneTapAuthTheme {
        LoginTopBar()
    }
}