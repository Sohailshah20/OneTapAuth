package com.example.onetapauth.domain.repository

import com.example.onetapauth.domain.model.ApiRequest
import com.example.onetapauth.domain.model.ApiResponse
import com.example.onetapauth.domain.model.UserUpdate
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun saveSignedInState(signedIn : Boolean)

    fun readSignedInState() : Flow<Boolean>

    suspend fun verifyTokenOnBackend(request: ApiRequest) : ApiResponse

    suspend fun getUserInfo() : ApiResponse

    suspend fun updateUser( userUpdate: UserUpdate) : ApiResponse

    suspend fun deleteUser() : ApiResponse

    suspend fun clearSession() : ApiResponse
}