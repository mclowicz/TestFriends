package com.mclowicz.testfriends.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mclowicz.testfriends.domain.user.UserRepository
import com.mclowicz.testfriends.domain.validation.RegexCredentialsValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    val credentialsValidator: RegexCredentialsValidator,
    val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow<SignupState>(SignupState())
    val state: StateFlow<SignupState> = _state

    private val _event = MutableStateFlow<SignUpEvent>(SignUpEvent.FillingForm)
    val event: StateFlow<SignUpEvent> = _event

    fun setEmailField(value: String) {
        _state.value = _state.value.copy(
            emailValue = value,
            isEmailError = credentialsValidator.isEmailValid(value)
        )
    }

    fun setPasswordField(value: String) {
        _state.value = _state.value.copy(
            passwordValue = value,
            isPasswordError = credentialsValidator.isPasswordValid(value)
        )
    }

    fun createAccount() {
        viewModelScope.launch {
            with(_state.value) {
                if (!isEmailError && !isPasswordError &&
                    emailValue.isNotBlank() && passwordValue.isNotBlank()
                ) {
                    _state.emit(_state.value.copy(isLoading = true))
                    val result = withContext(Dispatchers.IO) {
                        userRepository.signUp(
                            email = emailValue,
                            password = passwordValue
                        )
                    }
                    if (result.user != null) {
                        _event.emit(SignUpEvent.SignedUp(result.user))
                    }
                    _state.emit(result.copy(isLoading = false))
                }
            }
        }
    }
}