package com.example.onetapauth.presentation.screen.login

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.onetapauth.domain.model.ApiRequest
import com.example.onetapauth.domain.model.ApiResponse
import com.example.onetapauth.domain.model.MessageBarState
import com.example.onetapauth.domain.repository.Repository
import com.example.onetapauth.util.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _signedInState: MutableState<Boolean> = mutableStateOf(false)
    val signedInState: State<Boolean> = _signedInState

    private val _messageBarState: MutableState<MessageBarState> = mutableStateOf(MessageBarState())
    val messageBarState: State<MessageBarState> = _messageBarState

    private val _apiResponse: MutableState<RequestState<ApiResponse>> = mutableStateOf(RequestState.Idle)
    val apiResponse: State<RequestState<ApiResponse>> = _apiResponse

    init {
        viewModelScope.launch {
            repository.readSignedInState().collect {
                _signedInState.value = it
            }
        }
    }

    fun saveSignedInState(signedIn : Boolean){
        viewModelScope.launch(Dispatchers.IO){
            repository.saveSignedInState(signedIn = signedIn)
        }
    }

    fun updateMessageBarState(){
        _messageBarState.value = MessageBarState(error = GoogleAccountNotFoundException())
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


}

class GoogleAccountNotFoundException(
    override val message: String? = "Google Account Not Found"
) : Exception()
