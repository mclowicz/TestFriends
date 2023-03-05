package com.mclowicz.testfriends.di

import com.mclowicz.testfriends.domain.user.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    @Provides
    @Singleton
    fun provideUserCatalog(): UserCatalog = InMemoryUserCatalog()

    @Provides
    @Singleton
    fun provideInMemoryUserDataStore(): UserDataStore = InMemoryUserDataStore()
}