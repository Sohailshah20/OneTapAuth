package com.example.onetapauth.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.onetapauth.R
import com.example.onetapauth.ui.theme.LoadingBlue

@Composable
fun GoogleButton(
    modifier: Modifier = Modifier,
    loadingState: Boolean = false,
    defaultText: String = "Sign in with Google",
    loadingText: String = "Please Wait",
    icon: Int = R.drawable.goole_logo,
    shape: Shape = shapes.medium,
    borderColor: Color = Color.LightGray,
    borderStrokewidth: Dp = 1.dp,
    backgroundColor: Color = MaterialTheme.colors.surface,
    progressIndicatorColor: Color = LoadingBlue,
    onClick: () -> Unit
){
    var buttonText by remember{ mutableStateOf(defaultText) }

    LaunchedEffect(key1 = loadingState){
        buttonText = if (loadingState) loadingText else defaultText
    }

    Surface(
        modifier = modifier
            .clickable(enabled = !loadingState){
                onClick()
            },
        shape = shape,
        border = BorderStroke(width = borderStrokewidth, color = borderColor),
        color = backgroundColor
    ){
        Row(
            modifier = modifier
                .padding(
                    12.dp
                )
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = "googleLogo",
                tint = Color.Unspecified
            )
            Spacer(modifier = modifier.width(8.dp))
            Text(text = buttonText)
            if (loadingState){
                Spacer(modifier = modifier.width(16.dp))
                CircularProgressIndicator(
                    modifier = modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = progressIndicatorColor
                )
            }
        }
    }
}

@Composable
@Preview
fun buttonPreview(){
    GoogleButton{}
}