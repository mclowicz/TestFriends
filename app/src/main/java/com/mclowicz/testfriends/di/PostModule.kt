package com.mclowicz.testfriends.di

import com.mclowicz.testfriends.domain.post.InMemoryPostCatalog
import com.mclowicz.testfriends.domain.post.PostCatalog
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PostModule {

    @Provides
    @Singleton
    fun providePostCatalog(): PostCatalog = InMemoryPostCatalog()
}