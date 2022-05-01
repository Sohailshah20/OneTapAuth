package com.example.onetapauth.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiRequest(
     val tokenId : String
)
