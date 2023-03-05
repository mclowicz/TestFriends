package com.mclowicz.testfriends.di

import com.mclowicz.testfriends.domain.user.FriendsRepository
import com.mclowicz.testfriends.domain.user.UserCatalog
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PeopleModule {

    @Provides
    @Singleton
    fun providePeopleRepository(userCatalog: UserCatalog): FriendsRepository =
        FriendsRepository(userCatalog)
}