package com.parisara.cycle.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.parisara.cycle.data.local.SessionManager
import com.parisara.cycle.data.local.entity.User
import com.parisara.cycle.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            val userId = sessionManager.loggedInUserIdFlow.first()
            if (userId != null) {
                val user = repository.getUserById(userId)
                if (user != null) {
                    _currentUser.value = user
                    _authState.value = AuthState.Authenticated
                } else {
                    sessionManager.clearSession()
                }
            }
        }
    }

    fun register(name: String, email: String, mobile: String, passwordHash: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val existingUser = repository.getUserByEmailOrMobile(email)
                if (existingUser != null) {
                    _authState.value = AuthState.Error("Email already exists")
                    return@launch
                }

                val newUser = User(name = name, email = email, mobile = mobile, passwordHash = passwordHash)
                val userId = repository.registerUser(newUser)
                
                if (userId > 0) {
                    sessionManager.saveUserId(userId)
                    _currentUser.value = newUser.copy(id = userId)
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error("Failed to register")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun login(identifier: String, passwordHash: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val user = repository.getUserByEmailOrMobile(identifier)
                if (user != null && user.passwordHash == passwordHash) {
                    sessionManager.saveUserId(user.id)
                    _currentUser.value = user
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error("Invalid credentials")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionManager.clearSession()
            _currentUser.value = null
            _authState.value = AuthState.Idle
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Authenticated : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModelFactory(
    private val repository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository, sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
