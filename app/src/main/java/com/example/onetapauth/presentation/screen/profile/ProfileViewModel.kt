package com.example.onetapauth.presentation.screen.profile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onetapauth.domain.model.*
import com.example.onetapauth.domain.repository.Repository
import com.example.onetapauth.util.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _user: MutableState<User?> = mutableStateOf(null)
    val user: State<User?> = _user

    private val _firstName: MutableState<String> = mutableStateOf("")
    val firstName: State<String> = _firstName

    private val _lastName: MutableState<String> = mutableStateOf("")
    val lastName: State<String> = _lastName

    private val _apiResponse: MutableState<RequestState<ApiResponse>> = mutableStateOf(RequestState.Idle)
    val apiResponse: State<RequestState<ApiResponse>> = _apiResponse

    private val _messageBarState: MutableState<MessageBarState> = mutableStateOf(MessageBarState())
    val messageBarState: State<MessageBarState> = _messageBarState

    private val _clearSessionResponse: MutableState<RequestState<ApiResponse>> = mutableStateOf(RequestState.Idle)
    val clearSessionResponse: State<RequestState<ApiResponse>> = _clearSessionResponse

    init {
        getUserInfo()
    }

    private fun getUserInfo(){
        viewModelScope.launch  {
            _apiResponse.value = RequestState.Loading
            try {
                val result = repository.getUserInfo()
                _apiResponse.value = RequestState.Success(result)
                _messageBarState.value = MessageBarState(
                    message = result.message,
                    error = result.error
                )
                if (result.user != null){
                    _user.value = result.user
                    _firstName.value = result.user.name.split(" ").first()
                    _lastName.value = result.user.name.split(" ").last()
                }

            }catch (e :Exception){
                _apiResponse.value = RequestState.Error(e)
                _messageBarState.value = MessageBarState(
                    error = e
                )
            }
        }
    }

    fun verifyTokenOnBackend(request : ApiRequest){
        _apiResponse.value = RequestState.Loading
        try {
            viewModelScope.launch(Dispatchers.IO){
                val result = repository.verifyTokenOnBackend(request = request)
                _apiResponse.value = RequestState.Success(result)
                _messageBarState.value = MessageBarState(
                    message = result.message,
                    error = result.error
                )
            }
        }catch (e: Exception){
            _apiResponse.value = RequestState.Error(e)
            _messageBarState.value = MessageBarState(error = e)
        }
    }

    fun updateUserInfo(){
        _apiResponse.value = RequestState.Loading
        viewModelScope.launch{
            try {
                val result = withContext(Dispatchers.IO){
                    repository.getUserInfo()
                }
                if (user.value != null){
                    verifyAndUpdate(currentUser = result)
                }
            }catch (e: Exception){
                _apiResponse.value = RequestState.Error(e)
                _messageBarState.value = MessageBarState(error = e)
            }
        }
    }

    private fun verifyAndUpdate(currentUser :ApiResponse){
        val (verified, exception) = if (firstName.value.isEmpty() || lastName.value.isEmpty()){
            Pair(false, EmptyFieldException())
        }else{
            if (currentUser.user?.name?.split(" ")?.first() == firstName.value &&
                currentUser.user.name.split(" ").last() == lastName.value){
                Pair(false,NothingToUpdateException())
            }else{
                Pair(true, null)
            }
        }
        viewModelScope.launch(Dispatchers.IO){
        if (verified){
            try {
                val result = repository.updateUser(
                    userUpdate = UserUpdate(
                        firstName = firstName.value,
                        lastName = lastName.value
                    )
                )
                _apiResponse.value = RequestState.Success(result)
                _messageBarState.value = MessageBarState(
                    message = result.message,
                    error = result.error
                )
            }catch (e: Exception){
                _apiResponse.value = RequestState.Error(e)
                _messageBarState.value = MessageBarState(error = e)
            }

        }else{
            _apiResponse.value = RequestState.Success(
                ApiResponse(success = false, error = exception))
            _messageBarState.value = MessageBarState(error = exception)
        }
        }
    }

    fun clearSession(){
        _clearSessionResponse.value = RequestState.Loading
        _apiResponse.value = RequestState.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val result = repository.clearSession()
                _clearSessionResponse.value = RequestState.Success(result)
                _apiResponse.value = RequestState.Success(result)
                _messageBarState.value = MessageBarState(
                    message = result.message,
                    error = result.error
                )
            }catch (e:Exception){
                _clearSessionResponse.value = RequestState.Error(e)
                _apiResponse.value = RequestState.Error(e)
                _messageBarState.value = MessageBarState(error = e)
            }
        }
    }

    fun deleteUser(){
        _clearSessionResponse.value = RequestState.Loading
        _apiResponse.value = RequestState.Loading
        viewModelScope.launch(Dispatchers.IO){
            try {
                val result = repository.deleteUser()
                _clearSessionResponse.value = RequestState.Success(result)
                _apiResponse.value = RequestState.Success(result)
                _messageBarState.value = MessageBarState(
                    message = result.message,
                    error = result.error
                )
            }catch (e:Exception){
                _clearSessionResponse.value = RequestState.Error(e)
                _apiResponse.value = RequestState.Error(e)
                _messageBarState.value = MessageBarState(error = e)
            }
        }

    }

    fun saveSignedInState(signedIn : Boolean){
        viewModelScope.launch(Dispatchers.IO){
            repository.saveSignedInState(signedIn = signedIn)
        }
    }

    class NothingToUpdateException(
        override val message : String = "Nothing to update"
    ) : Exception()

    class EmptyFieldException(
        override val message : String = "Input Field Empty"
    ) : Exception()

    fun updateFirstName(newName : String){
        if (newName.length < 20){
            _firstName.value = newName
        }
    }

    fun updateLastName(newName : String){
        if (newName.length < 20){
            _lastName.value = newName
        }
    }


}