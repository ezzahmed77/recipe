package com.example.recipe.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.UNAUTHENTICATED)
    val authState: StateFlow<AuthState>
        get() = _authState

    private val _authMessage = MutableStateFlow("")
    val authMessage : StateFlow<String>
        get() = _authMessage

    fun login(email: String, password: String) {
        _authState.value = AuthState.AUTHENTICATING
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                _authState.value = AuthState.AUTHENTICATED
                _authMessage.value = "Signed In Successfully"
            } catch (e: Exception) {
                _authState.value = AuthState.INVALID_AUTHENTICATION
                _authMessage.value = e.message.toString()
            }
        }
    }

    fun register(email: String, password: String) {
        _authState.value = AuthState.AUTHENTICATING
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password).await()
                _authState.value = AuthState.AUTHENTICATED
                _authMessage.value = "Welcome"
            } catch (e: Exception) {
                _authState.value = AuthState.INVALID_AUTHENTICATION
                _authMessage.value = e.message.toString()
            }
        }
    }

    enum class AuthState {
        AUTHENTICATED,
        UNAUTHENTICATED,
        AUTHENTICATING,
        INVALID_AUTHENTICATION
    }
}
