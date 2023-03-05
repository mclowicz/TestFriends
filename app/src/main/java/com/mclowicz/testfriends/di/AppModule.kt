package com.mclowicz.testfriends.di

import com.mclowicz.testfriends.domain.post.PostCatalog
import com.mclowicz.testfriends.domain.post.PostRepository
import com.mclowicz.testfriends.domain.user.UserCatalog
import com.mclowicz.testfriends.domain.user.UserDataStore
import com.mclowicz.testfriends.domain.user.UserRepository
import com.mclowicz.testfriends.domain.validation.RegexCredentialsValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCredentialsValidator() = RegexCredentialsValidator()

    @Provides
    @Singleton
    fun provideUserRepository(
        userCatalog: UserCatalog,
        userDataStore: UserDataStore
    ): UserRepository = UserRepository(userCatalog, userDataStore)

    @Provides
    @Singleton
    fun providePostRepository(
        userDataStore: UserDataStore,
        postCatalog: PostCatalog
    ): PostRepository = PostRepository(userDataStore, postCatalog)
}