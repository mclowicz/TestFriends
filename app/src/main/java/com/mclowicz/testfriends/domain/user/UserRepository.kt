package com.mclowicz.testfriends.domain.user

import com.mclowicz.testfriends.domain.exceptions.BackendException
import com.mclowicz.testfriends.domain.exceptions.DuplicateAccountException
import com.mclowicz.testfriends.domain.exceptions.OfflineException
import com.mclowicz.testfriends.presentation.signup.Error
import com.mclowicz.testfriends.presentation.signup.SignupState
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userCatalog: UserCatalog,
    private val userDataStore: UserDataStore
) {

    suspend fun signUp(
        email: String,
        password: String,
    ): SignupState {
        return try {
            val user = userCatalog.createUser(email, password)
            userDataStore.storeLoggedInUserId(user.id)
            SignupState(user = user)
        } catch (e: DuplicateAccountException) {
            SignupState(error = Error.DuplicateAccount)
        } catch (e: BackendException) {
            SignupState(error = Error.Backend)
        } catch (e: OfflineException) {
            SignupState(error = Error.Offline)
        }
    }
}