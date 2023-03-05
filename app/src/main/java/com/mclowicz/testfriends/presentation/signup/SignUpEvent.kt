package com.mclowicz.testfriends.presentation.signup

import com.mclowicz.testfriends.domain.user.User

sealed class SignUpEvent {
    object FillingForm : SignUpEvent()
    class SignedUp(val user: User) : SignUpEvent() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            return true
        }

        override fun hashCode(): Int {
            return javaClass.hashCode()
        }
    }
}