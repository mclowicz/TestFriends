package com.mclowicz.testfriends.presentation.signup

import androidx.annotation.StringRes
import com.mclowicz.testfriends.R
import com.mclowicz.testfriends.domain.user.User

data class SignupState(
    val emailValue: String = String(),
    val isEmailError: Boolean = false,
    val passwordValue: String = String(),
    val isPasswordError: Boolean = false,
    val isLoading: Boolean = false,
    val error: Error? = null,
    val user: User? = null
)

sealed class Error(@StringRes val msgResId: Int) {
    object Offline : Error(msgResId = R.string.offlineError)
    object Backend : Error(msgResId = R.string.createAccountError)
    object DuplicateAccount : Error(msgResId = R.string.duplicateAccountError)
}