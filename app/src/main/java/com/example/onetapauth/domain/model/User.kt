package com.example.onetapauth.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id : String,
    val name : String,
    val emailAddress : String,
    val profilePicture: String
)
