package com.mclowicz.testfriends.domain.validation

import java.util.regex.Pattern

class RegexCredentialsValidator {

    companion object {
        private const val EMAIL_REGEX =
            """[a-zA-Z0-9+._%\-]{1,64}@[a-zA-Z0-9][a-zA-Z0-9<-]{1,64}(\.[a-zA-Z0-9][a-zA-Z0-9\-]{1,25})"""
        private const val PASSWORD_REGEX =
            """^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*+=\-]).{8,}$"""
    }

    private val emailPattern = Pattern.compile(EMAIL_REGEX)
    private val passwordPattern = Pattern.compile(PASSWORD_REGEX)

    fun isEmailValid(value: String): Boolean {
        return !emailPattern.matcher(value).matches()
    }

    fun isPasswordValid(value: String): Boolean {
        return !passwordPattern.matcher(value).matches()
    }
}